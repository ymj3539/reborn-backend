package com.rainbowbridge.reborn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class RebornBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(RebornBackendApplication.class, args);
    }

}
