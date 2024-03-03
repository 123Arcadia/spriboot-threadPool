/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.spribootthreadpool.demos.web;

import com.example.spribootthreadpool.demos.Service.AsyncTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
@Controller
public class BasicController {

    @Autowired
    private AsyncTaskService asyncTaskService;

    // http://127.0.0.1:8080/hello?name=lisi
    @RequestMapping(value = "/async", method = RequestMethod.POST)
    @ResponseBody
    public String hello() throws Exception {
        asyncTaskService.executeAsync(10);
        return "Hello aync!";
        //[monitor]: org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor@764a9912, ActiveCount:1 ,QueueSize:0, QueueCapacity:20, ThreadGroup:java.lang.ThreadGroup[name=MyThreadPoolGroup,maxpri=10]
        //线程ID：55线程名字：AsyncPool-1执行异步任务:10
    }

    // http://127.0.0.1:8080/user
    @RequestMapping("/asyncFuture")
    @ResponseBody
    public void asyncFuture() throws Exception {
        CompletableFuture<String> asyncFutrue = asyncTaskService.executeAsyncFutrue(10);
        asyncFutrue.thenAccept(System.out::println);
    }
    @RequestMapping("/asyncEx")
    @ResponseBody
    public void asyncEx()  throws Exception {
        asyncTaskService.executeAsyncExHandler(98);

    }


    /**
     * 扩展@joker指定线程池和异常处理器
     * @throws Exception
     */
    @RequestMapping("/asyncExExtend")
    @ResponseBody
    public void asyncExExtend(){
        asyncTaskService.executeAsyncExHandlerExtend(90);

    }


    /**
     *
     */
    @RequestMapping("/async2ThreadPool")
    @ResponseBody
    public void async2ThreadPool() throws Exception {
        asyncTaskService.async2ThreadPool(90);
    }
}
