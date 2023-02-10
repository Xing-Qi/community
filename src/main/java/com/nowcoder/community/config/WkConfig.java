package com.nowcoder.community.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * @author Oliver
 * @create 2023-02-10 21:06
 */
@Configuration
@Slf4j
public class WkConfig {

    @Value("${wk.image.storage}")
    private String wkImageStorage;

    /**
     * 创建wk目录
     */
    @PostConstruct
    public void init(){
        File file = new File(wkImageStorage);
        if(!file.exists()){
            file.mkdir();
            log.info("创建wk图片目录" + wkImageStorage);
        }
    }
}
