package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
@TableName("SYS_USER")
public class SysUserEntity implements Serializable {
    private static final long serialVersionUID = 1L;


    @TableId
    private String userId;


    @NotNull(message = "性别不能为空")
    private Integer sex;


    @NotBlank(message = "真实姓名不能为空")
    private String realName;


    @NotBlank(message = "用户名不能为空")
    private String userName;


    @NotBlank(message = "所属机构不能为空")
    private String orgNo;


    private String password;


    private String salt;


    private String emailHost;


    @Email(message = "邮箱格式不正确")
    private String email;


    private String emailPw;


    private String mobile;


    private Integer status;


    @TableField(exist = false)
    private List<String> roleIdList;


    private String createUserId;


    private String createUserOrgNo;


    private Date createTime;
}
