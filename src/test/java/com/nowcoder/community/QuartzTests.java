package com.nowcoder.community;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Oliver
 * @create 2023-02-08 12:15
 */
@SpringBootTest
public class QuartzTests {
    @Autowired
    private Scheduler scheduler;
    @Test
    @DisplayName("删除线程任务")
    public void testDeleteJob() throws SchedulerException {
        boolean b = scheduler.deleteJob(new JobKey("alphaJob", "alphaJobGroup"));
        System.out.println(b);
    }
}
