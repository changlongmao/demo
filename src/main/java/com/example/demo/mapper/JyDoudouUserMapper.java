package com.example.demo.mapper;

import com.example.demo.entity.JyDoudouUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * 豆豆兼职用户表
 *
 * @see JyDoudouUserEntity
 * @author changlf 2021-06-23
 */
@Mapper
public interface JyDoudouUserMapper {

    /**
     * 插入
     *
     * <strong>该方法由Allison 1875生成，请勿人为修改</strong>
     *
     * @param entity 豆豆兼职用户表
     * @return 插入条数
     */
    int insert(JyDoudouUserEntity entity);

    /**
     * 批量插入
     *
     * <strong>该方法由Allison 1875生成，请勿人为修改</strong>
     *
     * @param entities （多个）豆豆兼职用户表
     * @return 插入条数
     */
    int batchInsert(@Param("entities") Collection<JyDoudouUserEntity> entities);

    /**
     * 批量插入，为null的属性会被作为null插入
     *
     * <strong>该方法由Allison 1875生成，请勿人为修改</strong>
     *
     * @param entities （多个）豆豆兼职用户表
     * @return 插入条数
     */
    int batchInsertEvenNull(@Param("entities") Collection<JyDoudouUserEntity> entities);

    /**
     * （批量版）根据ID更新数据，为null的属性会被更新为null
     *
     * <strong>该方法由Allison 1875生成，请勿人为修改</strong>
     *
     * @param entities （多个）豆豆兼职用户表
     * @return 更新条数
     */
    int batchUpdateEvenNull(@Param("entities") Collection<JyDoudouUserEntity> entities);

    /**
     * 根据ID查询
     *
     * <strong>该方法由Allison 1875生成，请勿人为修改</strong>
     *
     * @param id
     * @return （多个）豆豆兼职用户表
     */
    JyDoudouUserEntity queryById(Long id);

    /**
     * 根据ID更新数据，忽略值为null的属性
     *
     * <strong>该方法由Allison 1875生成，请勿人为修改</strong>
     *
     * @param entity 豆豆兼职用户表
     * @return 更新条数
     */
    int updateById(JyDoudouUserEntity entity);

    /**
     * 根据ID更新数据，为null的属性会被更新为null
     *
     * <strong>该方法由Allison 1875生成，请勿人为修改</strong>
     *
     * @param entity 豆豆兼职用户表
     * @return 更新条数
     */
    int updateByIdEvenNull(JyDoudouUserEntity entity);

    /**
     * 根据多个ID查询
     *
     * <strong>该方法由Allison 1875生成，请勿人为修改</strong>
     *
     * @param ids （多个）
     * @return （多个）豆豆兼职用户表
     */
    List<JyDoudouUserEntity> queryByIds(@Param("ids") Collection<Long> ids);

    /**
     * 根据实体内的属性查询
     *
     * <strong>该方法由Allison 1875生成，请勿人为修改</strong>
     *
     * @param entity 条件
     * @return （多个）豆豆兼职用户表
     */
    List<JyDoudouUserEntity> queryByEntity(JyDoudouUserEntity entity);

    /**
     * 获取全部
     *
     * 出于性能考虑，这个方法只会返回最多500条数据
     * 事实上，只建议对数据量不大于500的配置表或常量表使用该方法，否则无法返回“全部”数据
     *
     * <strong>该方法由Allison 1875生成，请勿人为修改</strong>
     *
     * @return （多个）豆豆兼职用户表
     */
    List<JyDoudouUserEntity> listAll();
}
