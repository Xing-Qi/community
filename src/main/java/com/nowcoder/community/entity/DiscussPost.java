package com.nowcoder.community.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * @author Oliver
 * @create 2022-11-23 16:45
 */
@Data
@Document(indexName = "discusspost",type = "_doc",shards = 6,replicas = 3)
public class DiscussPost {
    @Id //主键
    private int id;
    @Field(type = FieldType.Integer)
    private int userId;
    //analyzer = "ik_max_word" 分词最大化 searchAnalyzer = "ik_smart" 聪明分词法
    @Field(type = FieldType.Text,analyzer = "ik_max_word",searchAnalyzer = "ik_smart")
    private String title;
    @Field(type = FieldType.Text,analyzer = "ik_max_word",searchAnalyzer = "ik_smart")
    private String content;
    @Field(type = FieldType.Integer)
    //0-普通; 1-置顶;
    private int type;
    @Field(type = FieldType.Integer)
    //0-正常; 1-精华; 2-拉黑;
    private int status;
    @Field(type = FieldType.Date)
    private Date createTime;
    @Field(type = FieldType.Integer)
    private int commentCount;
    @Field(type = FieldType.Double)
    private double score;
}
