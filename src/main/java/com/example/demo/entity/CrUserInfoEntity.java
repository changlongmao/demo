package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 顾问信息实体
 *
 * @author Antikvo
 * @date 2019-09-09 18:06:10
 */
@Data
@TableName("CR_USER_INFO")
public class CrUserInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * ID
     */
    @TableId
    private String id;
    /**
     * 姓名
     */
    private String name;
    /**
     * 用户类型
     */
    private Integer type;
    /**
     * 会员编号
     */
    private String memberNumber;
    /**
     * 密码
     */
    @JsonIgnore
    private String password;
    /**
     * 密码盐
     */
    @JsonIgnore
    private String salt;
    /**
     * 联系方式
     */
    private String phone;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 添加时间
     */
    @TableField(exist = false)
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 是否删除
     */
    @TableLogic
    @JsonIgnore
    private Integer isDelete;
    /**
     * 微信公众号id
     */
    private String openId;
    /**
     * 性别
     */
    private Integer sex;
    /**
     * 是否关注公众号
     */
    private Boolean subscribe;
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 微信昵称
     */
    private String nickname;
    /**
     * 推广者ID
     */
    private String parentId;
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 个人或企业
     */
    private Integer secondType;
}
