package com.example.demo.jwt;


import lombok.Data;

/**
 * 认证的请求dto
 *
 * @author admin
 * @date 2017/8/24 14:00
 */
@Data
public class AuthUserInfo {

    private String openid;

    private String userId;

    private Integer userType;

}
