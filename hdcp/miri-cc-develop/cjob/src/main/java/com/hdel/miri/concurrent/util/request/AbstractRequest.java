package com.hdel.miri.concurrent.util.request;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.security.core.context.SecurityContextHolder;

public class AbstractRequest implements CurrentUser {

    @Hidden
    private String userLocale = "ko_kr";

    @Hidden
    protected String currentUser;
    public AbstractRequest(){
        this.currentUser = "CONCURRENT USER";
    }
    @Override
    public String getCurrentUser() {
        return this.currentUser;
    }
    public String getUserLocale() { return (userLocale==null?"ko_kr" : userLocale); }
    public void setUserLocale(String userLocale) { this.userLocale = userLocale; }
}
