package uk.co.b2esoftware.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import uk.co.b2esoftware.Constants;
import uk.co.b2esoftware.Utils;
import uk.co.b2esoftware.service.TokenService;

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
public class RestAuthenticatedProcessingFilter extends GenericFilterBean
{
    @Autowired
    private TokenService tokenManagement;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException
    {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String token = request.getHeader(Constants.TOKEN_NAME);

        logger.info("Authentication by token");
        doAuthentication(token, request, response);

        chain.doFilter(request, response);
    }

    public void doAuthentication(String token, HttpServletRequest request, HttpServletResponse response)
    {
        if (tokenManagement.authorizeToken(token))
        {
            SecurityContextHolder.getContext().setAuthentication(tokenManagement.getAuthenticationToken(token));
            logger.info(token + " +++ " + tokenManagement.getAuthenticationToken(token));
        }
        else
        {
            tokenManagement.invalidateToken(token);
            Utils.deleteCookie(request, response, Constants.TOKEN_NAME);

            SecurityContextHolder.clearContext();
            request.getSession().invalidate();
        }
    }
}
