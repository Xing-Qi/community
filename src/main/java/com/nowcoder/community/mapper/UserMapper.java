package com.nowcoder.community.mapper;

import com.nowcoder.community.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author Oliver
 * @create 2022-11-23 10:56
 */
@Mapper
public interface UserMapper {
    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    User selectUserById(@Param("id") int id);

    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    @Select("select * from user where username = #{username}")
    User selectUserByName(@Param("username") String username);

    /**
     * 根据邮箱查询用户
     * @param email
     * @return
     */
    @Select("select * from user where email = #{email}")
    User selectUserByEmail(@Param("email") String email);

    /**
     * 插入用户
     * @param user
     * @return
     */
    int insertUser(User user);

    /**
     * 更新用户激活状态
     * @param userId
     * @param status
     * @return
     */
    int updateStatus(@Param("userId") int userId,@Param("status") int status);
    @Update({
            "update user set header_url = #{headerUrl} where id = #{userId}"
    })
    int upateHeader(@Param("userId") int userId,@Param("headerUrl") String headerUrl);
    @Update({
            "update user set password = #{password} where id = #{userId}"
    })
    int updateUserPassword(@Param("userId") int userId,@Param("password") String password);

}
