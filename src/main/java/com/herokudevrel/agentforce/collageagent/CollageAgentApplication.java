package com.herokudevrel.agentforce.collageagent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CollageAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(CollageAgentApplication.class, args);
    }

}
