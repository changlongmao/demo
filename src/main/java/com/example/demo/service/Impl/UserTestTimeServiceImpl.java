package com.example.demo.service.Impl;

import java.util.List;

import com.example.demo.entity.UserTestTime;
import com.example.demo.entity.UserTestTimeCondition;
import com.example.demo.mapper.UserTestTimeMapper;
import com.example.demo.service.UndoLogService;
import com.example.demo.service.UserTestTimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * Service接口实现
 *
 * @author bianj
 * @version 1.0.0 2021-04-28
 */
@Service
@Transactional(readOnly = true)
public class UserTestTimeServiceImpl implements UserTestTimeService {
    private static final Logger logger = LoggerFactory.getLogger(UserTestTimeServiceImpl.class);

    @Autowired
    private UserTestTimeMapper UserTestTimeMapper;
//    private UndoLogService undoLogService;

//    public UserTestTimeServiceImpl(UndoLogService undoLogService) {
//        this.undoLogService = undoLogService;
//    }
    @Override
    public IPage<UserTestTime> findUserTestTimeByCondition(UserTestTimeCondition condition) {
        IPage<UserTestTime> page = condition.buildPage();
        return UserTestTimeMapper.findUserTestTimeByCondition(page, condition);
    }

    @Override
    public UserTestTime getUserTestTimeById(Long id) {
        return UserTestTimeMapper.getUserTestTimeById(id);
    }

    @Transactional
    @Override
    public boolean addUserTestTime(UserTestTime userTestTime) {
        return UserTestTimeMapper.addUserTestTime(userTestTime);
    }

    @Transactional
    @Override
    public boolean updateUserTestTime(UserTestTime userTestTime) {
        return UserTestTimeMapper.updateUserTestTime(userTestTime);
    }

    @Transactional
    @Override
    public boolean deleteUserTestTimeById(Long id) {
        return UserTestTimeMapper.deleteUserTestTimeById(id);
    }

    @Transactional
    @Override
    public boolean deleteUserTestTimeByIds(List<Long> idList) {
        return UserTestTimeMapper.deleteUserTestTimeByIds(idList);
    }
}