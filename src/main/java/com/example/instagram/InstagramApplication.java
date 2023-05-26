package com.example.instagram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableConfigurationProperties
public class InstagramApplication {

    public static void main(String[] args) {
        SpringApplication.run(InstagramApplication.class, args);
    }

}
