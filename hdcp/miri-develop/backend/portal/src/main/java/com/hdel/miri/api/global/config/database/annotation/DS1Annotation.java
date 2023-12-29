package com.hdel.miri.api.global.config.database.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Documented
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface DS1Annotation {
    String value() default "";
}
