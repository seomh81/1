package com.hdel.miri.concurrent.global.config.database.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Documented
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface DS3Annotation {
    String value() default "";
}
