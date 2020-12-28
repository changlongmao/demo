package com.example.demo.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
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

    @Async("taskExecutor")
    @Override
    public void executeAsync() {
        log.info("start executeAsync");
        try{
            Thread.sleep(5000);
        }catch(Exception e){
            e.printStackTrace();
        }
        log.info("end executeAsync");
    }
}
