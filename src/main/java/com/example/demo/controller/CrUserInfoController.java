package com.example.demo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.CrUserInfoEntity;
import com.example.demo.entity.RestResponse;
import com.example.demo.entity.SysLog;
import com.example.demo.service.CrUserInfoService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户信息Controller
 *
 * @author Antikvo
 * @date 2019-07-02 17:17:22
 */
@RestController
@RequestMapping("cr/user_info")
public class CrUserInfoController {
    @Autowired
    private CrUserInfoService crConsultantInfoService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("cr:user_info:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<CrUserInfoEntity> list = crConsultantInfoService.queryAll(params);
        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询用户信息
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("cr:user_info:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = crConsultantInfoService.queryPage(params);

        return RestResponse.success().put("page", page);
    }


    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("cr:user_info:info")
    public RestResponse info(@PathVariable("id") String id) {
        CrUserInfoEntity crConsultantInfo = crConsultantInfoService.getById(id);

        return RestResponse.success().put("user_info", crConsultantInfo);
    }

    /**
     * 新增用户信息
     *
     * @param crConsultantInfo crConsultantInfo
     * @return RestResponse
     */
    @SysLog("新增用户信息")
    @RequestMapping("/save")
    public RestResponse save(@RequestBody CrUserInfoEntity crConsultantInfo) {
        crConsultantInfoService.add(crConsultantInfo);
        return RestResponse.success();
    }

    /**
     * 修改用户信息
     *
     * @param crConsultantInfo crConsultantInfo
     * @return RestResponse
     */
    @SysLog("修改用户信息")
    @RequestMapping("/update")
    @RequiresPermissions("cr:user_info:update")
    public RestResponse update(@RequestBody CrUserInfoEntity crConsultantInfo) {
        crConsultantInfo.setUpdateTime(null);
        crConsultantInfoService.update(crConsultantInfo);

        return RestResponse.success();
    }

    /**
     * 重置密码
     *
     * @return RestResponse
     */
    @SysLog("修改用户信息")
    @RequestMapping("/resetPwd/{id}")
    @RequiresPermissions("cr:user_info:resetPwd")
    public RestResponse resetPwd(@PathVariable("id") String id) {
        CrUserInfoEntity crConsultantInfo = new CrUserInfoEntity();
        crConsultantInfo.setId(id);
        String salt = RandomStringUtils.randomAlphanumeric(15);
//        crConsultantInfo.setPassword(new Sha256Hash(Constant.DEFAULT_PW, salt).toHex());
        crConsultantInfo.setSalt(salt);
        crConsultantInfo.setUpdateTime(null);
        crConsultantInfoService.update(crConsultantInfo);
        return RestResponse.success("密码已重置为" + "");
    }

    /**
     * 根据主键删除用户信息
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除用户信息")
    @RequestMapping("/delete")
    @RequiresPermissions("cr:user_info:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        crConsultantInfoService.deleteBatch(ids);

        return RestResponse.success();
    }
}
