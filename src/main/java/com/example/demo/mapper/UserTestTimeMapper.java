package com.example.demo.mapper;

import java.util.List;

import com.example.demo.entity.UserTestTime;
import com.example.demo.entity.UserTestTimeCondition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * Mapper接口
 * 
 * @author bianj
 * @version 1.0.0 2021-04-28
 */
@Mapper
public interface UserTestTimeMapper {
    /**
     * 根据查询条件分页查询列表
     * 
     * @param page
     *            分页参数
     * @param condition
     *            查询参数
     * @return 分页数据
     */
    IPage<UserTestTime> findUserTestTimeByCondition(IPage<UserTestTime> page, @Param("condition") UserTestTimeCondition condition);

    /**
     * 根据主键ID查询信息
     *
     * @param id 主键ID
     * @return 信息
     */
    UserTestTime getUserTestTimeById(@Param("id") Long id);

    /**
     * 新增信息
     *
     * @param userTestTime 信息
     * @return 是否成功
     */
    boolean addUserTestTime(UserTestTime userTestTime);

    /**
     * 修改信息
     *
     * @param userTestTime 信息
     * @return 是否成功
     */
    boolean updateUserTestTime(UserTestTime userTestTime);

    /**
     * 根据主键ID删除
     *
     * @param id 主键ID
     * @return 是否成功
     */
    boolean deleteUserTestTimeById(@Param("id") Long id);

    /**
     * 根据主键ID列表批量删除
     *
     * @param idList 主键ID列表
     * @return 是否成功
     */
    boolean deleteUserTestTimeByIds(List<Long> idList);

    int updateUserTestTimes(List<UserTestTime> list);
}