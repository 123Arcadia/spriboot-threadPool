package com.example.spribootthreadpool.demos.config;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Map;

/**
 * 如oauth2的认证信息，日志TracerId都是在请求线程中的，如果内部使用多线程处理就存在获取不到认证信息或TraceId的问题。这时候就需要处理子线程与主线程间数据传递的问题。
 *
 * https://blog.csdn.net/qq_29569183/article/details/111311632
 */
public class ContextCopyingDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
        try {
            /**
             * 存在问题:
             * 从父线程取出的RequestContextHolder对象，此为持有线程上下文的request容器，将其设置到子线程中，按道理只要对象还存在强引用，就不会被销毁，
             * 但由于RequestContextHolder的特殊性，在父线程销毁的时候，
             * 会触发里面的resetRequestAttributes方法（即清除threadLocal里面的信息，即reques中的信息会被清除），此时即使RequestContextHolder这个对象还是存在，
             * 子线程也无法继续使用它获取request中的数据了。这也是网上很多文章讲TaskDecorator时没提到的点，真正用起来会发现有时可以有时不行，这个就取决于父子线程哪个先结束了。
             */
            RequestAttributes context = RequestContextHolder.currentRequestAttributes();  //1
            Map<String,String> previous = MDC.getCopyOfContextMap(); 					  //2
            SecurityContext securityContext = SecurityContextHolder.getContext();	      //3
            return () -> {
                try {
                    // 将主线程的请求信息，设置到子线程中
                    RequestContextHolder.setRequestAttributes(context);	 //1
                    MDC.setContextMap(previous);					     //2
                    SecurityContextHolder.setContext(securityContext);   //3
                    runnable.run();
                } finally {
                    RequestContextHolder.resetRequestAttributes();		// 1
                    MDC.clear(); 										// 2
                    SecurityContextHolder.clearContext();				// 3
                }
            };
        } catch (IllegalStateException e) {
            return runnable;
        }
    }
}
