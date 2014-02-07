package uk.co.b2esoftware;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Manuel DEQUEKER on 31/01/2014.
 */
@Service
public class TokenManagement
{
    // 5 minutes timeout
    public final static long TIMEOUT = 5 * 60000;

    private final Map<String, TokenDetails> mapToken = new HashMap<String, TokenDetails>();

    public List<TokenDetails> listExternalToken()
    {
        List<TokenDetails> list = new ArrayList<TokenDetails>();
        for (Map.Entry entry : mapToken.entrySet())
        {
            TokenDetails token = (TokenDetails) entry.getValue();

            if (token.isExternal())
            {
                list.add(token);
            }
        }
        return list;
    }

    public TokenDetails generateToken(final String roles)
    {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        for (String role : roles.split(","))
        {
            authorities.add(new SimpleGrantedAuthority(role));
        }

        User user = new User("user_" + new Date().getTime(), "unused", true, true, true, true, authorities);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(user, null, authorities);

        TokenDetails tokenDetails = Utils.generateToken(true, usernamePasswordAuthenticationToken);
        mapToken.put(tokenDetails.getToken(), tokenDetails);

        return tokenDetails;
    }

    public void invalidateToken(final String token)
    {
        mapToken.remove(token);
    }

    public TokenDetails getToken(final String token)
    {
        return mapToken.get(token);
    }

    public void addToken(final String token, final TokenDetails tokenDetails)
    {
        mapToken.put(token, tokenDetails);
    }

    public boolean authorizeToken(final String token)
    {
        boolean authorize = false;
        TokenDetails tokenDetails = getToken(token);
        Date now = new Date();

        if (tokenDetails != null)
        {
            if (tokenDetails.isExternal())
            {
                authorize = now.getTime() < tokenDetails.getExpiryDate();
            }
            else
            {
                if (tokenDetails.getLastRequestTime() >= now.getTime() - TIMEOUT)
                {
                    tokenDetails.setLastRequestTime(now.getTime());
                    authorize = true;
                }
                else
                {
                    authorize = false;
                }
            }
        }

        return authorize;
    }
}
