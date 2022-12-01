package com.nowcoder.community.controller.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Oliver
 * @create 2022-11-29 11:45
 */
@Component
@Slf4j
public class AlphaInterceptor implements HandlerInterceptor {

    @Override
    //controller执行之前
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("preHandler:{}",handler);
        return true;
    }

    @Override
    //controller执行之后
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.debug("postHandler:{}",handler);
    }

    @Override
    //模板引擎加载之后
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.debug("completionHandler:{}",handler);
    }
}
