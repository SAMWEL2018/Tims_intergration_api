package com.tims.intergration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class IntergrationApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(IntergrationApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(IntergrationApplication.class);
    }

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

}
