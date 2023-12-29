package com.hdel.miri.api.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Slf4j
@Component
public class OpenURIConfig {

    @Value("${spring.app.base-host}")
    public String BASE_URI;

    private static final String[] swaggerURIs = {
        "/swagger-ui/**"
        ,"/swagger-resources/**"
        ,"/api-docs"
        ,"/api-docs/**"
        ,"/v3/api-docs"
        ,"/v3/api-docs/**"
        ,"/miri/swagger-ui/**"
        ,"/miri/swagger-resources/**"
        ,"/miri/api-docs"
        ,"/miri/api-docs/**"
        ,"/miri/v3/api-docs"
        ,"/miri/v3/api-docs/**"
};
    private static final String[] openAuthURIs = {
        "/v2/auth/signin"
        ,"/v2/user/find-account"
        ,"/v2/user/find-password"
        ,"/v2/user/already"
        ,"/v2/user/confirm-accept"
        ,"/v2/user/join/manager"
        ,"/v2/user/join/autoManager"
        ,"/v2/contract/anonymous/search"
        ,"/v2/ad/signin/images"
        ,"/miri/v2/auth/signin"
        ,"/miri/v2/user/find-account"
        ,"/miri/v2/user/find-password"
        ,"/miri/v2/user/already"
        ,"/miri/v2/user/confirm-accept"
        ,"/miri/v2/user/join/manager"
        ,"/miri/v2/user/join/autoManager"
        ,"/miri/v2/contract/anonymous/search"
        ,"/miri/v2/ad/signin/images"

    };

    private static final String[] openTokenURIs = {
            "/v2/deeplink/verify/token"
            , "/miri/v2/deeplink/verify/token"
    };
    private static final String[] imagesURIs = {
        "/v2/storage/images/viewer/**"
        ,"/miri/v2/storage/images/viewer/**"
        ,"/v2/storage/contract/**"
        ,"/miri/v2/storage/contract/**"
    };
    private static final String[] refreshURI = {"/v2/auth/refresh","/miri/v2/auth/refresh"};

    /**
     * Logging 제외 URL
     */
    private static final String[] excludeWrapper = {
        "/v2/storage/images/upload"
        ,"/v2/ad/section/details/create"
        ,"/v2/ad/signin/details/create"
        ,"/miri/v2/storage/images/upload"
        ,"/miri/v2/ad/section/details/create"
        ,"/miri/v2/ad/signin/details/create"
    };

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
    public String[] getOpenTokenURIs() {
        return openTokenURIs;
    }
    public String[] getRefreshURI() {
        return refreshURI;
    }
    public String[] getExcludeWrapper(){
        return excludeWrapper;
    }
}
