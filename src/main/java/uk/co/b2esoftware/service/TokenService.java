package uk.co.b2esoftware.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.co.b2esoftware.Constants;
import uk.co.b2esoftware.Utils;
import uk.co.b2esoftware.dao.TokenDAO;
import uk.co.b2esoftware.entity.Role;
import uk.co.b2esoftware.entity.Token;
import uk.co.b2esoftware.scheduler.Semaphore;

import java.util.*;

/**
 * Created by Manuel DEQUEKER on 31/01/2014.
 */
@Service
public class TokenService
{
    @Autowired
    private TokenDAO tokenDAO;

    @Autowired
    private Semaphore semaphore;

    // 1 minute timeout
    public final static long TIMEOUT = 1 * 60 * 1000;

    private final static Map<String, Token> localMapToken = new HashMap<String, Token>();

    @Transactional
    public List<Token> listExternalToken()
    {
        return tokenDAO.getExternalToken();
    }

    @Transactional
    public Authentication getAuthenticationToken(String token)
    {
        Authentication authentication;

        Token tokenDetails = getToken(token);

        if (tokenDetails.getAuthentication() != null)
        {
            authentication = localMapToken.get(token).getAuthentication();
        }
        else
        {
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

            for (Role role : tokenDetails.getRoles())
            {
                authorities.add(new SimpleGrantedAuthority(role.getRole()));
            }

            User user = new User(tokenDetails.getUsername(), "unused", true, true, true, true, authorities);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                                    new UsernamePasswordAuthenticationToken(user, null, authorities);
            tokenDetails.setAuthentication(usernamePasswordAuthenticationToken);
            authentication = tokenDetails.getAuthentication();
        }

        return authentication;
    }

    @Transactional
    public Token generateToken(final String roles)
    {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        List<Role> listRoles = new ArrayList<Role>();

        Token tokenDetails = Utils.generateToken(true);

        for (String role : roles.split(","))
        {
            authorities.add(new SimpleGrantedAuthority(role));
            listRoles.add(new Role(tokenDetails, role));
        }

        String username = "user_" + new Date().getTime();
        User user = new User(username, "unused", true, true, true, true, authorities);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(user, null, authorities);

        tokenDetails.setUsername(username);
        tokenDetails.setRoles(listRoles);
        tokenDetails.setAuthentication(usernamePasswordAuthenticationToken);
        localMapToken.put(tokenDetails.getToken(), tokenDetails);
        addToken(tokenDetails.getToken(), tokenDetails);

        return tokenDetails;
    }

    @Transactional
    public void invalidateToken(final String token)
    {
        Token tokenDetails = getToken(token);

        if (tokenDetails != null)
        {
            tokenDAO.delete(tokenDetails);
            localMapToken.remove(token);
        }
    }

    @Transactional
    public Token getToken(final String token)
    {
        Token tokenDetails = localMapToken.get(token);

        if (tokenDetails == null)
        {
            tokenDetails = tokenDAO.get(Token.class, token);
            if (tokenDetails != null)
            {
                localMapToken.put(token, tokenDetails);
            }
        }

        return tokenDetails;
    }

    @Transactional
    public void addToken(final String token, final Token tokenDetails)
    {
        tokenDAO.save(tokenDetails);
        localMapToken.put(token, tokenDetails);
    }

    @Transactional
    public boolean authorizeToken(final String token)
    {
        boolean authorize = false;
        Token tokenDetails = getToken(token);
        Date now = new Date();

        if (tokenDetails != null)
        {
            if (tokenDetails.isExternal())
            {
                authorize = now.getTime() < tokenDetails.getExpiryDate().getTime();
            }
            else
            {
                authorize = tokenDetails.getLastRequestTime().getTime() >= now.getTime() - TIMEOUT;
            }

            tokenDetails.setLastRequestTime(now);
            tokenDAO.saveOrUpdate(tokenDetails);
        }

        return authorize;
    }

    @Transactional
    public void cleanExpiredSession()
    {
        Date now = new Date();

        if (semaphore.lock(Constants.LOCK_CLEAN_SESSION))
        {
            List<Token> tokens = tokenDAO.getInternalToken();

            if (tokens != null)
            {
                for (Token token : tokens)
                {
                    if (token.getLastRequestTime().getTime() < now.getTime() - TIMEOUT)
                    {
                        tokenDAO.delete(token);
                    }
                }
            }

            semaphore.unlock(Constants.LOCK_CLEAN_SESSION);
        }

        Iterator it = localMapToken.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry<String, Token> token = (Map.Entry<String, Token>) it.next();
            if (!token.getValue().isExternal()
                    && token.getValue().getLastRequestTime().getTime() < now.getTime() - TIMEOUT)
            {
                it.remove();
            }
        }
    }
}
