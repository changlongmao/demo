package com.example.demo.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.CrUserInfoEntity;
import com.example.demo.entity.Query;
import com.example.demo.mapper.CrUserInfoDao;
import com.example.demo.service.CrUserInfoService;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 用户信息Service实现类
 *
 * @author Antikvo
 * @date 2019-07-02 17:17:22
 */
@Service("crUserInfoService")
public class CrUserInfoServiceImpl extends ServiceImpl<CrUserInfoDao, CrUserInfoEntity> implements CrUserInfoService {

    @Override
    public List<CrUserInfoEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.ID");
        params.put("asc", false);
        Page<CrUserInfoEntity> page = new Query<CrUserInfoEntity>(params).getPage();
        return page.setRecords(baseMapper.selectCrConsultantInfoPage(page, params));
    }

    @Override
    public boolean add(CrUserInfoEntity crConsultantInfo) {
        return this.save(crConsultantInfo);
    }

    @Override
    public boolean update(CrUserInfoEntity crConsultantInfo) {
        return this.updateById(crConsultantInfo);
    }

    @Override
    public boolean delete(String id) {
        return this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(String[] ids) {
        return this.removeByIds(Arrays.asList(ids));
    }

    @Override
    public CrUserInfoEntity login(String phone, String password) {
        CrUserInfoEntity user = getOne(new QueryWrapper<CrUserInfoEntity>().eq("PHONE", phone));
        if (user == null || !user.getPassword().equals(new Sha256Hash(password, user.getSalt()).toHex())) {
            return null;
        } else {
            return user;
        }
    }
}
