package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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

    User getById(String id);
}
