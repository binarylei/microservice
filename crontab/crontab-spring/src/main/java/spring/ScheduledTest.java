package spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author binarylei
 * @version 2019-10-28
 */
@EnableScheduling
public class ScheduledTest {
    private Logger logger = LoggerFactory.getLogger(ScheduledTest.class);

    public static void main(String[] args) {
        SpringApplication.run(ScheduledTest.class);
    }

    @Scheduled(initialDelay = 2000)
    public void initialDelay() {
        logger.info("initialDelay");
    }

//    @Scheduled(fixedDelay = 2000)
//    public void fixedDelay() {
//        logger.info("fixedDelay");
//    }
//
//    @Scheduled(fixedRate = 2000)
//    public void fixedRate() {
//        logger.info("fixedRate");
//    }
//
//    @Scheduled(cron = "0/2 * * * * *")
//    public void cron() {
//        logger.info("cron");
//    }
}
