package com.bot.forksendr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KsendrbotApplication {

    public static void main(String[] args) {
        SpringApplication.run(KsendrbotApplication.class, args);
    }

}
