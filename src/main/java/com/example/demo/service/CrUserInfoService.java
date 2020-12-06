package com.example.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.CrUserInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * 用户信息Service接口
 *
 * @author Antikvo
 * @date 2019-07-02 17:17:22
 */
public interface CrUserInfoService extends IService<CrUserInfoEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<CrUserInfoEntity> queryAll(Map<String, Object> params);

    /**
     * 分页查询用户信息
     *
     * @param params 查询参数
     * @return Page
     */
    Page queryPage(Map<String, Object> params);

    /**
     * 新增用户信息
     *
     * @param crConsultantInfo 用户信息
     * @return 新增结果
     */
    boolean add(CrUserInfoEntity crConsultantInfo);

    /**
     * 根据主键更新用户信息
     *
     * @param crConsultantInfo 用户信息
     * @return 更新结果
     */
    boolean update(CrUserInfoEntity crConsultantInfo);

    /**
     * 根据主键删除用户信息
     *
     * @param id id
     * @return 删除结果
     */
    boolean delete(String id);

    /**
     * 根据主键批量删除
     *
     * @param ids ids
     * @return 删除结果
     */
    boolean deleteBatch(String[] ids);

    /**
     * web端登录接口
     *
     * @param phone    用户名
     * @param password 密码
     * @return 用户信息，账号或密码错误时抛异常
     */
    CrUserInfoEntity login(String phone, String password);
}
