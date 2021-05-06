package com.example.demo.service.Impl;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.demo.entity.UndoLog;
import com.example.demo.entity.UndoLogCondition;
import com.example.demo.mapper.UndoLogMapper;
import com.example.demo.service.UndoLogService;
import com.example.demo.service.UserTestTimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service接口实现
 *
 * @author bianj
 * @version 1.0.0 2021-04-28
 */
@Service
public class UndoLogServiceImpl implements UndoLogService {
    private static final Logger logger = LoggerFactory.getLogger(UndoLogServiceImpl.class);

    @Autowired
    private UndoLogMapper UndoLogMapper;
//    private UserTestTimeService userTestTimeService;

//    public UndoLogServiceImpl(UserTestTimeService userTestTimeService) {
//        this.userTestTimeService = userTestTimeService;
//    }

    @Override
    public IPage<UndoLog> findUndoLogByCondition(UndoLogCondition condition) {
        IPage<UndoLog> page = condition.buildPage();
        return UndoLogMapper.findUndoLogByCondition(page, condition);
    }
    @Override
    public List<UndoLog> findUndoLogAll() {
        return UndoLogMapper.findUndoLogAll();
    }

    @Override
    public UndoLog getUndoLogById(Long id) {
        return UndoLogMapper.getUndoLogById(id);
    }

    @Transactional
    @Override
    public boolean addUndoLog(UndoLog undoLog) {
        return UndoLogMapper.addUndoLog(undoLog);
    }

    @Transactional
    @Override
    public int addUndoLogs(List<UndoLog> undoLogList) {
        return UndoLogMapper.addUndoLogs(undoLogList);
    }

    @Transactional
    @Override
    public boolean updateUndoLog(UndoLog undoLog) {
        return UndoLogMapper.updateUndoLog(undoLog);
    }

    @Transactional
    @Override
    public int updateUndoLogs(List<UndoLog> undoLogList) {
        return UndoLogMapper.updateUndoLogs(undoLogList);
    }

    @Transactional
    @Override
    public boolean deleteUndoLogById(Long id) {
        return UndoLogMapper.deleteUndoLogById(id);
    }

    @Transactional
    @Override
    public boolean deleteUndoLogByIds(List<Long> idList) {
        return UndoLogMapper.deleteUndoLogByIds(idList);
    }
}