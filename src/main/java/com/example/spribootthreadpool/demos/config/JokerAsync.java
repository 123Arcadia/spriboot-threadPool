package com.example.spribootthreadpool.demos.config;

import org.springframework.core.annotation.AliasFor;
import org.springframework.scheduling.annotation.Async;

import java.lang.annotation.*;

/**
 * 扩展Async： 十七可以指定异常处理器
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Async
public @interface JokerAsync {

    @AliasFor(annotation = Async.class)
    String value() default "";

    String exceptionHandler() default "";
}

