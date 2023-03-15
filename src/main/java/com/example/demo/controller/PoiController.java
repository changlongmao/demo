package com.example.demo.controller;

import com.example.demo.common.BaseResponse;
import com.example.demo.exception.ResponseUtil;
import com.example.demo.service.UserService;
import com.example.demo.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.*;


/**
 * @ClassName: PoiController
 * @Description: 测试poi
 * @Author: Chang
 * @Date: 2020/06/04 11:30
 **/
@Slf4j
//@RestController
@RequestMapping("/poi")
public class PoiController {

    @Autowired
    private UserService userService;

    private final static String xls = "xls";
    private final static String xlsx = "xlsx";
    //导入excel数据
    /*@RequestMapping(value = "1425", method = {RequestMethod.GET, RequestMethod.POST})
    public Response<Object> importFile(@RequestParam("file") MultipartFile file){
        //目标：读excel，入库
        List<UwoSpecialUser> uwoSpecialUserList=new ArrayList<>();
        Response<Object> result = new Response<Object>();
        try {
            int count = 0;
            String msg = "";
            //技巧：平时怎么读，代码怎么写。
            //1.打开工作簿(xls、xlsx格式)
            Workbook workbook = getWorkBook(file);
            //2.从工作簿中打开工作表
            //workbook.getSheet("Sheet1");//根据名字读取工作表
            Sheet sheet = workbook.getSheetAt(0);//g根据索引来读取工作表0-based physical & logical
            //3.一行一行读
            for (Row row : sheet) {
                //第一行一般是标题，要跳过
                if(row.getRowNum()==0||row.getRowNum()==1){
                    continue;
                }

                //将值封装到对象
                UwoSpecialUser uwoSpecialUser=new UwoSpecialUser();

                //一格一格读数据
                if (row.getCell(0)!=null){
                    row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
                    String specialUserName = row.getCell(0).getStringCellValue();
                    uwoSpecialUser.setSpecialUserName(specialUserName);
                }
                if (row.getCell(1)!=null){
                    row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
                    String phonenum = row.getCell(1).getStringCellValue();
                    uwoSpecialUser.setPhonenum(phonenum);
                }
                if (row.getCell(2)!=null){
                    row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
                    String money = row.getCell(2).getStringCellValue();
                    if (money!=null&&!"".equals(money)){
                        uwoSpecialUser.setMoney(Double.valueOf(money));
                    }
                }
                if (row.getCell(3)!=null){
                    row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
                    String type = row.getCell(3).getStringCellValue();
                    if (type!=null&&!"".equals(type)){
                        uwoSpecialUser.setType(Integer.valueOf(type));
                    }
                }
                if (uwoSpecialUser!=null){
                    if (uwoSpecialUser.getCreateTime() == null) {
                        uwoSpecialUser.setCreateTime(new Date());
                        uwoSpecialUser.setUpdateTime(uwoSpecialUser.getCreateTime());
                    }
                    //判断手机号码不为空的才执行插入数据
                    if (StringUtils.isNotBlank(uwoSpecialUser.getPhonenum())){
                        if (StringUtils.isNotBlank(uwoSpecialUser.getSpecialUserName())){
                            //将对象填入集合
                            uwoSpecialUserList.add(uwoSpecialUser);
                        }else {
                            msg += uwoSpecialUser.getPhonenum()+",";
                        }
                    }
                }
            }

            for (UwoSpecialUser uwoSpecialUser:uwoSpecialUserList) {
                try {
                    int i = uwoSpecialUserService.insertInfo(uwoSpecialUser);
                    count += i;
                } catch (Exception e) {
                    e.printStackTrace();
                    msg += uwoSpecialUser.getPhonenum()+",";
                }
            }
            if (msg!=null&&!"".equals(msg)){
                msg+="该手机号码已存在或者不合法或者没有填写姓名";
                result.setMsg(msg);
            }
            result.setData("成功上传"+count+"条数据");
        } catch (Exception e) {
            e.printStackTrace();
            result.setData("上传失败，Excel格式有误");
            result.setResponseState(ResponseState.FAILED);
        }
        return result;
    }*/

