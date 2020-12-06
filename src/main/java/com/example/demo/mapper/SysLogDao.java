package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.SysLogEntity;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface SysLogDao extends BaseMapper<SysLogEntity> {

}
