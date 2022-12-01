package com.nowcoder.community.entity;

import lombok.Data;

import javax.lang.model.element.NestingKind;
import java.util.Date;

/**
 * @author Oliver
 * @create 2022-11-28 19:40
 */
@Data
public class LoginTicket {
    private int id;
    private int userId;
    private String ticket;
    private int status; //0有效,1无效
    private Date expired;
}
