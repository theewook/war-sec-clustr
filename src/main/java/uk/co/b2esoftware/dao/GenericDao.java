package uk.co.b2esoftware.dao;

import org.hibernate.LockMode;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Manuel DEQUEKER
 * Date: 17/10/2013
 * Time: 15:17
 */
public interface GenericDao
{
    /**
     * Save an object.
     * @param o object to save
     * @param <T> object type
     * @return object saved
     */
    public <T> T save(final T o);

    /**
     * Delete an object
     * @param object object to delete
     */
    public void delete(final Object object);

    /**
     * Retrieve an object
     * @param clazz object class
     * @param id identifier
     * @param <T> object type
     * @return object retrieved
     */
    public <T> T get(final Class<T> clazz, final Long id);

    /**
     * Retrieve an object
     * @param clazz object class
     * @param id identifier
     * @param <T> object type
     * @return object retrieved
     */
    public <T> T get(final Class<T> clazz, final String id);

    /**
     * Merge an object
     * @param o object
     * @param <T> object type
     * @return object merged
     */
    public <T> T merge(final T o);

    /**
     * Save or update an object
     * @param o object
     * @param <T> object type
     */
    public <T> void saveOrUpdate(final T o);

    /**
     * Get all objects
     *
     * @param clazz object class
     * @return list of object
     */
    public <T> List getAll(final Class<T> clazz);

    /**
     * Obtain the specified lock level upon the given object. This may be used to
     * perform a version check (<tt>LockMode.READ</tt>), to upgrade to a pessimistic
     * lock (<tt>LockMode.UPGRADE</tt>), or to simply reassociate a transient instance
     * with a session (<tt>LockMode.NONE</tt>). This operation cascades to associated
     * instances if the association is mapped with <tt>cascade="lock"</tt>.
     * @param object a persistent or transient instance
     * @param lockMode the lock level
     */
    public void lock(Object object, LockMode lockMode);
}
