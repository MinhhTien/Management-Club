package fcode.backend.management;

import fcode.backend.management.config.interceptor.GatewayConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class ManagementApplication {

    public static void main(String[] args) {
        GatewayConstant.addApiEntities();
        SpringApplication.run(ManagementApplication.class, args);
    }

}
