package cn.plutowu.entity;

import lombok.Data;

import java.util.Date;

/**
 * 用户
 *
 * @author PlutoWu
 * @date 2021/05/01
 */
@Data
public class User {

    private Long id;
    private String nickname;
    private String password;
    private String salt;
    private String head;
    private Date registerDate;
    private Date lastLoginDate;
    private Integer loginCount;

}