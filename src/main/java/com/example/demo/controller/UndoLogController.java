package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.common.RestResponse;
import com.example.demo.entity.Result;
import com.example.demo.entity.UndoLog;
import com.example.demo.entity.UndoLogCondition;
import com.example.demo.service.UndoLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * Controller
 * 
 * @author bianj
 * @version 1.0.0 2021-04-28
 */
@Slf4j
@Api(tags = "undo_log服务")
@RestController
@RequestMapping("/undoLog")
public class UndoLogController {
    @Autowired
    private UndoLogService undoLogService;

    @ApiOperation(value = "根据查询参数分页查询列表")
    @ApiImplicitParam(name = "condition", value = "查询条件", required = true, dataType = "UndoLogCondition", paramType = "body")
    @PostMapping("/page")
    public IPage<UndoLog> page(@RequestBody UndoLogCondition condition) {
        IPage<UndoLog> page = undoLogService.findUndoLogByCondition(condition);
        return page;
    }

    @ApiOperation(value = "查询列表")
    @PostMapping("/list")
    public List<UndoLog> list() {
        return undoLogService.findUndoLogAll();
    }

    @ApiOperation(value = "根据主键ID查询信息")
    @ApiImplicitParam(name = "id", value = "主键ID", required = true)
    @GetMapping(value = "/get/{id}")
    public Result<UndoLog> get(@PathVariable Long id) {
        if (id == null) {
            return Result.failed("请选择需要查询的数据！");
        }
        UndoLog undoLog = undoLogService.getUndoLogById(id);
        return Result.ok(undoLog);
    }

    @ApiOperation(value = "新增信息")
    @ApiImplicitParam(name = "undoLog", value = "", required = true, dataType = "UndoLog", paramType = "body")
    @PostMapping("/add")
    public RestResponse add(@RequestBody UndoLog undoLog) {
//        boolean bool = undoLogService.addUndoLog(undoLog);
//        if (bool) {
//            return Result.ok(undoLog);
//        }
        return RestResponse.success().put("aa", "bb");
    }

    @ApiOperation(value = "新增信息")
    @ApiImplicitParam(name = "undoLog", value = "", required = true, dataType = "UndoLog", paramType = "body")
    @PostMapping("/addList")
    public Result<?> addList() {
        long start = System.currentTimeMillis();
//        IPage<UndoLog> page = undoLogService.findUndoLogByCondition(new UndoLogCondition());
        List<UndoLog> records = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            UndoLog undoLog = new UndoLog();
            undoLog.setContext(i + "1");
            undoLog.setBranchId((long) i);
            undoLog.setExt(i + "");
            undoLog.setXid(i + "");
            undoLog.setLogStatus(i);
            records.add(undoLog);
        }
        int bool = undoLogService.addUndoLogs(records);

        long end = System.currentTimeMillis();
        log.info("批量新增数据共用时:{}ms", (end - start));
        if (bool > 0) {
            return Result.ok(records);
        }
        return Result.failed();
    }

    @ApiOperation(value = "修改信息")
    @ApiImplicitParam(name = "undoLog", value = "", required = true, dataType = "UndoLog", paramType = "body")
    @PutMapping(value = "/update")
    public Result<?> update(@RequestBody UndoLog undoLog) {
        Long id = undoLog.getId();
        if (id == null) {
            return Result.failed("请选择需要修改的数据！");
        }
        boolean bool = undoLogService.updateUndoLog(undoLog);
        return Result.okOrFailed(bool);
    }

    @ApiOperation(value = "修改信息")
    @PostMapping(value = "/updateList")
    public Result<?> updateList() {
        long start = System.currentTimeMillis();
        List<UndoLog> undoLogAll = undoLogService.findUndoLogAll().stream().skip(9900).collect(Collectors.toList());
        List<UndoLog> updateList = new ArrayList<>();
        Date date = new Date();
        undoLogAll.forEach(u -> {
            UndoLog undoLog = new UndoLog();
            undoLog.setId(u.getId());
            undoLog.setContext(u.getContext() + 1);
            updateList.add(undoLog);
        });
        int bool = undoLogService.updateUndoLogs(updateList);

        long end = System.currentTimeMillis();
        log.info("批量更新数据共用时:{}ms", (end - start));
        return Result.ok();
    }

    @ApiOperation(value = "根据主键ID删除")
    @ApiImplicitParam(name = "id", value = "主键ID", required = true)
    @DeleteMapping(value = "/delete/{id}")
    public Result<?> delete(@PathVariable Long id) {
        if (id == null) {
            return Result.failed("请选择需要删除的数据！");
        }
        boolean bool = undoLogService.deleteUndoLogById(id);
        return Result.okOrFailed(bool);
    }

    @ApiOperation(value = "根据主键ID列表批量删除")
    @ApiImplicitParam(name = "idList", value = "主键ID列表", required = true, allowMultiple = true, paramType = "body")
    @DeleteMapping("/deleteList")
    public Result<?> deleteList(@RequestBody List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Result.failed("请选择需要删除的数据！");
        }
        boolean bool = undoLogService.deleteUndoLogByIds(idList);
        return Result.okOrFailed(bool);
    }
}