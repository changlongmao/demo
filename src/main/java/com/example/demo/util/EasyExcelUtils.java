package com.example.demo.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.SheetUtil;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Author Chang
 * @Description EasyExcel工具类
 * @Date 2021/8/3 17:18
 **/
@Slf4j
public class EasyExcelUtils {

    /**
     * 导出文件的后缀名
     */
    private static final String EXCEL_XLSX = ".xlsx";
    private static final String EXCEL_XLS = ".xls";
    private static final String XLSX_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private static final String XLS_CONTENT_TYPE = "application/vnd.ms-excel";


    /**
     * 最大导出数量
     */
    private static final Integer MAX_TOTAL = 20000;

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
     * @Param: fileName 文件名，同时也是表单名称，不需要带后缀名，默认'.xlsx'
     * @Author Chang
     * @Description 导出到web页面，可不传HttpServletResponse，自动从TheadLocal获取（若不是http请求则获取不到）
     * @Date 2021/8/4 13:44
     * @Return void
     **/
    public static <T> void exportToHttp(List<T> data, Class<T> clazz, String fileName) {
        HttpServletResponse response =
                ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();
        exportWithResponse(response, data, clazz, fileName);
    }

    private static <T> void exportWithResponse(HttpServletResponse response, List<T> data, Class<T> clazz,
                                               String fileName) {
        if (CollectionUtils.isEmpty(data)) {
            return;
        }
        if (data.size() > MAX_TOTAL) {
            throw new ApiException("1220003");
        }
        try {
            OutputStream out = response.getOutputStream();
            response.setContentType(XLSX_CONTENT_TYPE);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + URLEncoder.encode(fileName + EXCEL_XLSX, "UTF-8"));
            // 这里需要指定写用哪个class去写，然后写到第一个sheet，名字为文件名,然后文件流会自动关闭
            EasyExcel.write(out, clazz).registerWriteHandler(new AdaptiveColumnWidthStyleStrategy(data.size())).sheet(fileName).doWrite(data);
        } catch (Exception e) {
            log.error("生成excel发生异常", e);
            response.setContentType("application/json;charset=UTF-8");
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

    /**
     * easyExcel宽度自适应策略
     *
     * @author Chang
     * @date 2021/10/9 9:49
     **/
    public static class AdaptiveColumnWidthStyleStrategy extends AbstractColumnWidthStyleStrategy {

        /**
         * 最大最小宽度
         */
        private static final int MIN_COLUMN_WIDTH = 10;
        private static final int MAX_COLUMN_WIDTH = 50;
        private final Map<Integer, Double> cache = new HashMap<>();
        // 数据条数，方便找到最后一行
        private final int size;

        public AdaptiveColumnWidthStyleStrategy(int size) {
            this.size = size;
        }

        /**
         * 先计算表头自适应列宽，再在最后一行计算自适应列宽，设置更大的那个；
         * 因为easyExcel有缓存，sheet只能拿到500条数据，只计算了最后500条的自适应列宽；
         * 基本能满足场景需求
         *
         * @param writeSheetHolder
         * @param cellDataList
         * @param cell
         * @param head
         * @param relativeRowIndex
         * @param isHead
         * @author Chang
         * @date 2021/10/9 9:56
         **/
        @Override
        protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<CellData> cellDataList, Cell cell,
                                      Head head,
                                      Integer relativeRowIndex, Boolean isHead) {
            // 先对表头计算自适应列宽并缓存
            if (isHead) {
                cache.put(cell.getColumnIndex(), SheetUtil.getColumnWidth(writeSheetHolder.getSheet(),
                        cell.getColumnIndex()
                        , false));
            }

            // 当处于最后一行的时候，计算一下列宽并设置，其他情况返回
            if (size - 1 != relativeRowIndex) {
                return;
            }

            double columnWidth = Math.max(SheetUtil.getColumnWidth(writeSheetHolder.getSheet(), cell.getColumnIndex()
                    , false), cache.get(cell.getColumnIndex()));

            // xlsx文档计算列表不准确，进行适当加宽
            columnWidth += 7;
            if (columnWidth < 0) {
                return;
            }
            if (columnWidth > MAX_COLUMN_WIDTH) {
                columnWidth = MAX_COLUMN_WIDTH;
            } else if (columnWidth < MIN_COLUMN_WIDTH) {
                columnWidth = MIN_COLUMN_WIDTH;
            }
            writeSheetHolder.getSheet().setColumnWidth(cell.getColumnIndex(), (int) (columnWidth * 256));
        }

    }
}
