package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class User implements Serializable {

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

    static {
        System.out.println("静态代码块被调用");
    }

    @Data
    class LongFei {
        private String id;
    }

    public void testSynchronized() {
        synchronized (User.class) {
            System.out.println("测试synchronized成功");
        }
    }


}
