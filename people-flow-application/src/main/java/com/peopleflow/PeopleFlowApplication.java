package com.peopleflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PeopleFlowApplication {
    public static void main(String[] args) {
        SpringApplication.run(PeopleFlowApplication.class, args);
    }
} 