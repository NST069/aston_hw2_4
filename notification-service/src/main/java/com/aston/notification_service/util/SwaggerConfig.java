package com.aston.notification_service.util;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${server.servlet.context-path:/}")
    private String contextPath;

    @Value("${spring.application.name}")
    private String appName;

    @Bean
    public OpenAPI NotficationServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(appName.toUpperCase() + " API")
                        .version("1.0")
                        .description("API для управления уведомлениями")
                )
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8081" + contextPath)
                                .description("Локальный сервер Notification Service"),
                        new Server()
                                .url("http://notifcation-service:8081" + contextPath)
                                .description("Docker контейнер Notification Service")
                ));
    }
}
