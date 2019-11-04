package spring;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author binarylei
 * @version 2019-10-28
 */
@EnableAsync(proxyTargetClass = false, mode = AdviceMode.PROXY)
public class AsyncTest {
    private static final Logger logger = LoggerFactory.getLogger(AsyncTest.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(AsyncTest.class);

        AsyncTest asyncTest = context.getBean(AsyncTest.class);
        asyncTest.async();
        logger.info("main method");
    }

    @Async("myExecutor")
    public void async() {
        logger.info("async method");
    }

    @Bean
    public Executor myExecutor() {
        return Executors.newSingleThreadExecutor(
                new ThreadFactoryBuilder()
                        .setNameFormat("test-thread")
                        .build());
    }

}
