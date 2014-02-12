package uk.co.b2esoftware.dao;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Manuel DEQUEKER
 * Date: 17/10/2013
 * Time: 15:13
 */
public abstract class GenericDaoImpl implements GenericDao
{
    @Autowired
    private SessionFactory sessionFactory;

    /**
     * Get the current hibernate session
     * @return current session
     */
    protected final Session getCurrentSession()
    {
        return sessionFactory.getCurrentSession();
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public <T> T save(final T o)
    {
        return (T) sessionFactory.getCurrentSession().save(o);
    }

    /** {@inheritDoc} */
    public void delete(final Object object)
    {
        sessionFactory.getCurrentSession().delete(object);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public <T> T get(final Class<T> clazz, final Long id)
    {
        return (T) sessionFactory.getCurrentSession().get(clazz, id);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public <T> T get(final Class<T> clazz, final String id)
    {
        return (T) sessionFactory.getCurrentSession().get(clazz, id);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public <T> T merge(final T o)
    {
        return (T) sessionFactory.getCurrentSession().merge(o);
    }

    /** {@inheritDoc} */
    public <T> void saveOrUpdate(final T o)
    {
        sessionFactory.getCurrentSession().saveOrUpdate(o);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public <T> List<T> getAll(final Class<T> clazz)
    {
        final Session session = sessionFactory.getCurrentSession();
        final Criteria crit = session.createCriteria(clazz);
        return crit.list();
    }

    /** {@inheritDoc} */
    public void lock(Object object, LockMode lockMode)
    {
        sessionFactory.getCurrentSession().lock(object, lockMode);
    }
}