    //公共方法获取2003和2007的Workbook工作薄对象
    /*public static Workbook getWorkBook(MultipartFile file) {
        //获得文件名
        String fileName = file.getOriginalFilename();
        //创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        try {
            //获取excel文件的io流
            InputStream is = file.getInputStream();
            //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            if(fileName.endsWith(xls)){
                //2003
                workbook = new HSSFWorkbook(is);
            }else if(fileName.endsWith(xlsx)){
                //2007
                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {
            log.info(e.getMessage());
        }
        return workbook;
    }*/

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public Response<Object> download(HttpServletResponse response) throws IOException {
        //创建Excel
        //创建HSSFWorkbook对象(excel的文档对象)
        HSSFWorkbook wb = new HSSFWorkbook();
        //建立新的sheet对象（excel的表单）
        HSSFSheet sheet = wb.createSheet("统一受理");
        HSSFCellStyle columnTopStyle = this.getColumnTopStyle(wb);
        //单元格样式对象
        HSSFCellStyle style = this.getStyle(wb);
        //在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
        HSSFRow row1 = sheet.createRow(0);
        //创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
        HSSFCell cell = row1.createCell(0);
        row1.setHeightInPoints(30);
        //设置单元格内容
        cell.setCellValue("统一受理事件列表");
        cell.setCellStyle(columnTopStyle);
        List<String> rowsName = Arrays.asList("事件编号", "来访人姓名", "手机号", "身份证号", "受理中心", "业务类型", "来访时间", "事发地址", "事件描述");
        //合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, rowsName.size() - 1));
        //在sheet里创建第二行
        HSSFRow row2 = sheet.createRow(1);
        row2.setHeightInPoints(30);

        //创建单元格并设置单元格内容
        for (int i = 0; i < rowsName.size(); i++) {
            HSSFCell cell1 = row2.createCell(i);
            cell1.setCellValue(rowsName.get(i));
            cell1.setCellStyle(columnTopStyle);
        }

        List<String> rowsData = Arrays.asList("202006040003", "叶会田", "13646586477", "330324196608231036", "人民来访接待中心", "公安局", "2020-06-04 09:05:55", "黄田街道枫埠村", "反映信访人坐落于黄田街道枫埠村的老屋建于1986年，后因道路拓宽不允许发放产权证，现道路建设需要拆迁，以违章建筑处理，要求给予合理的拆迁安置。");
        //在sheet里创建第三行
        HSSFRow rown = sheet.createRow(2);
        for (int i = 0; i < rowsData.size(); i++) {
            HSSFCell cell0 = rown.createCell(i);
            cell0.setCellValue(rowsData.get(i));
            cell0.setCellStyle(style);
        }
        rown.setHeightInPoints(20);

        //让列宽随着导出的列长自动适应
        for (int colNum = 0; colNum < rowsName.size(); colNum++) {
            sheet.autoSizeColumn((short) colNum);
        }
        //输出Excel文件
        OutputStream output = response.getOutputStream();
        response.reset();
        response.setHeader("Content-disposition", "attachment; filename=" + toUtf8String("统一受理事件列表.xls"));
        response.setContentType("application/msexcel");
        wb.write(output);
        output.close();
        return null;
    }


