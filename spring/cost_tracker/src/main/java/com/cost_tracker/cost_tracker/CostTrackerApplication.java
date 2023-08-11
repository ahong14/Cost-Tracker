package com.cost_tracker.cost_tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication setup application context, beans, equivalent to @Configuration, @EnableAutoConfiguration and @ComponentScan
@SpringBootApplication
public class CostTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CostTrackerApplication.class, args);
    }

}
