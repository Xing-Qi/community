package com.nowcoder.community.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Oliver
 * @create 2023-02-08 11:20
 */
@Configuration
@EnableScheduling // 启用线程
@EnableAsync //开启异步调用
public class ThreadPoolConfig {

}
