package com.nowcoder.community;

import com.nowcoder.community.entity.Message;
import com.nowcoder.community.mapper.MessageMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Oliver
 * @create 2022-12-08 18:01
 */
@SpringBootTest
public class MessageMapperTests {
    @Autowired
    private MessageMapper messageMapper;
    @Test
    @DisplayName("查询通知数据")
    public void testFindNotices(){
        List<Message> like = messageMapper.selectNotices(149, "like", 0, 5);
        for (Message notice : like){
            System.out.println(notice);
        }
    }
    @Test
    @DisplayName("查询未读的通知数量")
    public void testSelectUnreadCount(){
        int count = messageMapper.selectNoticeUnreadCount(149,null);
        System.out.println(count);
    }
    @Test
    @DisplayName("查询某个主题所包含的通知数量")
    public void testSelectNoticeCount(){
        int count = messageMapper.selectNoticeCount(149, "comment");
        System.out.println(count);
    }
    @Test
    @DisplayName("查询相应主题的最新消息")
    public void testSelectLaterNotice(){
        Message message = messageMapper.selectLaterNotice(149, "comment");
        System.out.println(message);
    }
    @Test
    @DisplayName("更新私信状态")
    public void testUpdateMessageStatus(){
        List<Integer> ids = new ArrayList<>();
        ids.add(119);
        ids.add(120);
        messageMapper.updateMessageStatus(ids,0);

    }

    @Test
    @DisplayName("插入私信")
    public void testInsertMessage(){
        Message message = new Message();
        message.setContent("测试");
        message.setCreateTime(new Date());
        message.setFromId(111);
        message.setToId(112);
        message.setConversationId("111_222");
        message.setStatus(1);
        messageMapper.insertMessage(message);
    }

    @Test
    @DisplayName("查询某个会话未读私信的数量")
    public void testSelectLetterUnreadCount(){
        int count = messageMapper.selectLetterUnreadCount(131, "111_131");
        System.out.println(count);
    }
    @Test
    @DisplayName("查询某个会话所包含的私信数量")
    public void testSelectLetterCount(){
        messageMapper.selectLetterCount("111_112");
    }
    @Test
    @DisplayName("查询某个会话所包含的私信列表")
    public void testselectLetters(){
        List<Message> letters = messageMapper.selectLetters("111_112", 0,10);
        for(Message letter:letters){
            System.out.println(letter);
        }
    }
    @Test
    @DisplayName("返回用户最新私信数量")
    public void testSelectConversationCount(){
        int count = messageMapper.selectConversationCount(111);
        System.out.println(count);
    }
    @Test
    @DisplayName("返回用户最新私信")
    public void testSelectConversations(){
        List<Message> messages = messageMapper.selectConversations(111, 0, 20);
        for(Message message : messages){
            System.out.println(message);
        }
    }

}
