package com.example.demo.entity;

import lombok.Data;

@Data
public class UserDemo extends User{

    private String id;
    private String username;

    public UserDemo() {

    }
}
