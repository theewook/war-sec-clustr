package uk.co.b2esoftware.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by TheEwook on 10/02/2014.
 */
@Entity
@Table(name = "APP_ROLE")
public class Role implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TOKEN")
    private Token token;

    @Column(name = "ROLE", nullable = false)
    private String role;

    public Role()
    {
    }

    public Role(String role)
    {
        this.role = role;
    }

    public Role(Token token, String role)
    {
        this.token = token;
        this.role = role;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
    }

    public Token getToken()
    {
        return token;
    }

    public void setToken(Token token)
    {
        this.token = token;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Role role1 = (Role) o;

        if (!role.equals(role1.role)) return false;
        if (!token.equals(role1.token)) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = token.hashCode();
        result = 31 * result + role.hashCode();
        return result;
    }

    @Override
    public String toString()
    {
        return "Role{" +
                "id=" + id +
                ", token=" + token +
                ", role='" + role + '\'' +
                '}';
    }
}
