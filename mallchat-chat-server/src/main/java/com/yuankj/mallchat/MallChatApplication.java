package com.yuankj.mallchat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @author Ykj
 * @date 2023/10/8 0008/17:53
 * @apiNote
 */
@SpringBootApplication(scanBasePackages = {"com.yuankj.mallchat"})
@MapperScan({"com.yuankj.mallchat.**.mapper"})
@ServletComponentScan

public class MallChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(MallChatApplication.class,args);
    }
}
