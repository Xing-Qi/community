package com.nowcoder.community.controller;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.community.entity.Message;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.MessageService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
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
public class MessageController  implements CommunityConstant {
    @Autowired
    private MessageService messageService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private UserService userService;
    @Autowired
    private SensitiveFilter sensitiveFilter;
    //私信列表
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
                map.put("unreadCount",messageService.findLetterUnreadCount(user.getId(),message.getConversationId()));
                //找到与当前用户相对应的用户
                int targetId = user.getId() == message.getFromId() ? message.getToId() : message.getFromId();
                map.put("target",userService.findUserById(targetId));
                //装入每一个会话
                conversations.add(map);
            }
        }
        model.addAttribute("conversations",conversations);
        //未读私信数量
        int letterUnreadCount = messageService.findLetterUnreadCount(user.getId(),null);
        model.addAttribute("letterUnreadCount",letterUnreadCount);
        //未读通知数量
        int noticeUnreadCount = messageService.findNoticeUnreadCount(user.getId(), null);
        model.addAttribute("noticeUnreadCount",noticeUnreadCount);


        return "/site/letter";
    }

    @GetMapping("/letter/detail/{conversationId}")
    public String getLetters(Model model, Page page , @PathVariable("conversationId") String conversationId){
        //分页
        page.setLimit(5);
        page.setRows(messageService.findLetterCount(conversationId));
        //设置分页路径
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

    //发送私信
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
        messageService.insertMessage(message);
        return CommunityUtil.getJsonString(0);
    }

    //获取系统通知
    @GetMapping("/notice/list")
    public String getNoticeList(Model model){
        //获取当前用户
        User user = hostHolder.getUser();


        //查询评论类通知
        Message message = messageService.findLaterNotice(user.getId(), TOPIC_COMMENT);
        //定义展示对象

        if(message != null){
            Map<String,Object> messageVO = new HashMap<>();
            messageVO.put("message",message);
            //把数据库中的数据转义过来
            String content = HtmlUtils.htmlUnescape(message.getContent());
            //把json数据转换成java对象
            Map<String,Object> data = JSONObject.parseObject(content, HashMap.class);
            //触发事件用户
            messageVO.put("user",userService.findUserById((Integer) data.get("userId")));
            //目标实体类型
            messageVO.put("entityType",data.get("entityType"));
            //目标实体id
            messageVO.put("entityId",data.get("entityId"));
            //帖子id
            messageVO.put("postId",data.get("postId"));

            //获取评论通知数量
            int count = messageService.findNoticeCount(user.getId(), TOPIC_COMMENT);
            messageVO.put("count",count);
            //获取未读数量
            int unread = messageService.findNoticeUnreadCount(user.getId(), TOPIC_COMMENT);
            messageVO.put("unread",unread);
            model.addAttribute("commentNotice",messageVO);
        }



        //查询点赞类通知
        message = messageService.findLaterNotice(user.getId(),TOPIC_LIKE);
        if(message != null){
            Map<String,Object> messageVO = new HashMap<>();
            messageVO.put("message",message);
            String content = HtmlUtils.htmlUnescape(message.getContent());
            Map<String,Object> data = JSONObject.parseObject(content, HashMap.class);
            messageVO.put("user",userService.findUserById((Integer) data.get("userId")));
            messageVO.put("entityType",data.get("entityType"));
            messageVO.put("entityId",data.get("entityId"));
            messageVO.put("postId",data.get("postId"));

            int count = messageService.findNoticeCount(user.getId(),TOPIC_LIKE);
            messageVO.put("count",count);

            int unread = messageService.findNoticeUnreadCount(user.getId(),TOPIC_LIKE);
            messageVO.put("unread",unread);
            model.addAttribute("likeNotice",messageVO);
        }



        //查询关注类通知
        message = messageService.findLaterNotice(user.getId(),TOPIC_FOLLOW);
        if(message != null){
            Map<String,Object> messageVO = new HashMap<>();
            messageVO.put("message",message);
            String content = HtmlUtils.htmlUnescape(message.getContent());
            Map<String,Object> data = JSONObject.parseObject(content, HashMap.class);
            messageVO.put("user",userService.findUserById((Integer) data.get("userId")));
            messageVO.put("entityType",data.get("entityType"));
            messageVO.put("entityId",data.get("entityId"));

            int count = messageService.findNoticeCount(user.getId(),TOPIC_FOLLOW);
            messageVO.put("count",count);

            int unread = messageService.findNoticeUnreadCount(user.getId(),TOPIC_FOLLOW);
            messageVO.put("unread",unread);
            model.addAttribute("followNotice",messageVO);
        }

        //查询未读消息数量
        int letterUnreadCount = messageService.findLetterUnreadCount(user.getId(), null);
        model.addAttribute("letterUnreadCount",letterUnreadCount);
        int noticeUnreadCount = messageService.findNoticeUnreadCount(user.getId(), null);
        model.addAttribute("noticeUnreadCount",noticeUnreadCount);
        return "/site/notice";
    }

    //系统通知详情
    @GetMapping("/notice/detail/{topic}")
    public String findNoticesPage(@PathVariable("topic") String topic,Page page,Model model){
        User user = hostHolder.getUser();
        //设置分页数据

        page.setLimit(5);
        page.setRows(messageService.findNoticeCount(user.getId(),topic));
        page.setPath("/notice/detail/" + topic);

        //封装数据
        List<Message> noticeList = messageService.findNotices(user.getId(), topic, page.getOffset(), page.getLimit());
        List<Map<String,Object>> noticeVoList = new ArrayList<>();
        if(noticeList != null){
            for(Message notice : noticeList){
                Map<String,Object> map  = new HashMap<>();
                //通知
                map.put("notice",notice);
                //内容
                String content = HtmlUtils.htmlUnescape(notice.getContent());
                //转成java对象
                Map<String,Object> data = JSONObject.parseObject(content, HashMap.class);

                map.put("user",userService.findUserById((Integer) data.get("userId")));
                map.put("entityType",data.get("entityType"));
                map.put("entityId",data.get("entityId"));
                map.put("postId",data.get("postId"));
                //通知作者
                map.put("fromUser",userService.findUserById(notice.getFromId()));
                //临时解决user为空问题
                if(userService.findUserById((Integer) data.get("userId"))!=null){
                    noticeVoList.add(map);
                }
            }
            model.addAttribute("notices",noticeVoList);
            //设置已读
            List<Integer> ids = getLetterIds(noticeList);
            if(!ids.isEmpty()){
                messageService.updateMessageStatus(ids,1);
            }
        }
        return "/site/notice-detail";
    }
}
