package com.example.spribootthreadpool.demos.Service;

import com.example.spribootthreadpool.demos.config.JokerAsync;
import com.example.spribootthreadpool.demos.config.ThreadPoolExceptionhandlerSecond;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.concurrent.*;

@Service
public class AsyncTaskService {

    @Resource(name = "taskExecutor")
    private ThreadPoolTaskExecutor taskExecutor;

    /**
     *
     * @param i
     */
    @Async("taskExecutor")
    public void executeAsync(Integer i) throws Exception{
        monitor("/executeAsync");
        System.out.println("线程ID：" + Thread.currentThread().getId() + "线程名字：" +Thread.currentThread().getName()+"执行异步任务:" + i);
    }

    @Async("taskExecutor")
    public CompletableFuture<String> executeAsyncFutrue(Integer i) throws Exception {
        System.out.println("线程ID：" + Thread.currentThread().getId() +"线程名字：" +Thread.currentThread().getName()+ "执行异步有返回的任务:" + i);
//        return new AsyncResult<>("success:"+i);

        /**
         * 此方法会返回一个布尔值。如果当前 Class 对象表示的类或接口可以接受 cls 参数所表示的类或接口的实例作为参数（即 cls 可以赋值给当前类），则返回 true，否则返回 false。
         */
//        if (CompletableFuture.class.isAssignableFrom(returnType)) {
//            //@Async注释的方法返回类型如果为CompletableFuture及其子类
//            //就使用线程池执行并封装成CompletableFuture返回
//            return CompletableFuture.supplyAsync(() -> {
//                try {
//                    return task.call();
//                }
//                catch (Throwable ex) {
//                    throw new CompletionException(ex);
//                }
//            }, executor);
//        }
        return CompletableFuture.completedFuture("hello World!");
    }

    @Async("taskExecutor")
    public void executeAsyncExHandler(Integer i) throws Exception{
        monitor("/executeAsyncExHandler");
        System.out.println("线程ID：" + Thread.currentThread().getId() + "线程名字：" +Thread.currentThread().getName()+"执行异步任务:" + i);
        if (true) {
            throw new RuntimeException("[async]");
        }
    }

    /**
     * 只能有一个类实现AsyncConfigurer
     * @param i
     */
//    @JokerAsync(value = "taskExecutor",exceptionHandler = "ThreadPoolExceptionhandler")
    @JokerAsync(value = "taskExecutor",exceptionHandler = "ThreadPoolExceptionhandlerFirst")
    public void executeAsyncExHandlerExtend(Integer i){
        monitor("/executeAsyncExHandler");
        System.out.println("线程ID：" + Thread.currentThread().getId() + "线程名字：" +Thread.currentThread().getName()+"执行异步任务:" + i);
//        try {
            if (true) {
                throw new RuntimeException("[ThreadPoolExceptionhandlerFirst]");
            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }



    /**
     * 监控线程池运行时的各项指标, 比如:任务等待数、任务异常信息、已完成任务数、核心线程数、最大线程数等<p>
     */
    private void monitor(String title) {
        try {
            // 线程池监控信息记录, 这里需要注意写ES的时机,尤其是多个子线程的日志合并到主流程的记录方式
//            String threadPoolMonitor = MessageFormat.format(
//                    "{0}{1}core pool size:{2}, current pool size:{3}, queue wait size:{4}, active count:{5}, completed task count:{6}, " +
//                            "task count:{7}, largest pool size:{8}, max pool size:{9}, keep alive time:{10}, is shutdown:{11}, is terminated:{12}, " +
//                            "thread name:{13}{14}",
//                    System.lineSeparator(), title, taskExecutor.getCorePoolSize(), taskExecutor.getPoolSize(),
//                    taskExecutor.getQueue().size(), taskExecutor.getActiveCount(), taskExecutor.getCompletedTaskCount(), taskExecutor.getTaskCount(), taskExecutor.getLargestPoolSize(),
//                    taskExecutor.getMaximumPoolSize(), taskExecutor.getKeepAliveTime(TimeUnit.SECONDS), taskExecutor.isShutdown(),
//                    taskExecutor.isTerminated(), Thread.currentThread().getName(), System.lineSeparator());
//            System.out.println("threadPoolMonitor = " + threadPoolMonitor);
            StringBuilder sb = new StringBuilder();
            sb.append(title).append("->");
            sb.append(taskExecutor).append(", ActiveCount:").append(taskExecutor.getActiveCount()).append(" ,QueueSize:").append(taskExecutor.getQueueSize()).append(", QueueCapacity:")
                    .append(taskExecutor.getQueueCapacity()).append(", ThreadGroup:").append(taskExecutor.getThreadGroup())
                    .append(", KeepAliveSeconds:").append(taskExecutor.getKeepAliveSeconds())
                    .append(", CorePoolSize:").append(taskExecutor.getCorePoolSize())
                    .append(", PoolSize:").append(taskExecutor.getPoolSize()); // 当前线程池中的线程数量

            System.out.println("[monitor]: " + sb);
        } catch (Exception e) {
            System.err.println("ThreadPool monitor error: "+ e);
        }
    }


    /**
     * 定义第二种线程池
     */
    @Async("taskExecutor2")
    public void async2ThreadPool(Integer i) throws Exception{
        monitor("/async2ThreadPool");
        System.out.println("线程ID：" + Thread.currentThread().getId() + "线程名字：" +Thread.currentThread().getName()+"执行异步任务:" + i);
    }

}
