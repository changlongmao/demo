package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.demo.entity.CrUserInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户信息Dao
 *
 * @author Antikvo
 * @date 2019-07-02 17:17:22
 */
@Mapper
public interface CrUserInfoDao extends BaseMapper<CrUserInfoEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<CrUserInfoEntity> queryAll(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<CrUserInfoEntity> selectCrConsultantInfoPage(IPage page, @Param("params") Map<String, Object> params);
}
