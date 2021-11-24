package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 查询list集合
     *
     * @return
     */
    List<User> queryUserList();

    void batchInsert(List<User> list);

    void updateUserById(User user);

    void updateUserByName(User user);

    User getById(@Param("id") String id);

    User getByUsername(@Param("username") String username);

    List<Map<String, Object>> getTableName(Map<String, Object> params);

    void optimizeTable(@Param("tableName") String tableName);

    List<Map<String, Object>> getDatabaseName(Map<String, Object> params);

    void addOne(@Param("user") User user);
}
