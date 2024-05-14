package com.micropos.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class PosDiscoveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(PosDiscoveryApplication.class, args);
    }
}
