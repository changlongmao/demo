package com.example.demo.controller;

import java.util.List;

import com.example.demo.entity.Result;
import com.example.demo.entity.UserTestTime;
import com.example.demo.entity.UserTestTimeCondition;
import com.example.demo.service.UserTestTimeService;
import com.example.demo.util.TimeUtil;
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
@Api(tags = "user_test_time服务")
@RestController
@RequestMapping("/userTestTime")
public class UserTestTimeController {
    @Autowired
    private UserTestTimeService userTestTimeService;

    /**
     * 根据查询参数分页查询列表
     * @param condition
     * @author ChangLF 2023/7/14 09:50
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.example.demo.entity.UserTestTime>
     **/
    @ApiOperation(value = "根据查询参数分页查询列表")
    @ApiImplicitParam(name = "condition", value = "查询条件", required = true, dataType = "UserTestTimeCondition", paramType = "body")
    @PostMapping("/list")
    public IPage<UserTestTime> list(@RequestBody UserTestTimeCondition condition) {
        IPage<UserTestTime> page = userTestTimeService.findUserTestTimeByCondition(condition);
        return page;
    }

    @ApiOperation(value = "根据主键ID查询信息")
    @ApiImplicitParam(name = "id", value = "主键ID", required = true)
    @GetMapping(value = "/get/{id}")
    public Result<UserTestTime> get(@PathVariable Long id) {
        if (id == null) {
            return Result.failed("请选择需要查询的数据！");
        }
        UserTestTime userTestTime = userTestTimeService.getUserTestTimeById(id);
        return Result.ok(userTestTime);
    }

    @ApiOperation(value = "新增信息")
    @ApiImplicitParam(name = "userTestTime", value = "", required = true, dataType = "UserTestTime", paramType = "body")
    @PostMapping("/add")
    public Result<?> add(@RequestBody UserTestTime userTestTime) {
        boolean bool = userTestTimeService.addUserTestTime(userTestTime);
        if (bool) {
            return Result.ok(userTestTime);
        }
        return Result.failed();
    }

    @ApiOperation(value = "修改信息")
    @ApiImplicitParam(name = "userTestTime", value = "", required = true, dataType = "UserTestTime", paramType = "body")
    @PutMapping(value = "/update")
    public Result<?> update(@RequestBody UserTestTime userTestTime) {
        Long id = userTestTime.getId();
        if (id == null) {
            return Result.failed("请选择需要修改的数据！");
        }
        boolean bool = userTestTimeService.updateUserTestTime(userTestTime);
        return Result.okOrFailed(bool);
    }

    @ApiOperation(value = "根据主键ID删除")
    @ApiImplicitParam(name = "id", value = "主键ID", required = true)
    @DeleteMapping(value = "/delete/{id}")
    public Result<?> delete(@PathVariable Long id) {
        if (id == null) {
            return Result.failed("请选择需要删除的数据！");
        }
        boolean bool = userTestTimeService.deleteUserTestTimeById(id);
        return Result.okOrFailed(bool);
    }

    @ApiOperation(value = "根据主键ID列表批量删除")
    @ApiImplicitParam(name = "idList", value = "主键ID列表", required = true, allowMultiple = true, paramType = "body")
    @DeleteMapping("/deleteList")
    public Result<?> deleteList(@RequestBody List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Result.failed("请选择需要删除的数据！");
        }
        boolean bool = userTestTimeService.deleteUserTestTimeByIds(idList);
        return Result.okOrFailed(bool);
    }


    public static void main(String[] args) {
        System.out.println(handlerBelongDepartment("杭州今元标矩科技有限公司/招聘交付中心/北区/招聘一组"));
        System.out.println(getTimeStart(0));
        System.out.println(System.currentTimeMillis());
        System.out.println("杭州今元标矩科技有限公司/招聘交付中心/北区/招聘一组".substring("杭州今元标矩科技有限公司/招聘交付中心/北区/招聘一组".indexOf("/")+1));
    }
    public static long getTimeStart(long time) {
        String startDateStr = TimeUtil.parseTime(time, TimeUtil.TimeFormat.SHORT_DATE_PATTERN_LINE) + " 00:00:00";
        return TimeUtil.parseTimeStrToLong(startDateStr);
    }

    public static String handlerBelongDepartment(String belongDepartment) {
        if (StringUtils.isEmpty(belongDepartment)) {
            return belongDepartment;
        }
        String[] departmentArr = belongDepartment.split("/");
        if (departmentArr.length <= 1) {
            return belongDepartment;
        }
        StringBuilder result = new StringBuilder();
        if (belongDepartment.contains("分公司")) {
            for (String s : departmentArr) {
                if (s.contains("分公司")) {
                    result = new StringBuilder(s);
                    break;
                }
            }
            if (result.toString().equals(departmentArr[departmentArr.length - 1])) {
                return result.toString();
            }
            return result + "/" + departmentArr[departmentArr.length - 1];
        }
        for (int i = 1; i < departmentArr.length; i++) {
            result.append("/").append(departmentArr[i]);
        }
        return result.substring(1, result.length());
    }
}