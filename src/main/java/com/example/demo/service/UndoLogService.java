package com.example.demo.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.demo.entity.UndoLog;
import com.example.demo.entity.UndoLogCondition;

/**
 * Service接口
 *
 * @author bianj
 * @version 1.0.0 2021-04-28
 */
public interface UndoLogService {
    /**
     * 根据查询条件分页查询列表
     *
     * @param condition 查询条件
     * @return 分页信息
     */
    IPage<UndoLog> findUndoLogByCondition(UndoLogCondition condition);
    List<UndoLog> findUndoLogAll();

    /**
     * 根据主键ID查询信息
     *
     * @param id 主键ID
     * @return 信息
     */
    UndoLog getUndoLogById(Long id);

    /**
     * 新增信息
     *
     * @param undoLog 信息
     * @return 是否成功
     */
    boolean addUndoLog(UndoLog undoLog);

    int addUndoLogs(List<UndoLog> undoLog);

    /**
     * 修改信息
     *
     * @param undoLog 信息
     * @return 是否成功
     */
    boolean updateUndoLog(UndoLog undoLog);

    int updateUndoLogs(List<UndoLog> undoLogList);

    /**
     * 根据主键ID删除
     *
     * @param id 主键ID
     * @return 是否成功
     */
    boolean deleteUndoLogById(Long id);

    /**
     * 根据主键ID列表批量删除
     *
     * @param idList 主键ID列表
     * @return 是否成功
     */
    boolean deleteUndoLogByIds(List<Long> idList);
}