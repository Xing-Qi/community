package com.nowcoder.community.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author Oliver
 * @create 2022-11-23 16:45
 */
@Data
public class DiscussPost {
    private int id;
    private int userId;
    private String title;
    private String content;
    private int type;
    private int status;
    private Date createTime;
    private int commentCount;
    private double score;
}
