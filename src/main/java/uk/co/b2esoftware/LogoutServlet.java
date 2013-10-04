package uk.co.b2esoftware;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Does the necessary session invalidation as per the servlet spec.
 * User: art
 * Date: 02/10/2013
 * Time: 10:43
 */
public class LogoutServlet extends HttpServlet
{
    Logger log = LoggerFactory.getLogger(LogoutServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        log.debug("Processing POST request");
        logout(request, response);
    }

    private void logout(final HttpServletRequest request, final HttpServletResponse response)
        throws IOException
    {
        request.getSession().invalidate();
        response.sendRedirect(request.getContextPath());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        log.debug("Processing GET request");
        logout(request, response);
    }
}
