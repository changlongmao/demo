package com.example.demo.testMain;

import lombok.Data;

import java.util.List;

/**
 * 获取岗位类型层级结构展示类
 *
 * @author luoqilin
 * @date 2021-08-25
 */
@Data
public class PtListGetRespDto {

    private Integer id;

    /**
     * 父类ID
     */
    private Integer parentId;

    /**
     * 岗位名称
     */
    private String name;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 子类型
     */
    private List<PtListGetRespDto> subList;

}
