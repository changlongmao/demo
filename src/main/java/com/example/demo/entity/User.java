package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

@Data
public class User implements UserInterface{

    private String id;
    private String username;
    /**
     * 真实姓名
     */
    private String rearName;
    private String password;
    private Date createTime;

    @TableLogic
    @JsonIgnore
    private Integer isDelete;

    public User() {
    }

    public User(String id) {
        this.id = id;
    }

    @Override
    public Object sort() {

        return "user";
    }


    @Data
    class LongFei {
        private String id;
    }

    public void testSynchronized() {
        synchronized (User.class) {

        }
    }


}
