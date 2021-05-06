package com.example.demo.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.demo.entity.UserTestTime;
import com.example.demo.entity.UserTestTimeCondition;

/**
 * Service接口
 *
 * @author bianj
 * @version 1.0.0 2021-04-28
 */
public interface UserTestTimeService {
    /**
     * 根据查询条件分页查询列表
     *
     * @param condition 查询条件
     * @return 分页信息
     */
    IPage<UserTestTime> findUserTestTimeByCondition(UserTestTimeCondition condition);

    /**
     * 根据主键ID查询信息
     *
     * @param id 主键ID
     * @return 信息
     */
    UserTestTime getUserTestTimeById(Long id);

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
    boolean deleteUserTestTimeById(Long id);

    /**
     * 根据主键ID列表批量删除
     *
     * @param idList 主键ID列表
     * @return 是否成功
     */
    boolean deleteUserTestTimeByIds(List<Long> idList);
}