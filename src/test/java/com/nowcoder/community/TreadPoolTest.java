package com.nowcoder.community;

import com.nowcoder.community.service.AlphaService;
import com.nowcoder.community.service.impl.AlphaServiceImpl;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Date;
import java.util.concurrent.*;

/**
 * @author Oliver
 * @create 2023-02-08 10:45
 */
@SpringBootTest
@Slf4j
public class TreadPoolTest {
    //Jdk普通线程池
    private ExecutorService executorService = Executors.newFixedThreadPool(5);//初始化线程池数量
    //jdk执行定时任务的线程池
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);

    //spring普通线程池
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;
    //spring定时任务线程池
    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;
    @Autowired
    private AlphaServiceImpl alphaService;

    //线程睡眠方法
    public void sleep(long m) {
        try {
            Thread.sleep(m);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("jdk普通线程池执行任务")
    public void testExecutorService() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                log.debug("Hello ExecutorService!");
            }
        };
        for (int i = 0; i < 10; i++) {
            executorService.submit(task);
        }
        sleep(10000);
    }

    @Test
    @DisplayName("jdk定时线程池执行任务")
    public void testScheduleExecutorService() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                log.debug("Hello ScheduleExecutorService!");
            }
        };
        scheduledExecutorService.scheduleAtFixedRate(task, 10000, 1000, TimeUnit.MILLISECONDS);
        sleep(30000);
    }
    @Test
    @DisplayName("spring普通线程池")
    public void testThreadPoolTaskExecutor(){
        Runnable task = new Runnable() {
            @Override
            public void run() {
                log.debug("Hello,TreadPoolTaskExecutor!");
            }
        };
        for (int i = 0; i < 10; i++) {
            taskExecutor.execute(task);
        }
        sleep(10000);
    }
    @Test
    @DisplayName("spring定时任务线程池")
    public void testThreadPoolTaskSchedule(){
        Runnable task = new Runnable() {
            @Override
            public void run() {
                log.info("Hello,TreadPoolTaskSchedule!");
            }
        };
        Date startTime = new Date(System.currentTimeMillis() + 10000);
        taskScheduler.scheduleAtFixedRate(task,startTime,1000);
        sleep(30000);
    }
    /** 5.Spring普通线程池，简单版本 */
    @Test
    public void testThreadPoolTaskExecutorSimple() {
        for (int i = 0; i < 10; i++) {
            alphaService.execute1();
        }
        sleep(10000);
    }
    /** 6.Spring定时任务线程池(简化) */
    @Test
    public void testThreadPoolTaskSchedulerSimple() {
        sleep(30000);
    }

}
