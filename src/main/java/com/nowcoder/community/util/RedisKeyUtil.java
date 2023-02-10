package com.nowcoder.community.util;

/**
 * @author Oliver
 * @create 2022-12-11 16:46
 */
public class RedisKeyUtil {
    //分割符
    private static  final  String SPLIT = ":";
    //点赞实体key的前缀
    private static final String PREFIX_ENTITY_LIKE = "like:entity";
    //获取点赞的用户key
    private static final String PREFIX_USER_LIKE = "like:user";
    //被关注的实体目标前缀
    private static final String PREFIX_FOLLOWEE = "followee";
    //关注的用户前缀
    private static final String PREFIX_FOLLOWER = "follower";
    //验证码前缀
    private static final String PREFIX_KAPTCHA = "kaptcha";
    //登录凭证前缀
    private static final String PREFIX_TICKET = "ticket";
    //用户数据前缀
    private static final String PREFIX_USER = "user";
    //UvKey
    private static final String PREFIX_UV = "uv";
    //DauKey
    private static final String PREFIX_DAU = "dau";
    //Post
    private static final String PREFix_POST = "post";

    /**
     * 某个实体的赞 like:entity:entityType:entityId->(set(userId))
     * @param entityType
     * @param entityId
     * @return
     */
    public static String getEntityLikeKey(int entityType,int entityId){
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    /**
     * 某个用户的赞
     * @param userId
     * @return
     */
    public static String getUserLikeKey(int userId){
        return PREFIX_USER_LIKE + SPLIT + userId;
    }

    /**
     * 某个用户关注的实体
     * followee:userId:entityType -> zset(entityId,now)
     * @param userId
     * @param entityType
     * @return
     */
    public static String getFolloweeKey(int userId,int entityType){
        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }
    //follower:entityType:entityId->zset(userId,now)

    /**
     * 某个实体拥有的粉丝
     * @param entityType
     * @param entityId
     * @return
     */
    public static String getFollowerKey(int entityType, int entityId){
        return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }

    /**
     *
     * @param KaptchaOwnerKey 用户临时凭证
     * @return
     */
    public static String getKaptchaKey(String KaptchaOwnerKey){
        return PREFIX_KAPTCHA + SPLIT + KaptchaOwnerKey;
    }

    public static String getTicketKey(String ticket){
        return PREFIX_TICKET + SPLIT + ticket;
    }
    public static String getUserKey(int userId){
        return PREFIX_USER + SPLIT + userId;
    }
    //单日uv
    public static String getUvKey(String date){
        return PREFIX_UV+ SPLIT + date;
    }
    //范围uv
    public static String getUvKey(String startDate,String endDate){
        return PREFIX_UV + SPLIT + startDate + SPLIT + endDate;
    }
    //单日dau
    public static String getDauKey(String date){
        return PREFIX_DAU+ SPLIT + date;
    }
    //范围dau
    public static String getDauKey(String startDate,String endDate){
        return PREFIX_DAU + SPLIT + startDate + SPLIT + endDate;
    }
    //帖子分数
    public static String getPostScoreKey(){
        return PREFix_POST + SPLIT + "score";
    }

}
