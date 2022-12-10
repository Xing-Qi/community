package com.nowcoder.community.controller.advice;

import com.nowcoder.community.util.CommunityUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * @author Oliver
 * @create 2022-12-09 22:25
 */
//用于修饰类，表示该类是Controller的全局配置类 异常处理方案、绑定数据方案、绑定参数方案
@ControllerAdvice(annotations = Controller.class)
@Slf4j
public class ExceptionAdvice {
    //用于修饰方法，该方法会在Controller出现异常后被调用，用于处理捕获到的异常
    @ExceptionHandler
    public void hadleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.error("服务器发生异常",e.getMessage());
        for(StackTraceElement element : e.getStackTrace()){
            log.error(element.toString());
        }
        //若请求不为html页面
        //处理异步请求
        String xRequestWith = request.getHeader("x-requested-with");
        String type = "XMLHttpRequest";
        if(type.equals(xRequestWith)){
            response.setContentType("application/plain;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(CommunityUtil.getJsonString(1,"服务器异常!"));
        }else {
            response.sendRedirect(request.getContextPath() + "/error");
        }

    }
}
