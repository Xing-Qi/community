package com.nowcoder.community.controller;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.service.impl.DiscussPostServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Oliver
 * @create 2022-11-25 10:22
 */
@Controller
public class HomeController {
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private UserService userService;

    @GetMapping(value = {"/index","/"})
    public String getIndex(Model model, Page page) {
        //方法调用栈，SpringMVC会自动实例化model和page，并将page注入到model，thymeleaf中可以直接访问page对象中的数据
        //查询全部数据
        List<DiscussPost> list = discussPostService.findDiscussPost(0, page.getOffset(), page.getLimit());
        //获取总条数
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");
        //拼接用户数据
        List<Map<String,Object>> discussPosts = new ArrayList<>();
        //遍历拼接数据
        if(list !=null){
            for (DiscussPost post: list) {
                Map<String,Object> map = new HashMap<>();
                map.put("post",post);
                User user = userService.findUserById(post.getUserId());
                map.put("user",user);
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts",discussPosts);
        return "index";
    }

    @GetMapping("/error")
    public String getErrorPage(){
        return "error/5xx";
    }

}
