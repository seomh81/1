package com.hdel.miri.concurrent.util.request;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.security.core.context.SecurityContextHolder;

public class DefaultAbstract implements CurrentUser {
    @Hidden
    protected String currentUser;
    public DefaultAbstract(){
        this.currentUser = "CONCURRENT USER";
    }
    @Override
    public String getCurrentUser() {
        return this.currentUser;
    }
}
