package com.example.demo.entity;

import lombok.Data;
import java.util.Date;

/**
 * user
 * 
 * @author bianj
 * @version 1.0.0 2021-04-28
 */
@Data
public class User implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = -921063982228618853L;

    /* This code was generated by TableGo tools, mark 1 begin. */

    /** id */
    private String id;

    /** username */
    private String username;

    /** rearName */
    private String rearName;

    /** password */
    private String password;

    /** createTime */
    private Date createTime;

    /** isDelete */
    private Integer isDelete;

    public User() {
        System.out.println(this);
    }

    public User(String id) {
        this.id = id;
    }

}