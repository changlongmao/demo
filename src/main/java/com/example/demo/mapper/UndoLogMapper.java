package com.example.demo.mapper;

import java.util.List;

import com.example.demo.entity.UndoLog;
import com.example.demo.entity.UndoLogCondition;
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
public interface UndoLogMapper {
    /**
     * 根据查询条件分页查询列表
     * 
     * @param page
     *            分页参数
     * @param condition
     *            查询参数
     * @return 分页数据
     */
    IPage<UndoLog> findUndoLogByCondition(IPage<UndoLog> page, @Param("condition") UndoLogCondition condition);

    /**
     * 根据主键ID查询信息
     *
     * @param id 主键ID
     * @return 信息
     */
    UndoLog getUndoLogById(@Param("id") Long id);

    List<UndoLog> findUndoLogAll();

    /**
     * 新增信息
     *
     * @param undoLog 信息
     * @return 是否成功
     */
    boolean addUndoLog(UndoLog undoLog);

    int addUndoLogs(List<UndoLog> list);

    /**
     * 批量修改信息
     *
     * @param list 信息
     * @return 是否成功
     */
    int updateUndoLogs(@Param("list") List<UndoLog> list);
    /**
     * 修改信息
     *
     * @param undoLog 信息
     * @return 是否成功
     */
    boolean updateUndoLog(UndoLog undoLog);

    /**
     * 根据主键ID删除
     *
     * @param id 主键ID
     * @return 是否成功
     */
    boolean deleteUndoLogById(@Param("id") Long id);

    /**
     * 根据主键ID列表批量删除
     *
     * @param idList 主键ID列表
     * @return 是否成功
     */
    boolean deleteUndoLogByIds(List<Long> idList);
}