    /**
     * @Param: s
     * @Author Chang
     * @Description 解决文件名乱码问题
     * @Date 2020/6/5 8:51
     * @Return java.lang.String
     **/
    public static String toUtf8String(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= 0 && c <= 255) {
                sb.append(c);
            } else {
                byte[] b;
                try {
                    b = Character.toString(c).getBytes("utf-8");
                } catch (Exception ex) {
                    System.out.println(ex);
                    b = new byte[0];
                }
                for (int j = 0; j < b.length; j++) {
                    int k = b[j];
                    if (k < 0) {
                        k += 256;
                    }
                    sb.append("%" + Integer.toHexString(k).toUpperCase());
                }
            }
        }
        return sb.toString();
    }

    /**
     * @Param: workbook
     * @Author Chang
     * @Description 列头单元格样式
     * @Date 2020/6/4 14:59
     * @Return org.apache.poi.hssf.usermodel.HSSFCellStyle
     **/
    public HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {

        // 设置字体
        HSSFFont font = workbook.createFont();
        //设置字体大小
        font.setFontHeightInPoints((short) 11);
        //字体加粗
        font.setBold(true);
        //设置字体名字
        font.setFontName("微软雅黑");
        //设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        //在样式用应用设置的字体;
        style.setFont(font);
        //设置自动换行;
        style.setWrapText(false);
        //设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        //设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;

    }

    public XSSFCellStyle getColumnTopStyle(XSSFWorkbook workbook) {

        // 设置字体
        XSSFFont font = workbook.createFont();
        //设置字体大小
        font.setFontHeightInPoints((short) 11);
        //字体加粗
        font.setBold(true);
        //设置字体名字
        font.setFontName("微软雅黑");
        //设置样式;
        XSSFCellStyle style = workbook.createCellStyle();
        //在样式用应用设置的字体;
        style.setFont(font);
        //设置自动换行;
        style.setWrapText(false);
        //设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        //设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;

    }

    public XSSFCellStyle getStyle(XSSFWorkbook workbook) {
        // 设置字体
        XSSFFont font = workbook.createFont();
        //设置字体大小
        font.setFontHeightInPoints((short) 10);
        //设置字体名字
        font.setFontName("微软雅黑");
        //设置样式;
        XSSFCellStyle style = workbook.createCellStyle();
        //在样式用应用设置的字体;
        style.setFont(font);
        //设置自动换行;
        style.setWrapText(true);
        //设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        //设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    /**
     * @Param: workbook
     * @Author Chang
     * @Description 列数据信息单元格样式
     * @Date 2020/6/4 14:59
     * @Return org.apache.poi.hssf.usermodel.HSSFCellStyle
     **/
    public HSSFCellStyle getStyle(HSSFWorkbook workbook) {
        // 设置字体
        HSSFFont font = workbook.createFont();
        //设置字体大小
        font.setFontHeightInPoints((short) 10);
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
        return style;
    }


    @RequestMapping(value = "/importExcelXian", method = RequestMethod.GET)
    public BaseResponse importExcelXian(HttpServletResponse response) throws IOException {
        long startTime = System.currentTimeMillis();
        List<Map<String, Object>> list = new ArrayList<>();
        XSSFWorkbook workbook = null;
        try (InputStream inputStream = new FileInputStream("D:/西安市2020年1-6月份网络宣传情况排名分数.xlsx")) {
            // 读取Excel文件
            workbook = new XSSFWorkbook(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Map<String, Object>> sheetList = new ArrayList<>();

        for (int sheetNumber = 0; sheetNumber < workbook.getNumberOfSheets(); sheetNumber++) {
            // 获取表单
            XSSFSheet xssfSheet = workbook.getSheetAt(sheetNumber);
            if (xssfSheet == null) {
                continue;
            }
            System.out.println(xssfSheet.getSheetName());
            Map<String, Object> sheetMap = new HashMap<>();
            List<Map<String, Object>> dataList = new ArrayList<>();

            DecimalFormat df = new DecimalFormat("0.00000000");

            // 循环行
            double firstPoint = xssfSheet.getRow(2).getCell(2).getNumericCellValue();
            for (int rowNum = 2; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                if (xssfRow == null) {
                    continue;
                }

                // 将单元格中的内容存入集合
                Map<String, Object> rowMap = new HashMap<>(4);
                String unit = xssfRow.getCell(1).getStringCellValue();
                double rowPoint = Double.parseDouble(df.format(xssfRow.getCell(2).getNumericCellValue() / firstPoint * 100));
                rowMap.put("单位", unit);
                rowMap.put("分值", rowPoint);
                dataList.add(rowMap);
            }
            sheetMap.put("sheetName", xssfSheet.getSheetName());
            sheetMap.put("dataList", dataList);
            sheetList.add(sheetMap);
        }
        System.out.println(sheetList.toString());


        // 导出excel表
        //建立新的sheet对象（excel的表单）
        XSSFSheet sheet = workbook.createSheet("综合数据");
        XSSFRow firstRow = sheet.createRow(0);

        XSSFCellStyle columnTopStyle = this.getColumnTopStyle(workbook);
        //单元格样式对象
        XSSFCellStyle style = this.getStyle(workbook);
        //创建单元格并设置单元格内容
        XSSFCell firstRowCell = firstRow.createCell(0);
        firstRowCell.setCellValue("单位");
        firstRowCell.setCellStyle(columnTopStyle);
        for (int i = 0; i < sheetList.size(); i++) {
            XSSFCell cell1 = firstRow.createCell(i +1);
            cell1.setCellValue(String.valueOf(sheetList.get(i).get("sheetName")));
            cell1.setCellStyle(columnTopStyle);
        }
        XSSFCell lastRowCell = firstRow.createCell(sheetList.size() +1);
        lastRowCell.setCellValue("平均分");
        lastRowCell.setCellStyle(columnTopStyle);

        // 获取所有单位
        Set<String> unitSet = new HashSet<>();
        for (Map<String, Object> map : sheetList) {
            List<Map<String, Object>> dataList = (List<Map<String, Object>>) map.get("dataList");
            dataList.forEach(data -> unitSet.add(String.valueOf(data.get("单位"))));
        }

        // 将所有分值循环塞入表中
        List<String> unitList = new ArrayList<>(unitSet);
        for (int i = 0; i < unitList.size(); i++) {
            XSSFRow row = sheet.createRow(i + 1);
            XSSFCell firstCell = row.createCell(0);
            String unit = unitList.get(i);
            firstCell.setCellValue(unit);
            firstCell.setCellStyle(style);
            double totalPoint = 0;
            int num = 0;
            for (int j = 0; j < sheetList.size(); j++) {
                XSSFCell cell1 = row.createCell(j +1);
                cell1.setCellStyle(style);
                List<Map<String, Object>> dataList = (List<Map<String, Object>>) sheetList.get(j).get("dataList");
                boolean flag = false;
                for (Map<String, Object> map : dataList) {
                    if (unit.equals(map.get("单位"))) {
                        double point = Double.parseDouble(String.valueOf(map.get("分值")));
                        cell1.setCellValue(point);
                        totalPoint += point;
                        num++;
                        flag = true;
                    }
                    if (!flag) {
                        cell1.setCellValue(0);
                    }
                }
            }
            // 算出平均值
            XSSFCell cell = row.createCell(sheetList.size() + 1);
            cell.setCellValue(totalPoint/num);
            cell.setCellStyle(style);
        }

        //让列宽随着导出的列长自动适应
        for (int colNum = 0; colNum < sheetList.size() + 1; colNum++) {
            sheet.autoSizeColumn((short) colNum);
        }
        //输出Excel文件
        OutputStream output = response.getOutputStream();
        response.reset();
        response.setHeader("Content-disposition", "attachment; filename=" + toUtf8String("西安市2020年1-6月份网络宣传情况排名分数.xlsx"));
        response.setContentType("application/msexcel");
        workbook.write(output);
        output.close();

        Long endTime = System.currentTimeMillis();
        System.out.println("导出excel表共用时" + (endTime - startTime) + "ms");
        return ResponseUtil.success();
    }



    public static void main(String[] args) {
    }

    public static BaseResponse importExcelHeader() {
        List<Map<String, Object>> list = new ArrayList<>();
        HSSFWorkbook workbook = null;
        try (InputStream inputStream = new FileInputStream("D:/平安指数.xls")) {
            // 读取Excel文件
            workbook = new HSSFWorkbook(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 获取表单
        HSSFSheet hssfSheet = workbook.getSheetAt(0);
        if (hssfSheet == null) {
            return ResponseUtil.error("工作表为空");
        }
        DataFormatter dataFormatter = new DataFormatter();

        // 表头合并单元格数量
        List<Integer> numberList = new ArrayList<>();
        HSSFRow firstRow = hssfSheet.getRow(0);
        int temp = 1;
        for (int i = 0; i < firstRow.getLastCellNum(); i++) {
            String cellValue = dataFormatter.formatCellValue(firstRow.getCell(i + 1));
            if (StringUtils.isNotBlank(cellValue)) {
                numberList.add(temp);
                temp = 1;
            } else {
                temp += 1;
            }
        }
        System.out.println(numberList.toString());
        // 基础工作类型
        List<String> normalWorkList = new ArrayList<>();
        for (int i = numberList.get(1); i < firstRow.getLastCellNum() - 3; i++) {
            String cellValue = dataFormatter.formatCellValue(firstRow.getCell(i));
            if (StringUtils.isNotBlank(cellValue)) {
                normalWorkList.add(cellValue);
            }
        }

        // 获取平安工作类型
        HSSFRow secondRow = hssfSheet.getRow(1);
        List<String> safeWorkList = new ArrayList<>();
        for (int i = 1; i < numberList.get(1); i++) {
            String cellValue = dataFormatter.formatCellValue(secondRow.getCell(i));
            if (StringUtils.isNotBlank(cellValue)) {
                safeWorkList.add(cellValue);
            }
        }
        System.out.println(normalWorkList.toString());
        System.out.println(safeWorkList.toString());
        return ResponseUtil.success();
    }

    /*public static BaseResponse importExcel() {
        List<Map<String,Object>> list = new ArrayList<>();
        HSSFWorkbook workbook = null;
        try {
            // 读取Excel文件
            InputStream inputStream = new FileInputStream("D:/平安指数.xls");
            workbook = new HSSFWorkbook(inputStream);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 获取表单
        HSSFSheet hssfSheet = workbook.getSheetAt(0);
        if (hssfSheet == null) {
            return ResponseUtil.error("工作表为空");
        }
        DataFormatter dataFormatter = new DataFormatter();
        // 平安工作数量
        int safeWorkNumber = 2;
        // 基础工作数量
        int normalWorkNumber = 3;

        // 表头合并单元格数量
        List<Integer> numberList = new ArrayList<>();
        HSSFRow firstRow = hssfSheet.getRow(0);
        int temp = 1;
        for (int i = 0; i < firstRow.getLastCellNum(); i++) {
            String cellValue = dataFormatter.formatCellValue(firstRow.getCell(i + 1));
            if (StringUtils.isNotBlank(cellValue)) {
                numberList.add(temp);
                temp = 1;
            }else {
                temp += 1;
            }
        }
        System.out.println(numberList.toString());
        if (!Objects.equals(numberList.get(1) - 1, safeWorkNumber)) {
            return ResponseUtil.error("平安工作类型与建模不匹配");
        }
        if (!Objects.equals(numberList.size() - 3, normalWorkNumber)) {
            return ResponseUtil.error("基础工作类型与建模不匹配");
        }
        // 基础工作类型
        List<String> normalWorkList = new ArrayList<>();
        for (int i = safeWorkNumber + 1; i < (normalWorkNumber * 2) + safeWorkNumber; i++) {
            String cellValue = dataFormatter.formatCellValue(firstRow.getCell(i + 1));
            if (StringUtils.isNotBlank(cellValue)) {
                normalWorkList.add(cellValue);
            }
        }
        System.out.println(normalWorkList.toString());

        // 循环行
        for (int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
            HSSFRow hssfRow = hssfSheet.getRow(rowNum);
            if (hssfRow == null) {
                continue;
            }

            // 将单元格中的内容存入集合
            Map<String,Object> rowMap = new HashMap<>(20);
            for (int i = 0; i < hssfRow.getLastCellNum(); i++) {
                HSSFCell cell = hssfRow.getCell(i);
                if (cell == null) {
                    continue;
                }

                rowMap.put(i+"",dataFormatter.formatCellValue(cell));
            }
            list.add(rowMap);
        }
        System.out.println(list.toString());
        return ResponseUtil.success();
    }*/

}
