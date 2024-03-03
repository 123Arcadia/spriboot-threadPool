//package com.example.spribootthreadpool.demos.config;
//
//import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.AsyncConfigurer;
//import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//
//import java.util.concurrent.Executor;
//import java.util.concurrent.RejectedExecutionHandler;
//import java.util.concurrent.ThreadPoolExecutor;
//
//@Configuration
//@ComponentScan("com.example.spribootthreadpool.demos.config")
//@EnableAsync  //开启异步操作
//public class TaskExecutorConfig implements AsyncConfigurer {
//
//    /**
//     * 通过getAsyncExecutor方法配置ThreadPoolTaskExecutor,获得一个基于线程池TaskExecutor
//     *
//     * @return
//     */
//    @Override
//    public Executor getAsyncExecutor() {
//        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
//        pool.setCorePoolSize(5);//核心线程数
//        pool.setMaxPoolSize(10);//最大线程数
//        pool.setQueueCapacity(25);//线程队列
//        pool.setKeepAliveSeconds(10);
//        pool.setPrestartAllCoreThreads(true);
//        pool.setRejectedExecutionHandler(ThreadPoolExecutor.AbortPolicy);
//        pool.initialize();//线程初始化
//        return pool;
//    }
//
//    @Override
//    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
//        return null;
//    }
//}
