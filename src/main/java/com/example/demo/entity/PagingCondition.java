package com.example.demo.entity;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.ApiModelProperty;

/**
 * 分页查询参数封装，用于继承
 *
 * @author ${paramConfig.author}
 * @version 1.0.0 ${today}
 */
public abstract class PagingCondition extends OverrideBeanMethods {
    /** 版本号 */
    private static final long serialVersionUID = -5826914803574438815L;

    @ApiModelProperty(value = "每页显示多少条记录", example = "20")
    private Long pageSize = 20L;

    @ApiModelProperty(value = "当前页", example = "1")
    private Long page = 1L;

    @ApiModelProperty(value = "排序字段")
    private String sortField;

    @ApiModelProperty(value = "排序方式，只能是ASC或DESC")
    private String sortOrder;

    /**
     * 获取每页显示多少条记录
     *
     * @return 每页显示多少条记录
     */
    public Long getPageSize() {
        return this.pageSize;
    }

    /**
     * 设置每页显示多少条记录
     *
     * @param pageSize
     *          每页显示多少条记录
     */
    public void setPageSize(Long pageSize) {
        if (pageSize != null && pageSize > 0) {
            this.pageSize = pageSize;
        }
    }

    /**
     * 获取当前页
     *
     * @return 当前页
     */
    public Long getPage() {
        return this.page;
    }

    /**
     * 设置当前页
     *
     * @param page
     *          当前页
     */
    public void setPage(Long page) {
        if (page != null && page > 0) {
            this.page = page;
        }
    }

    /**
     * 获取排序字段
     *
     * @return 排序字段
     */
    public String getSortField() {
        return this.sortField;
    }

    /**
     * 设置排序字段
     *
     * @param sortField
     *          排序字段
     */
    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    /**
     * 获取排序方式，只能是ASC或DESC
     *
     * @return 排序方式
     */
    public String getSortOrder() {
        return this.sortOrder;
    }

    /**
     * 设置排序方式，只能是ASC或DESC
     *
     * @param sortOrder
     *          排序方式
     */
    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    /** 创建简单分页模型 */
    public <T> Page<T> buildPage() {
        return new Page<>(page, pageSize);
    }
}