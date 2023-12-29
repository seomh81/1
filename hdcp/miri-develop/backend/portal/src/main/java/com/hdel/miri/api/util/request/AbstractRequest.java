package com.hdel.miri.api.util.request;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.context.SecurityContextHolder;

public class AbstractRequest implements CurrentUser {
    @Schema(type = "String", example = "ko_kr", description = "언어 설정 정보")
    private String userLocale;

    @Hidden
    protected String currentUser;

    @Hidden
    @Setter
    @Getter
    private Boolean mobile;

    public AbstractRequest(){
        try {
            this.currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        } catch ( NullPointerException e){
            this.currentUser ="anonymousUser";
        }
    }
    @Override
    public String getCurrentUser() {
        return this.currentUser;
    }
    public String getUserLocale() { return ( userLocale==null ? "ko_kr" : userLocale); }
    public void setUserLocale(String userLocale) { this.userLocale = userLocale; }

}
