package spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author binarylei
 * @version 2019-10-28
 */
public class ScheduledXmlTest {
    private Logger logger = LoggerFactory.getLogger(ScheduledXmlTest.class);

    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("spring-context-schedule.xml");
    }

    public void fixedDelay() {
        logger.info("fixedDelay");
    }

    public void fixedRate() {
        logger.info("fixedRate");
    }

    public void cron() {
        logger.info("cron");
    }
}
