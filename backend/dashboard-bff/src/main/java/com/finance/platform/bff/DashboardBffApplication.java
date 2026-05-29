package com.finance.platform.bff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(scanBasePackages = {
        "com.finance.platform.bff",
        "com.finance.platform.security"
})
@ConfigurationPropertiesScan
public class DashboardBffApplication {

    public static void main(String[] args) {
        SpringApplication.run(DashboardBffApplication.class, args);
    }
}
