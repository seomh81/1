package com.hdel.miri.concurrent.global.config.async;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable ex, java.lang.reflect.Method method, Object... params) {
        log.error( ex.getMessage(), ex );        
    }
}
