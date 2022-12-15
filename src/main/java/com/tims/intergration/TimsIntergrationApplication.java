package com.tims.intergration;

import com.tims.intergration.model.Buyer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class TimsIntergrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimsIntergrationApplication.class, args);
    }

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
