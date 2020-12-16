package com.example.demo.util;

import com.example.demo.exception.BusinessException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.SheetUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName: PoiExcelUtil
 * @Description: Poi导入导出excel工具类
 * @Author: Chang
 * @Date: 2020/06/05 10:15
 **/
public class PoiExcelUtil {

    private static final Logger logger = LoggerFactory.getLogger(PoiExcelUtil.class);

    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";


    /**
     * @Param: response
     * @Param: params
     * @Author Chang
     * @Description excel导出
     * @Date 2020/6/8 14:49
     * @Return void
     **/
    public static void exportExcel(HttpServletResponse response, Map<String, Object> params) {
        if (!(response instanceof HttpServletResponse) || params == null || params.size() == 0) {
            return;
        }
        //创建Excel
        //创建HSSFWorkbook对象(excel的文档对象)
        HSSFWorkbook wb = new HSSFWorkbook();
        //建立新的sheet对象（excel的表单）
        HSSFSheet sheet = wb.createSheet(String.valueOf(params.get("excelFilename")));
        //获取标题样式
        HSSFCellStyle columnTopStyle = getColumnTopStyle(wb);
        //获取单元格样式
        HSSFCellStyle style = getStyle(wb);

        // 获取标题和字段名
        List<String> titleName = (List<String>) params.get("titleName");
        List<String> columnName = (List<String>) params.get("columnName");
        if (CollectionUtils.isEmpty(titleName)) {
            return;
        }

        //在sheet里创建标题行
        HSSFRow titleRow = sheet.createRow(0);

        //创建单元格并设置单元格内容
        for (int i = 0;i < titleName.size();i++){
            HSSFCell cell1 = titleRow.createCell(i);
            cell1.setCellValue(titleName.get(i));
            cell1.setCellStyle(columnTopStyle);
        }

        List<Map<String, Object>> data = (List<Map<String, Object>>) params.get("data");
        if (CollectionUtils.isNotEmpty(data) && CollectionUtils.isNotEmpty(columnName)) {
            // 遍历数据塞入单元格中
            for (int i = 0; i < data.size(); i++) {
                HSSFRow rown = sheet.createRow(i + 1);
                rown.setHeightInPoints(25);
                for (int j = 0;j < columnName.size();j++){
                    HSSFCell cell = rown.createCell(j);
                    Object value = data.get(i).get(columnName.get(j));
                    cell.setCellValue(value == null ? "" : value.toString());
                    cell.setCellStyle(style);
                }
            }
        }

        // 计算合并单元格
        int row = 1; // 当前指针所在行数
        List<Integer> mergeNumber = (List<Integer>) params.get("mergeNumber");
        List<String> mergeTitleName = (List<String>) params.get("mergeTitleName");
        if (CollectionUtils.isNotEmpty(mergeNumber) && CollectionUtils.isNotEmpty(mergeTitleName)) {
            for (Integer number : mergeNumber) {
                if (number != 1 && number != 0) {
                    for (String merge : mergeTitleName) {
                        int index = titleName.indexOf(merge);
                        if (index != -1) {
                            //合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
                            sheet.addMergedRegion(new CellRangeAddress(row, row + number - 1, index, index));
                        }
                    }
                }
                row += number;
            }
        }

        // 让列宽随着导出的列长自动适应,最大列宽不能超过50
        for (int colNum = 0; colNum < titleName.size(); colNum++) {
            double width = SheetUtil.getColumnWidth(sheet, colNum, false);
            if (width != -1) {
                width *= 256;
                int maxColumnWidth = 50 * 256;
                if (width > maxColumnWidth) {
                    width = maxColumnWidth;
                }
            }
            sheet.setColumnWidth(colNum, (int) (width));
        }
        //输出Excel文件
        try (OutputStream output = response.getOutputStream()){
            String filename = params.get("excelFilename") + ".xls";

            response.reset();
            response.setHeader("Content-disposition", "attachment; filename="+ URLEncoder.encode(filename,"UTF-8"));
            response.setContentType("application/ms-excel");
            wb.write(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Param: file
     * @Author Chang
     * @Description 导入excel表，支持xls和xlsx格式
     * @Date 2020/12/16 10:06
     * @Return java.util.List<java.util.Map<java.lang.String,java.lang.String>>
     **/
    public static List<Map<String,String>> importExcelMatchFormat(MultipartFile file) {

        // 检查问题
        checkFile(file);

        // 根据文件类型获取workbook
        Workbook workbook = getWorkBook(file);

        List<Map<String, String>> list = new ArrayList<>();

        // 获取表单
        Sheet sheet = workbook.getSheetAt(0);
        if (sheet == null) {
            return list;
        }
        DataFormatter dataFormatter = new DataFormatter();

        // 读取第一行数据获取表头
        Row firstRow = sheet.getRow(0);
        List<String> titleList = new ArrayList<>();
        for (int i = 0; i < firstRow.getLastCellNum(); i++) {
            String cellValue = dataFormatter.formatCellValue(firstRow.getCell(i));
            if (StringUtils.isNotBlank(cellValue)) {
                if (cellValue.equals("备注")) {
                    cellValue = dataFormatter.formatCellValue(firstRow.getCell(i - 1)) + cellValue;
                }
                titleList.add(cellValue);
            }
        }

        // 读取每一行数据
        for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
            Row row = sheet.getRow(rowNum);
            if (row == null) {
                continue;
            }
            Map<String, String> map = new HashMap<>();
            boolean flag = false;
            for (int i = 0; i < titleList.size(); i++) {
                Cell cell = row.getCell(i);
                if (cell == null) {
                    map.put(titleList.get(i), "");
                    continue;
                }
                String cellValue = "";
                if (cell.getCellTypeEnum() != CellType.STRING && HSSFDateUtil.isCellDateFormatted(cell)){
                    // 对日期类型数据处理
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    Date date = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
                    cellValue = sdf.format(date);
                }else if (cell.getCellTypeEnum() == CellType.FORMULA) {
                    // 公式算出的数值接收
                    cellValue = String.valueOf(cell.getNumericCellValue());
                }else {
                    cellValue = dataFormatter.formatCellValue(cell);
                }
                map.put(titleList.get(i), cellValue);
                if (StringUtils.isNotBlank(cellValue)) {
                    flag = true;
                }
            }
            if (flag) {
                list.add(map);
            }
        }
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * @Param: file
     * @Author Chang
     * @Description 检查文件
     * @Date 2020/12/16 10:40
     * @Return void
     **/
    private static void checkFile(MultipartFile file) {

        if (null == file) {
            logger.error("文件不存在！");
            throw new BusinessException("文件不存在！");
        }

        String fileName = file.getOriginalFilename();

        if (!fileName.endsWith(EXCEL_XLS) && !fileName.endsWith(EXCEL_XLSX)) {
            logger.error(fileName + "不是excel文件");
            throw new BusinessException(fileName + "不是excel文件");
        }
    }

    /**
     * @Param: file
     * @Author Chang
     * @Description 获取workbook
     * @Date 2020/12/16 10:40
     * @Return org.apache.poi.ss.usermodel.Workbook
     **/
    private static Workbook getWorkBook(MultipartFile file) {

        String fileName = file.getOriginalFilename();

        Workbook workbook = null;
        try {

            InputStream is = file.getInputStream();

            if (fileName.endsWith(EXCEL_XLS)) {

                workbook = new HSSFWorkbook(is);
            } else if (fileName.endsWith(EXCEL_XLSX)) {

                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
        return workbook;
    }

    /**
     * @Param: workbook
     * @Author Chang
     * @Description 列头单元格样式
     * @Date 2020/6/4 14:59
     * @Return org.apache.poi.hssf.usermodel.HSSFCellStyle
     **/
    private static HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {

        // 设置字体
        HSSFFont font = workbook.createFont();
        //设置字体大小
        font.setFontHeightInPoints((short)13);
        //字体加粗
        font.setBold(true);
        //设置字体名字
        font.setFontName("微软雅黑");
        //设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        //在样式用应用设置的字体;
        style.setFont(font);
        //设置自动换行;
        style.setWrapText(true);
        //设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        //设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // 设置背景颜色
//        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//        style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);

        return style;

    }

    /**
     * @Param: workbook
     * @Author Chang
     * @Description 列数据信息单元格样式
     * @Date 2020/6/4 14:59
     * @Return org.apache.poi.hssf.usermodel.HSSFCellStyle
     **/
    private static HSSFCellStyle getStyle(HSSFWorkbook workbook) {
        // 设置字体
        HSSFFont font = workbook.createFont();
        //设置字体大小
        font.setFontHeightInPoints((short)12);
        //设置字体名字
        font.setFontName("微软雅黑");
        //设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        //在样式用应用设置的字体;
        style.setFont(font);
        //设置自动换行;
        style.setWrapText(true);
        //设置水平对齐的样式为居中对齐;
//        style.setAlignment(HorizontalAlignment.CENTER);
        //设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }
}
