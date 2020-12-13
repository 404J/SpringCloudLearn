package com.mars;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class McEurekaApplication {
    public static void main(String[] args) {
        SpringApplication.run(McEurekaApplication.class, args);
    }
}
