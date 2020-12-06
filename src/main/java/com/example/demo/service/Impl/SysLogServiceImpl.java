package com.example.demo.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.Query;
import com.example.demo.entity.SysLogEntity;
import com.example.demo.mapper.SysLogDao;
import com.example.demo.service.SysLogService;
import com.example.demo.util.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("sysLogService")
public class SysLogServiceImpl extends ServiceImpl<SysLogDao, SysLogEntity> implements SysLogService {

    @Override
    public IPage queryPage(Map<String, Object> params) {
        String key = (String) params.get("key");

        Page<SysLogEntity> page = new Query<SysLogEntity>(params).getPage();

        return baseMapper.selectPage(page,
                new QueryWrapper<SysLogEntity>().like(StringUtils.isNotBlank(key), "USER_NAME", key)
                        .or().like(StringUtils.isNotBlank(key), "OPERATION", key)
                        .orderByDesc("CREATE_TIME"));
    }
}
