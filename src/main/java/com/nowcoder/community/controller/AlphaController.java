package com.nowcoder.community.controller;

import com.nowcoder.community.service.AlphaService;
import com.nowcoder.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Oliver
 * @create 2022-11-20 15:21
 */
@Controller
@RequestMapping("/alpha")
public class AlphaController {
    @Autowired
    private AlphaService alphaService;

    @GetMapping("/hello")
    @ResponseBody
    public String sayHello() {
        return "Hello SpringBoot!";
    }

    @ResponseBody
    @GetMapping("/data")
    public String getData() {
        return alphaService.select();
    }

    //student?current=1&limit=20
    @ResponseBody
    @RequestMapping("/student")
    public String geStudents(
            @RequestParam(name = "current", required = false, defaultValue = "1") int current,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit
    ) {
        System.out.println("current:" + current + " limit:" + limit);
        return "students";
    }

    //student/id
    @ResponseBody
    @RequestMapping(path = "/student/{id}" ,method = RequestMethod.GET)
    public String getStudent(@PathVariable("id") int id){
        System.out.println(id);
        return "student";
    }

    //响应html数据
    //modelandview
    @GetMapping("/teacher")
    public ModelAndView getTeacher(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("name","张三");
        modelAndView.addObject("age","20");
        modelAndView.setViewName("/demo/view");
        return modelAndView;
    }

    //model
    @GetMapping("/school")
    public String getSchool(Model model){
        model.addAttribute("name","TiHua");
        model.addAttribute("age","88");
        return "/demo/view";
    }

    //响应json
    @GetMapping("/emp")
    @ResponseBody
    public Map<String,Object> getEmp(){
        Map<String, Object> emp = new HashMap<>();
        emp.put("name","张三");
        emp.put("age","30");
        return emp;
    }

    //setcookie
    @GetMapping("/cookie/set")
    @ResponseBody
    public String setCookie(HttpServletResponse response){
        //创建cookie对象
        Cookie cookie = new Cookie("code", CommunityUtil.generateUUID());
        //设置作用范围
        cookie.setPath("/community/alpha");
        //设置有效时间
        cookie.setMaxAge(60*10);//秒
        //添加到响应体中
        response.addCookie(cookie);
        return "set Cookie";
    }

    @GetMapping("/cookie/get")
    @ResponseBody
    public String getCookie(){
        return "get cookie";
    }

    //session
    @GetMapping("/session/set")
    @ResponseBody
    public String setSession(HttpSession session){
        session.setAttribute("id",1);
        session.setAttribute("name","Test");
        return "set Session";
    }

    @GetMapping("/session/get")
    @ResponseBody
    public String getSession(HttpSession session){
        System.out.println(session.getAttribute("id"));
        System.out.println(session.getAttribute("name"));
        return "get Session";
    }
}
