package uk.co.b2esoftware;

import org.springframework.security.core.Authentication;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by TheEwook on 05/02/2014.
 */
public class Utils
{
    public static TokenDetails generateToken(final boolean external, Authentication authentication)
    {
        SecureRandom random = new SecureRandom();
        String token = new BigInteger(100, random).toString(32);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);

        TokenDetails tokenDetails = new TokenDetails(token, external, cal.getTime().getTime(), authentication);

        return tokenDetails;
    }

    public static void saveTokenToCookie(final HttpServletResponse httpResponse, final String cookieName, final String tokenValue)
    {
        Cookie tokenCookie = new Cookie(cookieName, tokenValue);
        tokenCookie.setPath("/" + Constants.CONTEXT_NAME);
        tokenCookie.setMaxAge((int) TokenManagement.TIMEOUT);
        httpResponse.addCookie(tokenCookie);
    }

    public static String getTokenFromCookie(final Cookie[] cookies, final String name)
    {
        if (cookies == null || cookies.length == 0)
        {
            return null;
        }

        Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();

        for (int i=0; i < cookies.length; i++)
        {
            Cookie cookie = cookies[i];
            cookieMap.put(cookie.getName(), cookie);
        }

        Cookie cookie = cookieMap.get(name);
        return cookie != null ? cookie.getValue() : null;
    }

    public static void deleteCookie(final HttpServletRequest httpRequest, final HttpServletResponse httpResponse,
                                    final String cookieName)
    {
        Cookie[] cookies = httpRequest.getCookies();

        if (cookies != null)
        {
            for (Cookie cookie : cookies)
            {
                if (cookie.getName().equals(cookieName))
                {
                    cookie.setValue(null);
                    cookie.setMaxAge(0);
                    cookie.setPath("/" + Constants.CONTEXT_NAME);
                    httpResponse.addCookie(cookie);
                    break;
                }
            }
        }
    }

    private Utils()
    {
    }
}
