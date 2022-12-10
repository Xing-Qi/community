package com.nowcoder.community.service;

import com.nowcoder.community.entity.Message;
import com.nowcoder.community.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Oliver
 * @create 2022-12-08 22:36
 */
@Service
public class MessageService {
    @Autowired
    private MessageMapper messageMapper;

    public List<Message> findConversations(int userId,int offset,int limit){
        return messageMapper.selectConversations(userId,offset,limit);
    }
    public int finConversationCount(int userId){
        return messageMapper.selectConversationCount(userId);
    }
    public List<Message> findLetters(String conversationId,int offset,int limit){
        return messageMapper.selectLetters(conversationId,offset,limit);
    }
    public int findLetterCount(String conversationId){
        return messageMapper.selectLetterCount(conversationId);
    }
    public int findUnreadCount(int userId,String conversationId){
        return messageMapper.selectLetterUnreadCount(userId,conversationId);
    }
    public int insetMessage(Message message){
        return messageMapper.insertMessage(message);
    }
    public int updateMessageStatus(List<Integer> ids ,int status){
        return messageMapper.updateMessageStatus(ids,status);
    }
}
