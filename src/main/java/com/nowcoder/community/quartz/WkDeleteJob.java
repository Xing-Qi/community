package com.nowcoder.community.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;

/**
 * @author Oliver
 * @create 2023-02-11 21:00
 */
@Slf4j
public class WkDeleteJob implements Job {
    @Value("${wk.image.storage}")
    private String wkImageStorage;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //listFiles()方法的作用如果file是个文件，则返回的是null，
        // 如果file是空目录，返回的是空数组，
        // 如果file不是空目录，则返回的是该目录下的文件和目录
        File[] files = new File(wkImageStorage).listFiles();
        if (files == null || files.length == 0) {
            log.info("没有wk图片，任务取消");
            return;
        }
        for (File file : files) {
            //删除一分钟前创建的图片
            //LastModified()方法用一个长整型值来代表文件最后一次被修改的时间
            if (System.currentTimeMillis() - file.lastModified() > 60 * 1000) ;
            log.info("删除wk图片" + file.getName());
            file.delete();
        }
    }
}
