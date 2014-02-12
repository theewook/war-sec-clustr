package uk.co.b2esoftware.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by Manuel DEQUEKER on 10/02/2014.
 */
@Entity
@Table(name = "APP_LOCK")
public class Lock implements Serializable
{
    @Id
    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "VALUE", nullable = false)
    private Boolean value;

    public Lock()
    {
    }

    public Lock(String name, Boolean value)
    {
        this.name = name;
        this.value = value;
    }

    public void lock()
    {
        this.value = true;
    }

    public void unlock()
    {
        this.value = false;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Boolean getValue()
    {
        return value;
    }

    public void setValue(Boolean value)
    {
        this.value = value;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Lock lock = (Lock) o;

        if (name != null ? !name.equals(lock.name) : lock.name != null) return false;
        if (value != null ? !value.equals(lock.value) : lock.value != null) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return "Lock{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
