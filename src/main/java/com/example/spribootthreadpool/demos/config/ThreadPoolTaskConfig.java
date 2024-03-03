package com.example.spribootthreadpool.demos.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
//@EnableAsync
public class ThreadPoolTaskConfig {
    /**
     * corePoolSize：最小线程数，默认为 1。
     * maxPoolSize：最大线程数，默认为 Integer.MAX_VALUE。
     * keepAliveSeconds：（maxPoolSize-corePoolSzie）部分线程空闲最大存活时间，默认存活时间是 60s。
     * queueCapacity：阻塞队列的大小，默认为 Integer.MAX_VALUE，默认使用 LinkedBlockingQueue。
     * allowCoreThreadTimeOut：是否允许核心线程过期，设置为 true 的话，keepAliveSeconds 参数设置的有效时间对 corePoolSize 线程也有效，默认是 false。
     * threadNamePrefix：线程名称前缀，为 ThreadPoolTaskExecutor 的增强功能，默认为“类名-”。
     * threadFactory：设置创建线程的工厂，可以通过线程工厂给每个创建出来的线程设置更有意义的名字。使用开源框架 guava 提供的 ThreadFactoryBuilder 可以快速给线程池里的线程设置有意义的名字。
     * rejectedExecutionHandler：拒绝策略，当队列 workQueue 和线程池 maxPoolSize 都满了，说明线程池处于饱和状态，那么必须采取一种策略处理提交的新任务。这个策略默认情况下是 AbortPolicy，表示无法处理新任务时抛出异常。
     * setWaitForTasksToCompleteOnShutdown：在调用shutdown后, 是否等待任务执行完毕后再关闭线程池(默认false)
     */
    // 核心线程数（默认线程数）
    private static final int CORE_POOL_SIZE = 5;
    // 最大线程数
    private static final int MAX_POOL_SIZE = 5;
    // 允许线程空闲时间（单位：默认为秒）
    private static final int KEEP_ALIVE_TIME = 10;
    // 缓冲队列大小
    private static final int QUEUE_CAPACITY = 20;
    // 线程池名前缀
    private static final String THREAD_NAME_PREFIX = "AsyncPool-";

    /**
     * protected BlockingQueue<Runnable> createQueue(int queueCapacity) {
     * 		if (queueCapacity > 0) {
     * 			return new LinkedBlockingQueue<>(queueCapacity);
     *                }
     * 		else {
     * 			return new SynchronousQueue<>();
     *        }* 	}
     *
     *        阻塞队列：默认SynchronousQueue, 如果设置容量则是LinkedBlockingQueue
     * @return
     */
    @Bean("taskExecutor") // bean的名称，默认为首字母小写的方法名
    public ThreadPoolTaskExecutor taskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setKeepAliveSeconds(KEEP_ALIVE_TIME);
        executor.setThreadNamePrefix(THREAD_NAME_PREFIX);
//        executor.setThreadGroupName("MyThreadPoolGroup");
        // 线程池对拒绝任务的处理策略
        // CallerRunsPolicy：由调用线程（提交任务的线程）处理该任务
        executor.setAllowCoreThreadTimeOut(true);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.setThreadNamePrefix("MySpringbootThreadPool-");
        // 在调用shutdown后, 是否等待任务执行完毕后再关闭线程池(默认false)
        executor.setWaitForTasksToCompleteOnShutdown(true);

        //加入装饰器
        executor.setTaskDecorator(new ContextCopyingDecorator());

        // 初始化
        executor.initialize();
        return executor;
    }


    @Bean("taskExecutor2") // bean的名称，默认为首字母小写的方法名
    public ThreadPoolTaskExecutor taskExecutor2(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setKeepAliveSeconds(KEEP_ALIVE_TIME);
        executor.setThreadNamePrefix(THREAD_NAME_PREFIX);
//        executor.setThreadGroupName("MyThreadPoolGroup");
        // 线程池对拒绝任务的处理策略
        // CallerRunsPolicy：由调用线程（提交任务的线程）处理该任务
        executor.setAllowCoreThreadTimeOut(true);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.setThreadNamePrefix("MySpringbootThreadPool2-");
        // 在调用shutdown后, 是否等待任务执行完毕后再关闭线程池(默认false)
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 初始化
        executor.initialize();
        return executor;
    }



    /**
     * 另一种定义线程池的方法:
     * 定义一个AsyncConfigurer类型的bean，实现getAsyncExecutor方法，返回自定义的线程池
     */
    @Bean
    public AsyncConfigurer asyncConfigurer(@Qualifier("taskExecutor2") Executor executor) {
        return new AsyncConfigurer() {
            @Nullable
            @Override
            public Executor getAsyncExecutor() {
                return executor;
            }
        };

    }

    /**
     * 在多线程的情况下，定义线程池
     *
     * volatile: s使得各个线程可见
     */
    private static volatile ThreadPoolTaskExecutor executor;
    /**
     * 线程池
     * @return
     */
    public static Executor getAsyncExecutor() {
        if (executor == null) {
            synchronized (executor) {
                if (executor == null) {
                    executor = new ThreadPoolTaskExecutor();
                    executor.setCorePoolSize(30);
                    executor.setMaxPoolSize(100);
                    executor.setQueueCapacity(1000);
                    executor.setThreadNamePrefix("AsyncThread-");
                    executor.initialize();
                }
            }
        }
        return executor;
    }

}
