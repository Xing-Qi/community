package com.nowcoder.community.controller;

import com.nowcoder.community.entity.Message;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.mapper.MessageMapper;
import com.nowcoder.community.service.MessageService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import com.nowcoder.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import java.util.*;

/**
 * @author Oliver
 * @create 2022-12-09 10:33
 */
@Controller
public class MessageController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private UserService userService;
    @Autowired
    private SensitiveFilter sensitiveFilter;
    @GetMapping("/letter/list")
    public String getLetterList(Model model, Page page){
        User user = hostHolder.getUser();

        //分页信息
        page.setLimit(5);
        page.setRows(messageService.finConversationCount(user.getId()));
        //目标路径
        page.setPath("/letter/list");

        //会话列表
        List<Message> conversationList = messageService.findConversations(
                user.getId(), page.getOffset(), page.getLimit());
        //数据展示
        List<Map<String,Object>> conversations = new ArrayList<>();
        if(conversationList!= null){
            for (Message message : conversationList ){
                Map<String,Object> map = new HashMap<>();
                //会话
                map.put("conversation",message);
                //某个会话id包含的私信数量
                map.put("letterCount", messageService.findLetterCount(message.getConversationId()));
                //某个会话id包含的未读私信数量
                map.put("unreadCount",messageService.findUnreadCount(user.getId(),message.getConversationId()));
                //找到与当前用户相对应的用户
                int targetId = user.getId() == message.getFromId() ? message.getToId() : message.getFromId();
                map.put("target",userService.findUserById(targetId));
                //装入每一个会话
                conversations.add(map);
            }
        }
        model.addAttribute("conversations",conversations);
        //未读私信数量
        int letterUnreadCount = messageService.findUnreadCount(user.getId(),null);
        model.addAttribute("letterUnreadCount",letterUnreadCount);
        return "/site/letter";
    }

    @GetMapping("/letter/detail/{conversationId}")
    public String getLetters(Model model, Page page , @PathVariable("conversationId") String conversationId){
        //分页
        page.setLimit(5);
        page.setRows(messageService.findLetterCount(conversationId));
        page.setPath("/letter/detail/" + conversationId);
        //私信列表
        List<Message> letterList = messageService.findLetters(conversationId, page.getOffset(), page.getLimit());
        //数据展示
        List<Map<String,Object>> letters = new ArrayList<>();
        if(letterList != null){
            for (Message letter : letterList ){
                Map<String,Object> map = new HashMap<>();
                map.put("letter",letter);
                map.put("fromUser",userService.findUserById(letter.getFromId()));
                letters.add(map);
            }
        }
        model.addAttribute("letters",letters);
        model.addAttribute("targetUser",getLetterUser(conversationId));

        //设置已读
        List<Integer> ids = getLetterIds(letterList);
        if(!ids.isEmpty()){
            messageService.updateMessageStatus(ids,1);
        }
        return "/site/letter-detail";

    }

    //查找未读的id
    private List<Integer> getLetterIds(List<Message> letterList){
        List<Integer> ids = new ArrayList<>();
        if(letterList != null){
            for (Message message : letterList){
                if(hostHolder.getUser().getId() == message.getToId() && message.getStatus() == 0){
                    ids.add(message.getId());
                }
            }
        }
        return ids;
    }

    //获取目标用户
    public User getLetterUser(String conversationId){
        String[] ids = conversationId.split("_");
        int id0 = Integer.parseInt(ids[0]);
        int id1 = Integer.parseInt(ids[1]);

        if(hostHolder.getUser().getId() == id0){
            return userService.findUserById(id1);
        }else
            return userService.findUserById(id0);
    }

    @PostMapping("/letter/send")
    @ResponseBody
    public String sendMessage(String content,String toName){

        User toUser = userService.selectUserByUsername(toName);
        if(toUser == null){
            CommunityUtil.getJsonString(1,"目标用户不存在!");
        }
         content = HtmlUtils.htmlEscape(content);
         content = sensitiveFilter.filter(content);
        Message message = new Message();
        message.setContent(content);
        message.setFromId(hostHolder.getUser().getId());
        message.setToId(toUser.getId());
        message.setCreateTime(new Date());
        message.setStatus(0);
        //设置conversationId
        if(message.getFromId() > message.getToId()){
            message.setConversationId(message.getToId() + "_" + message.getFromId());
        }else {
            message.setConversationId(message.getFromId() + "_" + message.getToId());
        }
        messageService.insetMessage(message);
        return CommunityUtil.getJsonString(0);
    }
}
