package com.example.demo.entity;

import com.example.demo.enums.ExcelImportTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * @author ChangLF 2022-05-13
 */
@Data
@Accessors(chain = true)
public class ExcelImportDto<T> {

    /**
     * 导入的Excel 文件URL
     */
    private String fileUrl;

    /**
     * inputStream流
     */
    private InputStream inputStream;

    /**
     * 导入的Excel 文件
     */
    private MultipartFile file;

    /**
     * 输入枚举类型，1为fileUrl，2为inputStream，3为file，不填默认为fileUrl
     */
    private ExcelImportTypeEnum importTypeEnum;

    /**
     * 0 - 此工作表没有标题，因为第一行是数据(若第一行为标题，则会把标题解析为第一行数据)
     * 1 - 此工作表有一个行头，因为第二行是数据
     * 2 - 此工作表有两个行头，因为第三行是数据
     * 因图灵项目基本第一行表头为温馨提示，第二行为标题，第三行才是数据，不指定时默认为2
     */
    private Integer headRowNumber;

    /**
     * 导入最大数据条数，不指定默认为500，超出会抛出异常
     */
    private Integer maxRowNumber;

    /**
     * 表头类文件
     */
    private Class<T> clazz;

}
