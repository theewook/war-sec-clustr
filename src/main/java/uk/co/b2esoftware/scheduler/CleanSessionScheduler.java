package uk.co.b2esoftware.scheduler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uk.co.b2esoftware.service.TokenService;

/**
 * Created by Manuel DEQUEKER on 11/02/2014.
 */
@Component
public class CleanSessionScheduler
{
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private TokenService tokenService;

    @Scheduled(fixedDelayString = "${cleanSession.fixedDelay}")
    public void cleanSession()
    {
        logger.info("Clean Expired Sessions");
        tokenService.cleanExpiredSession();
    }
}
