package fcode.backend.management;

import fcode.backend.management.config.interceptor.GatewayConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class ManagementApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        GatewayConstant.addApiEntities();
        SpringApplication.run(ManagementApplication.class, args);
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ManagementApplication.class);
    }
}