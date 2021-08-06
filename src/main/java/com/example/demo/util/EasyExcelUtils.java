package com.example.demo.util;

import com.alibaba.excel.EasyExcel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;

/**
 * @Author Chang
 * @Description EasyExcel工具类
 * @Date 2021/8/3 17:18
 **/
@Slf4j
public class EasyExcelUtils {

    private static final String EXCEL_XLSX = ".xlsx";

    /**
     * @Param: response
     * @Param: data 数据来源
     * @Param: clazz 泛型的class对象
     * @Param: fileName 文件名，同时也是表单名称，不需要带后缀名，默认'.xlsx'
     * @Author Chang
     * @Description 导出excel输出到web页面，限制为一个表单
     * @Date 2021/8/3 17:18
     * @Return void
     **/
    public static <T> void exportToHttp(HttpServletResponse response, List<T> data, Class<T> clazz, String fileName) {
        exportWithResponse(response, data, clazz, fileName);
    }

    /**
     * @Param: data
     * @Param: clazz
     * @Param: fileName
     * @Author Chang 
     * @Description 导出到web页面，可不传HttpServletResponse，自动从TheadLocal获取（若不是http请求则获取不到）
     * @Date 2021/8/4 13:44 
     * @Return void
     **/
    public static <T> void exportToHttp(List<T> data, Class<T> clazz, String fileName) {
        HttpServletResponse response = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();
        exportWithResponse(response, data, clazz, fileName);
    }

    private static <T> void exportWithResponse(HttpServletResponse response, List<T> data, Class<T> clazz, String fileName) {
        try (OutputStream out = response.getOutputStream()) {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName + EXCEL_XLSX, "UTF-8"));
            // 这里需要指定写用哪个class去写，然后写到第一个sheet，名字为文件名,然后文件流会自动关闭
            EasyExcel.write(out, clazz).sheet(fileName).doWrite(data);
        } catch (Exception e) {
            log.error("生成excel发生异常", e);
        }
    }

    /**
     * @Param: file
     * @Param: data
     * @Param: clazz
     * @Param: sheetName
     * @Author Chang
     * @Description 导出输出到文件
     * @Date 2021/8/3 18:18
     * @Return void
     **/
    public static <T> void exportToFile(File file, List<T> data, Class<T> clazz, String sheetName) {
        try {
            EasyExcel.write(file, clazz).sheet(sheetName).doWrite(data);
        } catch (Exception e) {
            log.error("生成excel发生异常", e);
        }
    }

    /**
     * @Param: data
     * @Param: clazz
     * @Param: sheetName
     * @Author Chang
     * @Description 输出到InputStream，可拿着InputStream上传到云服务器
     * @Date 2021/8/4 12:35
     * @Return java.io.InputStream
     **/
    public static <T> InputStream exportInputStream(List<T> data, Class<T> clazz, String sheetName) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            EasyExcel.write(out, clazz).sheet(sheetName).doWrite(data);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            log.error("生成excel发生异常", e);
        }
        return null;
    }

}
