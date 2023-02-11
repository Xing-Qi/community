package com.nowcoder.community.controller;

import com.nowcoder.community.entity.Event;
import com.nowcoder.community.event.EventProducer;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Oliver
 * @create 2023-02-10 21:24
 */
@Controller
@Slf4j
public class ShareController implements CommunityConstant {
    @Autowired
    private EventProducer eventProducer;
    @Value("${community.path.domain}")
    private String domain;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Value("${wk.image.storage}")
    private String wkImageStorage;

    /**
     * 生成长图
     * @param htmlUrl
     * @return
     */
    @GetMapping("/share")
    @ResponseBody
    public String share(String htmlUrl){
        //文件名
        String fileName = CommunityUtil.generateUUID();
        //异步生成长图
        Event event = new Event()
                .setTopic(TOPIC_SHARE)
                .setData("htmlUrl",htmlUrl)
                .setData("fileName",fileName)
                .setData("suffix",".png");
        eventProducer.fireEvent(event);

        //返回访问路径
        Map<String,Object> map = new HashMap<>();
        map.put("shareUrl",domain+contextPath+"/share/image/" + fileName);
        return CommunityUtil.getJsonString(0,null,map);
    }

    /**
     * 废弃 改为云服务获取长图
     * @param fileName
     * @param response
     */
    @GetMapping("/share/image/{fileName}")
    public void getShareImage(
            @PathVariable("fileName") String fileName,
            HttpServletResponse response
    ){
        if(StringUtils.isBlank(fileName)){
            throw new IllegalArgumentException("文件名不能为空");
        }
        response.setContentType("image/png");
        File file = new File(wkImageStorage + "/" + fileName + ".png");
        try {
            OutputStream os = response.getOutputStream();
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];//一次读取1024字节
            int b = 0;//游标
            while ((b = fis.read(buffer))!= -1){
                os.write(buffer,0,b);
            }
        } catch (IOException e) {
           log.error("获取长图失败: "+e.getMessage());
        }
    }
}
