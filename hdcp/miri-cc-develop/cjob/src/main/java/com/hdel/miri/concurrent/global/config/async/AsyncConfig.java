package com.hdel.miri.concurrent.global.config.async;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Value("${spring.tomcat.corePoolSize}")
    private int CORE_POOL_SIZE = 50;

    @Value("${spring.tomcat.maxPoolSize}")
    private int MAX_POOL_SIZE = 1000;

    @Value("${spring.tomcat.queueCapacity}")
    private int QUEUE_CAPACITY = 100_000_000;

    @Bean(name="CcAsyncExcutor1")
    public Executor threadPooltaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(CORE_POOL_SIZE);
        taskExecutor.setMaxPoolSize(MAX_POOL_SIZE);
        taskExecutor.setQueueCapacity(QUEUE_CAPACITY);
        taskExecutor.setTaskDecorator(new CustomDecorator()); 
        taskExecutor.setThreadNamePrefix("CC-EXECUTOR-");

        return taskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (AsyncUncaughtExceptionHandler) new AsyncExceptionHandler(); 
    }
}
