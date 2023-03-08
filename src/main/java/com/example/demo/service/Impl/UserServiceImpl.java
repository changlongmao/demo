package com.example.demo.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.annotation.SysLog;
import com.example.demo.controller.TestThreadController;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    public static final ThreadLocal<Object> threadLocal = new ThreadLocal<>();

    @Override
    public List<User> selectList() {
        return baseMapper.queryUserList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchInsert(List<User> users) {
        baseMapper.batchInsert(users);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserById(User user) {
        System.out.println("service层" + this);
        baseMapper.updateUserById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserByName(User user) {
        baseMapper.updateUserByName(user);
//        updateUserById(user);
    }

    @Async
    @Override
    public void executeAsync() {
        System.out.println(this);
        log.info("start executeAsync");
        try{
            Thread.sleep(5000);
        }catch(Exception e){
            e.printStackTrace();
        }
        log.info("end executeAsync");
    }

    @Override
    public Object getThreadLocal() {
        log.info("service ThreadLocal: {}", threadLocal.get());
        System.out.println(this);
        return TestThreadController.threadLocal.get();
    }

    @Override
    @SysLog
    @Transactional(rollbackFor = Exception.class)
    public User selectById(String id) {
        return baseMapper.getById(id);
    }

    @Override
    public User getByUsername(String username) {
        return baseMapper.getByUsername(username);
    }

    @Override
    public List<Map<String, Object>> getTableName(Map<String, Object> params) {
        return baseMapper.getTableName(params);
    }

    @Override
    public List<Map<String, Object>> optimizeTable(String tableName) {
        baseMapper.optimizeTable(tableName);
        return null;
    }

    @Override
    public List<Map<String, Object>> getDatabaseName(Map<String, Object> params) {
        return baseMapper.getDatabaseName(params);
    }

    @Override
    public void addOne(User user) {
        baseMapper.addOne(user);
    }
}
