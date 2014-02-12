package uk.co.b2esoftware.vo;

import uk.co.b2esoftware.entity.Role;
import uk.co.b2esoftware.entity.Token;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by TheEwook on 06/02/2014.
 */
public class TokenVO
{
    private final String token;
    private final List<Role> roles;
    private final Date expiryDate;

    public TokenVO(Token tokenDetails)
    {
        this.token = tokenDetails.getToken();
        this.roles = tokenDetails.getRoles();
        this.expiryDate = tokenDetails.getExpiryDate();
    }

    public String getExpiryDateFormatted()
    {
        DateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        return sdf.format(expiryDate);
    }

    public String getToken()
    {
        return token;
    }

    public List<Role> getRoles()
    {
        return roles;
    }

    public Date getExpiryDate()
    {
        return expiryDate;
    }
}
