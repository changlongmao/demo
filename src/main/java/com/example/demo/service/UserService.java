package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService extends IService<User> {

    List<User> selectList();

    void batchInsert(List<User> users);

    void updateUserById(User user);

    void updateUserByName(User user);

    void executeAsync();

    Object getThreadLocal();

    User selectById(String id);

    List<Map<String, Object>> getTableName(Map<String, Object> params);

    List<Map<String, Object>> optimizeTable(String tableName);

    List<Map<String, Object>> getDatabaseName(Map<String, Object> params);

    void addOne(User user);
}
