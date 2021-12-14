package com.example.demo.controller;

import com.alibaba.excel.EasyExcel;
import com.example.demo.entity.RestResponse;
import com.example.demo.entity.User;
import com.example.demo.entity.UserTestTime;
import com.example.demo.util.EasyExcelListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author ChangLF 2021-12-11
 */
@Slf4j
@RestController
@RequestMapping("/testEasyExcel")
public class TestEasyExcelController {


    /**
     * easy excel导入
     * @param file
     * @author Chang
     * @date 2021/12/11 20:53
     * @return com.example.demo.entity.RestResponse
     **/
    @PostMapping("/importExcel")
    public RestResponse importExcel(@RequestParam("file") MultipartFile file) throws IOException {

        EasyExcelListener<UserTestTime> easyExcelListener = new EasyExcelListener<>();
        EasyExcel.read(file.getInputStream(), UserTestTime.class, easyExcelListener).sheet().doRead();
        List<UserTestTime> dataList = easyExcelListener.getDataList();

        log.info("数据条数为{}", dataList.size());
        return RestResponse.success().put("size", dataList.size());
    }
}
