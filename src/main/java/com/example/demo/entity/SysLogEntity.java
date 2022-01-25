package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;


@Data
@TableName("SYS_LOG")
public class SysLogEntity implements RedisKeyNameLock, Serializable {
    private static final long serialVersionUID = 1L;
    @TableId
    private String id;

    private String userName;

    private String operation;

    private String method;

    private String params;

    private Long time;

    private String ip;

    private Date createTime;

    @Override
    public String getRedisKeyName() {
        return this.id + this.userName;
    }

    public Boolean isUserIp(){return true;} ;

}
