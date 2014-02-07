package uk.co.b2esoftware.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.co.b2esoftware.Constants;
import uk.co.b2esoftware.TokenManagement;
import uk.co.b2esoftware.Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

/**
 * Created by Manuel DEQUEKER on 05/02/2014.
 */
@Controller
public class LoginController
{
    @Autowired
    TokenManagement tokenManagement;

    @RequestMapping(value="/", method = RequestMethod.GET)
    public String welcome(ModelMap model, Principal principal )
    {
        String name = principal.getName();
        model.addAttribute("username", name);
        return "index";
    }

    @RequestMapping(value="/login", method = RequestMethod.GET)
    public String login(ModelMap model)
    {
        return "login";
    }

    @RequestMapping(value="/loginfailed", method = RequestMethod.GET)
    public String loginfailed(ModelMap model)
    {
        model.addAttribute("error", true);
        return "login";
    }

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logout(ModelMap model, HttpServletRequest request, HttpServletResponse httpResponse)
    {
        String token = Utils.getTokenFromCookie(request.getCookies(), Constants.COOKIE_TOKEN_NAME);

        tokenManagement.invalidateToken(token);
        Utils.deleteCookie(request, httpResponse, Constants.COOKIE_TOKEN_NAME);

        SecurityContextHolder.clearContext();
        request.getSession().invalidate();

        return "login";
    }
}
