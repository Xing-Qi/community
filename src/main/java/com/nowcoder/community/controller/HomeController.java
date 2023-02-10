package com.nowcoder.community.controller;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.service.impl.DiscussPostServiceImpl;
import com.nowcoder.community.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Oliver
 * @create 2022-11-25 10:22
 */
@Controller
public class HomeController implements CommunityConstant {
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private UserService userService;
    @Autowired
    private LikeService likeService;

    @GetMapping(value = {"/index","/"})
    public String getIndex(Model model, Page page, @RequestParam(value = "orderMode",defaultValue = "0") int orderMode) {
        //方法调用栈，SpringMVC会自动实例化model和page，并将page注入到model，thymeleaf中可以直接访问page对象中的数据
        //查询全部数据
        List<DiscussPost> list = discussPostService.findDiscussPost(0, page.getOffset(), page.getLimit(),orderMode);
        //获取总条数
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index?orderMode=" + orderMode);
        //拼接用户数据
        List<Map<String,Object>> discussPosts = new ArrayList<>();
        //遍历拼接数据
        if(list !=null){
            for (DiscussPost post: list) {
                Map<String,Object> map = new HashMap<>();
                map.put("post",post);
                User user = userService.findUserById(post.getUserId());
                map.put("user",user);
                //查询点赞数量
                long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId());
                map.put("likeCount",likeCount);
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts",discussPosts);
        model.addAttribute("orderMode",orderMode);
        return "index";
    }

    @GetMapping("/error")
    public String getErrorPage(){
        return "error/5xx";
    }

    //拒绝访问时的提示页面
    @GetMapping("/denied")
    public String getDeniedPage(){
        return "/error/404";
    }

}
