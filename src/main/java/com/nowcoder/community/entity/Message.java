package com.nowcoder.community.entity;

import com.mysql.cj.conf.PropertyDefinitions;
import lombok.Data;

import java.util.Date;

/**
 * @author Oliver
 * @create 2022-12-08 15:13
 */
@Data
public class Message {
    //私信id
    private int id;
    //发送者
    private int fromId;
    //接受者
    private int toId;
    //会话id
    private String conversationId;
    //消息内容
    private String content;
    //消息状态 0-未读;1-已读;2-删除;
    private int status;
    //创建时间
    private Date createTime;
}
