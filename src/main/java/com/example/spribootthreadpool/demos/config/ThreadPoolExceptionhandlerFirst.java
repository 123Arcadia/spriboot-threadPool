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
 * AsyncConfigurer在Spring种只能有一个启用的实现类
 */
@Component
public class ThreadPoolExceptionhandlerFirst implements AsyncConfigurer {
    //    @Override
//    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
////        return AsyncConfigurer.super.getAsyncUncaughtExceptionHandler();
//        return (ex, method, params) -> {
//            System.err.println("Unexpected exception occurred invoking async method: " + method + ", param: " + params +":" + ex.getMessage());
//        };
//    }

    /**
     * required : 定义该属性是否允许为nul
     */
    @Autowired(required = false)
    private Map<String, AsyncUncaughtExceptionHandler> exceptionHandlerMap = new HashMap<>();

    private final AsyncUncaughtExceptionHandler defaultExceptionHandler = new SimpleAsyncUncaughtExceptionHandler();

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {

        return (ex, method, params) -> {
            // 得到别名
            String qualifier = getExceptionHandlerQualifier(method);
//            System.out.println("[ThreadPoolExceptionhandlerFirst]qualifier = " + qualifier);
            //[ThreadPoolExceptionhandlerFirst]qualifier = ThreadPoolExceptionhandlerFirst
            AsyncUncaughtExceptionHandler exceptionHandler = null;

            if (Objects.nonNull(qualifier) && qualifier.length() > 0) {
                exceptionHandler = exceptionHandlerMap.get(qualifier);
            }

            if (Objects.isNull(exceptionHandler)) {
                exceptionHandler = defaultExceptionHandler;
            }
            // 调用的默认处理器
            exceptionHandler.handleUncaughtException(ex, method, params);

        };

    }

    protected String getExceptionHandlerQualifier(Method method) {

        JokerAsync async = AnnotatedElementUtils.findMergedAnnotation(method, JokerAsync.class);

        if (async == null) {
            async = AnnotatedElementUtils.findMergedAnnotation(method.getDeclaringClass(), JokerAsync.class);
        }

        return (async != null ? async.exceptionHandler() : null);

    }
}
