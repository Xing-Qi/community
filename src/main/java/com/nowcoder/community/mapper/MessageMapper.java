package com.nowcoder.community.mapper;

import com.nowcoder.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Oliver
 * @create 2022-12-08 15:18
 */
@Mapper
public interface MessageMapper {
    //查询当前用户的会话列表，针对每一个会话只返回一条最新的私信
    List<Message> selectConversations(int userId,int offset,int limit);
    //查询当前用户的会话数量
    int selectConversationCount(int userId);
    //查询某个会话所包含的私信列表
    List<Message> selectLetters(String conversationId,int offset,int limit);
    //查询某个会话所包含的私信数量
    int selectLetterCount(String conversationId);
    //查询未读私信的数量
    int selectLetterUnreadCount(int userId,String conversationId);
    //新增私信
    int insertMessage(Message message);
    //更新私信状态
    int updateMessageStatus(List<Integer> ids,int status);
}
