package com.wuai.company;

import com.wuai.company.order.util.ScenesConfig;
import com.wuai.company.order.util.VipCostConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.IOException;

import static com.wuai.company.Scanner.getDoc;

/**
 * Created by Ness on 2017/6/8.
 */
@SpringBootApplication
@EnableConfigurationProperties({ScenesConfig.class,VipCostConfig.class})
@EnableAsync
public class Service {

//    public static void main(String[] args) throws IOException {
//        getDoc();
//        ConfigurableApplicationContext service = SpringApplication.run(Service.class, args);
//        service.registerShutdownHook();
//        service.start();
//    }

    public static void main(String[] args){
        SpringApplication.run(Service.class, args);
    }
}
