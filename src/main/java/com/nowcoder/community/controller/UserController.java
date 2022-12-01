package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.mapper.UserMapper;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.logging.Logger;

/**
 * @author Oliver
 * @create 2022-11-30 22:03
 */
@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Value("${community.path.domain}")
    String domain;
    @Value("${server.servlet.context-path}")
    String contextPath;
    @Value("${community.path.upload}")
    String uploadPath;
    @Autowired
    private HostHolder hostHolder;
    @LoginRequired
    @GetMapping("/setting")
    public String getSettingPage() {
        return "/site/setting";
    }
    @LoginRequired
    @PostMapping("/upload")
    public String uploadHeader(MultipartFile headerImg, Model model) {
        if (headerImg == null) {
            model.addAttribute("error", "您还没有选择图片!");
            return "/site/setting";
        }
        //获取原始文件名
        String fileName = headerImg.getOriginalFilename();
        //获取文件类型
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "文件格式不正确！");
            return "/site/setting";
        }
        //生成随机文件名
        fileName = CommunityUtil.generateUUID() + suffix;
        //确定文件存放路径
        File dest = new File(uploadPath + "/" + fileName);
        try {
            headerImg.transferTo(dest);
        } catch (IOException e) {
            log.error("上传文件失败{}", e.getMessage());
            throw new RuntimeException("上传文件失败，服务器发生异常！", e);
        }
        //更新当前用户头像的路径
        //http://localhost/community/user/header/xxx.png
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + fileName;
        userService.updateHeader(user.getId(), headerUrl);

        return "redirect:/index";
    }

    @GetMapping("/header/{fileName}")
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        //服务器存放路径
        fileName = uploadPath + "/" + fileName;
        //文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        //响应图片
        response.setContentType("image/" + suffix);
        try (
                FileInputStream fis = new FileInputStream(fileName);
                OutputStream os = response.getOutputStream();
        ) {
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1){
               os.write(buffer,0,b);
            }
        } catch (IOException e) {
            log.error("读取头像失败：" + e.getMessage());
        }
    }

    @PostMapping("/updatePassword")
    public String updatePassWord(String oldPassword, String newPassword,Model model){
        //获取本次持有对象
        User user = hostHolder.getUser();
        //对输入的密码进行MD5加密
        oldPassword = CommunityUtil.md5(oldPassword + user.getSalt());
        //输入的密码有误或者为空
        if(oldPassword == null || !oldPassword.equals(user.getPassword())){
            model.addAttribute("passwordError","密码输入错误");
            return "/site/setting";
        }
        //密码正确
        newPassword = CommunityUtil.md5(newPassword + user.getSalt());
        userService.updateUserPassword(user.getId(),newPassword);
        //退出登录,重定向到登录页面
        return "redirect:/logout";
    }
}
