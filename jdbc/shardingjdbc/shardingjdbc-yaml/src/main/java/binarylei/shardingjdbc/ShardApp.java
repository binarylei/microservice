package binarylei.shardingjdbc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: qingshan
 * @Description: 咕泡学院，只为更好的你
 */
@SpringBootApplication(scanBasePackages = "binarylei.shardingjdbc.service")
@MapperScan(basePackages = "binarylei.shardingjdbc.dao")
public class ShardApp {

    public static void main(String[] args) {
        SpringApplication.run(ShardApp.class, args);
    }

}
