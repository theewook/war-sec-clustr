package uk.co.b2esoftware.vo;

import org.springframework.security.core.GrantedAuthority;
import uk.co.b2esoftware.TokenDetails;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;

/**
 * Created by TheEwook on 06/02/2014.
 */
public class TokenVO
{
    private final String token;
    private final Collection<? extends GrantedAuthority> roles;
    private final long expiryDate;

    public TokenVO(TokenDetails tokenDetails)
    {
        this.token = tokenDetails.getToken();
        this.roles = tokenDetails.getAuthentication().getAuthorities();
        this.expiryDate = tokenDetails.getExpiryDate();
    }

    public String getExpiryDateFormatted()
    {
        if (expiryDate != -1)
        {
            DateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
            return sdf.format(expiryDate);
        }
        else
        {
            return "Infinite";
        }
    }

    public String getToken()
    {
        return token;
    }

    public Collection<? extends GrantedAuthority> getRoles()
    {
        return roles;
    }

    public long getExpiryDate()
    {
        return expiryDate;
    }
}
