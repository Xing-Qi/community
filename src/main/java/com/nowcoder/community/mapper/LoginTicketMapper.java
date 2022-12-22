package com.nowcoder.community.mapper;

import com.nowcoder.community.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * @author Oliver
 * @create 2022-11-28 19:52
 */
@Mapper
@Deprecated
public interface LoginTicketMapper {
    /**
     * 插入登录凭证
     * @param loginTicket
     * @return
     */
    @Insert({
            "insert into login_ticket(user_id,ticket,status,expired) " ,
            "values (#{userId},#{ticket},#{status},#{expired})"
    })
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int insertLoginTicket(LoginTicket loginTicket);

    /**
     * 根据ticket查询用户登录凭证
     * @param ticket
     * @return
     */
    @Select({
            "select * from login_ticket where ticket = #{ticket}"
    })
    LoginTicket selectLoginTicketByTicket(@Param("ticket") String ticket);
    @Update({
            "update login_ticket set status = #{status} where ticket = #{ticket} "
    })
    int updateStatus(@Param("ticket") String ticket,@Param("status") int status);
}
