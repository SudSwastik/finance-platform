package com.finance.platform.goals;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.finance.platform.goals",
        "com.finance.platform.security"
})
public class GoalsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoalsServiceApplication.class, args);
    }
}
