package uk.co.b2esoftware.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import uk.co.b2esoftware.dao.LockDAO;
import uk.co.b2esoftware.entity.Lock;

/**
 * Created by Manuel DEQUEKER on 11/02/2014.
 */
@Component
public class Semaphore
{
    @Autowired
    private LockDAO lockDao;

    @Transactional
    public boolean lock(String name)
    {
        Lock lock = lockDao.get(Lock.class, name);
        if (!lock.getValue())
        {
            lock.lock();
            lockDao.saveOrUpdate(lock);
            return true;
        }
        else
        {
            return false;
        }
    }

    @Transactional
    public void unlock(String name)
    {
        Lock lock = lockDao.get(Lock.class, name);
        if (lock.getValue())
        {
            lock.unlock();
            lockDao.saveOrUpdate(lock);
        }
    }
}
