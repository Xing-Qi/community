package com.nowcoder.community.controller;

import com.nowcoder.community.service.AlphaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
}
