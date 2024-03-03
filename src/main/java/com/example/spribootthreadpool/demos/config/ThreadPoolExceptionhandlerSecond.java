package com.example.spribootthreadpool.demos.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 默认异常处理器: 仅仅是输出err
 *
 * AsyncConfigurer在Spring种只能有一个启用的实现类
 */
//@Component
public class ThreadPoolExceptionhandlerSecond implements AsyncConfigurer {
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
//        return AsyncConfigurer.super.getAsyncUncaughtExceptionHandler();
        return (ex, method, params) -> {
            System.err.println("[ThreadPoolExceptionhandlerSecond]Err method: " + method + ", param: " + params + ":" + ex.getMessage());
        };
    }


}
