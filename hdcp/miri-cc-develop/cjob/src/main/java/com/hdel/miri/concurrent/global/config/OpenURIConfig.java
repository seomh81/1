package com.hdel.miri.concurrent.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Slf4j
@Component
public class OpenURIConfig {
    //2023-10-13 add
    @Value("${spring.app.base-host}")
    public String BASE_URI;

    private static final String[] swaggerURIs = {
        "/swagger-ui/**"
        ,"/swagger-resources/**"
        ,"/api-docs"
        ,"/api-docs/**"
        ,"/v3/api-docs"
        ,"/v3/api-docs/**"
        ,"/miri-cc/swagger-ui/**"
        ,"/miri-cc/swagger-resources/**"
        ,"/miri-cc/api-docs"
        ,"/miri-cc/api-docs/**"
        ,"/miri-cc/v3/api-docs"
        ,"/miri-cc/v3/api-docs/**"
};

    private static final String[] openAuthURIs = {
        "/v2/auth/signin"
        ,"/v2/user/find-account"
        ,"/v2/user/find-password"
        ,"/v2/user/join/manager"
        ,"/v2/contract/anonymous/search"
        ,"/miri-cc/v2/auth/signin"
        ,"/miri-cc/v2/user/find-account"
        ,"/miri-cc/v2/user/find-password"
        ,"/miri-cc/v2/user/join/manager"
        ,"/miri-cc/v2/contract/anonymous/search"
};
    private static final String[] imagesURIs = {
        "/v2/storage/images/viewer/**"
        ,"/miri-cc/v2/storage/images/viewer/**"
    };

    private static final String[] concurrentURIs = {
        "/v2/cc/**"
        ,"/v2/message/**"
        ,"/miri-cc/v2/cc/**"
        ,"/miri-cc/v2/message/**"
    };

    private static final String[] refreshURI = {"/v2/auth/refresh","/miri-cc/v2/auth/refresh"};
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        return mapper;
    }

    public String[] getImagesURIs() {
        return imagesURIs;
    }
    public String[] getSwaggerURIs() {
        return swaggerURIs;
    }
    public String[] getOpenAuthURIs() {
        return openAuthURIs;
    }
    public String[] getRefreshURI() {
        return refreshURI;
    }

    public String[] getConcurrentURI() {
        return concurrentURIs;
    }
}
