package com.example.demo.entity;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;


public class Query<T> extends LinkedHashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    private Page<T> page;

    private int currPage = 1;

    private int limit = 10;

    private static final String ASC = "asc";

    public Query(Map<String, Object> params) {

        String strPage = "page";
        String strLimit = "limit";
        this.putAll(params);


        if (params.get(strPage) != null) {
            currPage = Integer.parseInt((String) params.get("page"));
        }
        if (params.get(strLimit) != null) {
            limit = Integer.parseInt((String) params.get("limit"));
        }

        this.put("offset", (currPage - 1) * limit);
        this.put("page", currPage);
        this.put("limit", limit);

        String sidx = (String) params.get("sidx");

        Boolean asc = true;
        if (!StringUtils.isNullOrEmpty(params.get(ASC))) {
            asc = (Boolean) params.get("asc");
        }

        this.page = new Page<>(currPage, limit);


        if (StringUtils.isNotBlank(sidx)) {
            if (asc) {
                this.page.setAsc(sidx);
            } else {
                this.page.setDesc(sidx);
            }
        }

    }

    public Page<T> getPage() {
        return page;
    }

    public int getCurrPage() {
        return currPage;
    }

    public int getLimit() {
        return limit;
    }
}
