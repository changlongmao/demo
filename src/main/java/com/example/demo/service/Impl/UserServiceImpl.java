package com.example.demo.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Override
    public List<User> selectList() {
        return baseMapper.queryUserList();
    }

    @Override
    public void batchInsert(List<User> users) {
        baseMapper.batchInsert(users);
    }

    @Override
    public void updateUserById(User user) {
        baseMapper.updateUserById(user);
    }

    @Override
    public void updateUserByName(User user) {
        baseMapper.updateUserByName(user);
    }
}
