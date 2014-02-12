package uk.co.b2esoftware.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.preauth.j2ee.J2eePreAuthenticatedProcessingFilter;
import uk.co.b2esoftware.Constants;
import uk.co.b2esoftware.Utils;
import uk.co.b2esoftware.entity.Role;
import uk.co.b2esoftware.entity.Token;
import uk.co.b2esoftware.service.TokenService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Manuel DEQUEKER on 31/01/2014.
 */
public class CustomJ2eePreAuthenticatedProcessingFilter extends J2eePreAuthenticatedProcessingFilter
{
    @Autowired
    private TokenService tokenService;

    @Autowired
    private RestAuthenticatedProcessingFilter restAuthenticatedProcessingFilter;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException
    {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String token = Utils.getTokenFromCookie(request.getCookies(), Constants.TOKEN_NAME);

        if (token != null && tokenService.getToken(token) == null)
        {
            token = null;
        }

        if (token != null)
        {
            restAuthenticatedProcessingFilter.doAuthentication(token, request, response);
            chain.doFilter(request, response);
        }
        else
        {
            logger.info("Authentication by container");

            Token tokenDetails = Utils.generateToken(false);
            Utils.saveTokenToCookie(response, Constants.TOKEN_NAME, tokenDetails.getToken());
            super.doFilter(req, res, chain);

            if (SecurityContextHolder.getContext().getAuthentication() instanceof PreAuthenticatedAuthenticationToken
                    && SecurityContextHolder.getContext().getAuthentication().isAuthenticated())
            {
                List<Role> roles = new ArrayList<Role>();
                for (GrantedAuthority role : SecurityContextHolder.getContext().getAuthentication().getAuthorities())
                {
                    roles.add(new Role(tokenDetails, role.getAuthority()));
                }

                tokenDetails.setUsername(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
                tokenDetails.setRoles(roles);
                tokenDetails.setLastRequestTime(new Date());
                token = tokenDetails.getToken();
                tokenService.addToken(token, tokenDetails);
            }

            logger.info(token + " +++ " + SecurityContextHolder.getContext().getAuthentication());
        }
    }
}
