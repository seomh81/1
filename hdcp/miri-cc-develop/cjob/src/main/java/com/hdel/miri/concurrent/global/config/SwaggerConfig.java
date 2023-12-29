package com.hdel.miri.concurrent.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class SwaggerConfig {

        private final String securitySchemeName = "bearerAuth";

        @Value("${springdoc.appTitle}")
        private String appTitle;

        @Value("${springdoc.appDescription}")
        private String appDescription;

        @Value("${springdoc.appContactName}")
        private String appContactName;

        @Value("${springdoc.appContactUrl}")
        private String appContactUrl;

        @Value("${springdoc.appContactEmail}")
        private String appContactEmail;

        @Bean
        public OpenAPI openAPI(@Value("${springdoc.appVersion}") String appVersion) {
                Info info = new Info().title(appTitle + " ").version(appVersion)
                                .description(appDescription)
                                .termsOfService("http://swagger.io/terms/")
                                .contact(new Contact().name(appContactName + " ").url(appContactUrl)
                                                .email(appContactEmail))
                                .license(new License().name("Apache License Version 2.0")
                                                .url("http://www.apache.org/licenses/LICENSE-2.0"));

                List<Server> serverList = new ArrayList();
                Server localServer = new Server();
                localServer.setDescription("local");
                localServer.setUrl("http://localhost:8080");

                Server devServer = new Server();
                devServer.setDescription("dev");
                devServer.setUrl("https://miricp-api-dev.hdel.co.kr/miri-cc");

                Server prodServerAP1 = new Server();
                prodServerAP1.setDescription("prod-AP1");
                prodServerAP1.setUrl("https://miri-api.hdel.co.kr/miri-cc");

                Server prodServerAP2 = new Server();
                prodServerAP2.setDescription("prod-AP2");
                prodServerAP2.setUrl("https://miricc-api.hdel.co.kr/miri-cc");

                serverList.add(localServer);
                serverList.add(devServer);
                serverList.add(prodServerAP1);
                serverList.add(prodServerAP2);

                return new OpenAPI()
                                .servers(serverList)
                                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                                .components(
                                                new Components()
                                                                .addSecuritySchemes(securitySchemeName,
                                                                                new SecurityScheme()
                                                                                                .name(securitySchemeName)
                                                                                                .type(SecurityScheme.Type.HTTP)
                                                                                                .scheme("bearer")
                                                                                                .bearerFormat("JWT")))
                                .info(info);
        }
}
