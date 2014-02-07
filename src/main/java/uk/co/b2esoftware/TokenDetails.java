package uk.co.b2esoftware;

import org.springframework.security.core.Authentication;

import java.util.Date;

/**
 * Created by Manuel DEQUEKER on 06/02/2014.
 */
public class TokenDetails
{
    private final String token;
    private final boolean external;
    private final long expiryDate;
    private long lastRequestTime;
    private Authentication authentication;

    public TokenDetails(String token, boolean external, long expiryDate, Authentication authentication)
    {
        this.token = token;
        this.external = external;
        this.expiryDate = expiryDate;
        this.lastRequestTime = new Date().getTime();
        this.authentication = authentication;
    }

    @Override
    public String toString()
    {
        return "TokenDetails{" +
                "token='" + token + '\'' +
                ", external=" + external +
                ", expiryDate=" + expiryDate +
                ", lastRequestTime=" + lastRequestTime +
                ", authentication=" + authentication +
                '}';
    }

    public String getToken()
    {
        return token;
    }

    public boolean isExternal()
    {
        return external;
    }

    public long getExpiryDate()
    {
        return expiryDate;
    }

    public void setAuthentication(Authentication authentication)
    {
        this.authentication = authentication;
    }

    public void setLastRequestTime(long lastRequestTime)
    {
        this.lastRequestTime = lastRequestTime;
    }

    public long getLastRequestTime()
    {
        return lastRequestTime;
    }

    public Authentication getAuthentication()
    {
        return authentication;
    }
}
