package com.citiustech.HospitalManagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI hospitalManagementOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hospital Management API")
                        .description("API documentation for Hospital Management system")
                        .version("v1")
                        .contact(new Contact()
                                .name("Hospital Management Team"))
                );
    }
}
