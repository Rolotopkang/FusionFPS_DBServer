package com.topgaming.fusion_server;

import com.topgaming.fusion_server.netty.BootNettyServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.annotation.Async;

@SpringBootApplication
public class FusionServerApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(FusionServerApplication.class, args);
        System.out.println("服务器初始化开始");
    }

    @Async
    @Override
    public void run(String... args) throws  Exception{
        new BootNettyServer().bind(16888);
    }

}
