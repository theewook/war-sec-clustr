package uk.co.b2esoftware;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.preauth.j2ee.J2eePreAuthenticatedProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Manuel DEQUEKER on 31/01/2014.
 */
public class CustomJ2eePreAuthenticatedProcessingFilter extends J2eePreAuthenticatedProcessingFilter
{
    private String token = null;

    @Autowired
    private TokenManagement tokenManagement;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException
    {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        this.token = Utils.getTokenFromCookie(request.getCookies(), Constants.COOKIE_TOKEN_NAME);

        if (token == null)
        {
            token = request.getHeader(Constants.COOKIE_TOKEN_NAME);
        }

        if (token != null && tokenManagement.getToken(token) == null)
        {
            this.token = null;
        }

        if (token != null)
        {
            logger.info("Authentication by token");

            if (tokenManagement.authorizeToken(this.token))
            {
                TokenDetails tokenDetails = tokenManagement.getToken(this.token);
                SecurityContextHolder.getContext().setAuthentication(tokenDetails.getAuthentication());
                logger.info(token + " +++ " + tokenDetails.getAuthentication());
            }
            else
            {
                tokenManagement.invalidateToken(this.token);
                Utils.deleteCookie(request, response, Constants.COOKIE_TOKEN_NAME);

                SecurityContextHolder.clearContext();
                request.getSession().invalidate();
            }
            chain.doFilter(request, response);
        }
        else
        {
            logger.info("Authentication by container");

            TokenDetails tokenDetails = Utils.generateToken(false, null);
            Utils.saveTokenToCookie(response, Constants.COOKIE_TOKEN_NAME, tokenDetails.getToken());
            super.doFilter(req, res, chain);

            if (SecurityContextHolder.getContext().getAuthentication() instanceof PreAuthenticatedAuthenticationToken
                    && SecurityContextHolder.getContext().getAuthentication().isAuthenticated())
            {
                tokenDetails.setAuthentication(SecurityContextHolder.getContext().getAuthentication());
                token = tokenDetails.getToken();
                tokenManagement.addToken(token, tokenDetails);
            }

            logger.info(token + " +++ " + SecurityContextHolder.getContext().getAuthentication());
        }
    }
}
