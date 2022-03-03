package com.example.demo.testMain;

import com.alibaba.fastjson.JSON;
import com.example.demo.util.JsonUtils;
import org.apache.commons.collections.MultiMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ChangLF 2022-02-28
 */
public class TestFor {

    public static void main(String[] args) {
        List<PtListGetRespDto> all = new ArrayList<>();

        all.add(new PtListGetRespDto(){{
            setId(1);
            setParentId(0);
        }});
        all.add(new PtListGetRespDto(){{
            setId(2);
            setParentId(1);
        }});
        all.add(new PtListGetRespDto(){{
            setId(3);
            setParentId(1);
        }});
        all.add(new PtListGetRespDto(){{
            setId(4);
            setParentId(2);
        }});
        all.add(new PtListGetRespDto(){{
            setId(5);
            setParentId(2);
        }});
        all.add(new PtListGetRespDto(){{
            setId(6);
            setParentId(3);
        }});
        all.add(new PtListGetRespDto(){{
            setId(7);
            setParentId(3);
        }});

        Map<Integer, PtListGetRespDto> map = new HashMap<>(all.size() * 2);
        all.forEach(item -> {
            item.setSubList(new ArrayList<>());
            map.put(item.getId(), item);
        });
        for (PtListGetRespDto item : all) {
            recursionAssemblyPositionData(item, map);
        }
        System.out.println(JsonUtils.toJson(all));
        System.out.println(JSON.toJSON(all));
        System.out.println(all);

    }


    /**
     * 递归处理岗位数据
     */
    private static void recursionAssemblyPositionData(PtListGetRespDto item, Map<Integer, PtListGetRespDto> map) {
        if (item.getParentId() == null) {
            return;
        }
        PtListGetRespDto parent = map.get(item.getParentId());
        if (parent == null) {
            return;
        }
        parent.getSubList().add(item);
    }
}
