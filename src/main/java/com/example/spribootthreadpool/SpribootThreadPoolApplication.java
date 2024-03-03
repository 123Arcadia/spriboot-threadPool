package com.example.spribootthreadpool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
@EnableAsync
@SpringBootApplication
public class SpribootThreadPoolApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpribootThreadPoolApplication.class, args);
    }

}
