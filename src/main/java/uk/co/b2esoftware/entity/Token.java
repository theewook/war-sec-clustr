package uk.co.b2esoftware.entity;

import org.springframework.security.core.Authentication;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Manuel DEQUEKER on 06/02/2014.
 */
@Entity
@Table(name = "APP_TOKEN")
public class Token implements Serializable
{
    @Id
    @Column(name = "TOKEN", nullable = false)
    private String token;

    @Column(name = "USERNAME", nullable = false)
    private String username;

    @Column(name = "EXTERNAL", nullable = false)
    private Boolean external;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EXPIRYDATE", nullable = true)
    private Date expiryDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LASTREQUESTTIME", nullable = true)
    private Date lastRequestTime;

    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "token")
    private List<Role> roles;

    @Transient
    private Authentication authentication;

    public Token()
    {
    }

    public Token(String token, Boolean external, Date expiryDate, Date lastRequestTime)
    {
        this.token = token;
        this.external = external;
        this.expiryDate = expiryDate;
        this.lastRequestTime = lastRequestTime;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public Boolean isExternal()
    {
        return external;
    }

    public void setExternal(Boolean external)
    {
        this.external = external;
    }

    public Date getExpiryDate()
    {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate)
    {
        this.expiryDate = expiryDate;
    }

    public Date getLastRequestTime()
    {
        return lastRequestTime;
    }

    public void setLastRequestTime(Date lastRequestTime)
    {
        this.lastRequestTime = lastRequestTime;
    }

    public List<Role> getRoles()
    {
        return roles;
    }

    public void setRoles(List<Role> roles)
    {
        this.roles = roles;
    }

    public Authentication getAuthentication()
    {
        return authentication;
    }

    public void setAuthentication(Authentication authentication)
    {
        this.authentication = authentication;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token token1 = (Token) o;

        if (!token.equals(token1.token)) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        return token.hashCode();
    }

    @Override
    public String toString()
    {
        return "Token{" +
                "token='" + token + '\'' +
                ", username='" + username + '\'' +
                ", external=" + external +
                ", expiryDate=" + expiryDate +
                ", lastRequestTime=" + lastRequestTime +
                ", roles=" + roles +
                ", authentication=" + authentication +
                '}';
    }
}
