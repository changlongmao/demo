package com.example.demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.SysLogEntity;

import java.util.Map;


public interface SysLogService extends IService<SysLogEntity> {


    IPage queryPage(Map<String, Object> params);
}
