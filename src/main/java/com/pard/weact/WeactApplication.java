package com.pard.weact;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableConfigurationProperties(JwtProperties.class)
public class WeactApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeactApplication.class, args);
    }

}
