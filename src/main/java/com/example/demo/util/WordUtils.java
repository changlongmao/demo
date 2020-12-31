package com.example.demo.util;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;


/**
 * @ClassName: WordUtils
 * @Description:
 * @Author: Chang
 * @Date: 2020/12/29 09:01
 **/
public class WordUtils {
    
    private static final Logger log = LoggerFactory.getLogger(WordUtils.class);

    private static final String FILE_OUT_PUT_PATH = "D://";

    private static final String FONT_FAMILY = "宋体";//字体类型
    private static final int DOC_NAME = 20;//文档名称
    private static final int FONT_TITLE_1 = 18;//一级标题文字大小
    private static final int FONT_TITLE_2 = 10;//二级标题文字大小
    private static final int FONT_SIZE = 10;//正文字体大小
    private static final String HEAD_BG_COLOR = "ffff66";//表格标题背景颜色
    private static final int HR_HEIGHT = 300;//表格行高
    private static final int IMG_WIDTH = 600;//图片宽度
    private static final int IMG_HEIGHT = 400;//图片高度

    /**
     * 为段落添加内容
     *
     * @param p        当前段落
     * @param isBold   是否加粗
     * @param fontSize 字体大小
     * @param text     文本内容
     * @param isBreak  是否换行
     */
    public static void addText(XWPFParagraph p, String text, boolean isBold, int fontSize, String fontColor, boolean isBreak) {
        XWPFRun run = p.createRun();
        run.setFontSize(fontSize == 0 ? FONT_SIZE : fontSize);
        run.setText(text);
        run.setFontFamily(FONT_FAMILY);
        if (fontColor != null && !"".equals(fontColor))
            run.setColor(fontColor);
        run.setBold(isBold);
        if (isBreak)
            run.addBreak();
    }

    /**
     * 添加正文内容和颜色
     *
     * @param p
     * @param colors
     * @param content
     * @param isBreak
     */
    public static void addText(XWPFParagraph p, Map<String, String> colors, String content, boolean isBreak) {
        //关键词位置
        if (colors != null && !colors.isEmpty() && content != null && !"".equals(content)) {
            int curPos = 0;
            String curStr = content;
            for (String key : colors.keySet()) {
                String color = colors.get(key);
                Integer pos = curStr.indexOf(key);
                if (pos < 0) {
                    continue;
                }
                //添加文本
                WordUtils.addText(p, curStr.substring(curPos, pos), false, WordUtils.FONT_SIZE, "", false);
                //添加文本
                WordUtils.addText(p, curStr.substring(pos, (pos + key.length()) > curStr.length() ? curStr.length() : (pos + key.length())), true, WordUtils.FONT_SIZE, color, false);
                curStr = curStr.substring(pos + key.length());
            }
            if (curStr != null && !"".equals(curStr))
                WordUtils.addText(p, curStr, false, WordUtils.FONT_SIZE, "", isBreak);

        } else {
            WordUtils.addText(p, content, false, WordUtils.FONT_SIZE, "", isBreak);
        }
    }

    /**
     * 添加正文
     *
     * @param p
     * @param content
     * @param isBreak
     */
    public static void addText(XWPFParagraph p, String content, boolean isBreak) {
        WordUtils.addText(p, content, false, WordUtils.FONT_SIZE, "", isBreak);
    }

    /**
     * 文档添加表格
     *
     * @param p
     * @param
     * @param
     * @param dataset key:类型，value：类型的数据
     */
    public static void addTable(XWPFParagraph p, int w, String[][] dataset) {
        if (dataset.length == 0) {
            return;
        }
        int rows = dataset.length;
        int cols = dataset[0].length;
        //创建表格
        XWPFTable table = p.getDocument().createTable(rows, cols);

        CTTbl ttbl = table.getCTTbl();
        CTTblPr tblPr = ttbl.getTblPr() == null ? ttbl.addNewTblPr() : ttbl.getTblPr();
        CTTblWidth tblWidth = tblPr.isSetTblW() ? tblPr.getTblW() : tblPr.addNewTblW();
        CTJc cTJc = tblPr.addNewJc();
        cTJc.setVal(STJc.Enum.forString("center"));
        tblWidth.setW(new BigInteger(String.valueOf(w == 0 ? 8000 : w)));
        tblWidth.setType(STTblWidth.DXA);


        //设置表格行高
        for (XWPFTableRow row : table.getRows()) {
            row.setHeight(HR_HEIGHT);
        }
        //设置表格数据
        for (int i = 0; i < dataset.length; i++) {
            for (int j = 0; j < dataset[i].length; j++) {
                if (i == 0) {
                    //填充表头数据
                    WordUtils.addCell(table.getRow(i), j, dataset[i][j], WordUtils.HEAD_BG_COLOR, "", 9, true);
                } else {
                    WordUtils.addCell(table.getRow(i), j, dataset[i][j], "", "", 9, false);
                }
            }
        }

        //删除第一列
        for (XWPFTableRow r : table.getRows()) {
            r.removeCell(0);
        }

    }

    /**
     * 添加单元格
     *
     * @param r         当前行
     * @param index     列索引
     * @param text      文本内容
     * @param cellColor 单元格背景颜色
     * @param textColor 单元格文本颜色
     * @param fontSize  单元格文字大小
     * @param isBold    是否加粗
     */
    public static void addCell(XWPFTableRow r, int index, String text, String cellColor, String textColor, int fontSize, boolean isBold) {
        XWPFTableCell cell = r.getCell(index);
        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
        if (cellColor != null && !"".equals(cellColor))
            cell.setColor(cellColor);
        XWPFParagraph xp = cell.addParagraph();
        xp.setAlignment(ParagraphAlignment.CENTER);
        cell.removeParagraph(0);
        XWPFRun xpRun = xp.createRun();
        xpRun.setBold(isBold);
        xpRun.setText(text);
        xpRun.setFontSize(fontSize == 0 ? FONT_SIZE : fontSize);
        if (textColor != null && !"".equals(textColor))
            xpRun.setColor(textColor);
    }

    /**
     * 添加图片
     *
     * @param doc    文档对象
     * @param input  图片流
     * @param width  图片宽度
     * @param height 图片高度
     */
    public static void addImage(CustomXWPFDocument doc, InputStream input, int width, int height) {
        //创建空的段落
        XWPFParagraph p = doc.createParagraph();
        try {
            doc.addPictureData(input, XWPFDocument.PICTURE_TYPE_PNG);
            doc.createPicture(p, doc.getAllPictures().size() - 1, width, height, "");
        } catch (InvalidFormatException e) {

            e.printStackTrace();
        }
    }

    /**
     * 添加文档概括信息
     *
     * @param doc     文档对象
     * @param dataset 数据集
     */
    public static void addSummary(CustomXWPFDocument doc, Map<String, String> colors, Map<String, String> dataset) {
        if (dataset == null || dataset.isEmpty()) return;
        XWPFParagraph p = doc.createParagraph();
        p.setAlignment(ParagraphAlignment.LEFT);
        for (String key : dataset.keySet()) {
            String[] array = dataset.get(key).split("\n");
            addText(p, key + "：", true, FONT_SIZE, "", array.length > 1 ? true : false);
            for (String str : array) {
                StringBuilder sb = new StringBuilder("");
                if (array.length > 1) {
                    for (int j = 1; j < 4; j++) {//缩进两个字符
                        sb.append(" ");
                    }
                }
                sb.append(str);
                if (array.length > 1) addText(p, colors, sb.toString(), true);
                else addText(p, sb.toString(), true);
            }
        }
    }

    /**
     * 添加文档标题
     *
     * @param doc   文档对象
     * @param title 标题名称
     */
    public static void addTitle(CustomXWPFDocument doc, String title) {
        if (title == null || "".equals(title)) return;
        XWPFParagraph p = doc.createParagraph();
        p.setAlignment(ParagraphAlignment.CENTER);
        addText(p, title, true, DOC_NAME, "", false);
    }

    /**
     * 添加段落
     *
     * @param doc      当前文档
     * @param text     内容
     * @param isBold   是否加粗
     * @param isBreak  是否换行
     * @param position 对齐方式
     */
    public static XWPFParagraph addParagraph(CustomXWPFDocument doc, String text, boolean isBold, boolean isBreak, ParagraphAlignment position) {
        XWPFParagraph p = doc.createParagraph();
        p.setAlignment(position == null ? ParagraphAlignment.CENTER : position);
        if (text != null && !"".equals(text))
            addText(p, text, isBold, FONT_SIZE, "", isBreak);
        return p;
    }

    /**
     * 添加一级段落
     *
     * @param doc   当前文档对象
     * @param
     * @param index 段落索引
     */
    public static XWPFParagraph addParagraphLevel1(CustomXWPFDocument doc, String text, String index) {
        XWPFParagraph p = doc.createParagraph();
        p.setAlignment(ParagraphAlignment.LEFT);
        addText(p, index + "." + text, true, FONT_TITLE_1, "", false);
        return p;
    }

    /**
     * 添加二级段落
     *
     * @param doc   当前文档对象
     * @param
     * @param index 段落索引
     */
    public static XWPFParagraph addParagraphLevel2(CustomXWPFDocument doc, String text, String index) {
        XWPFParagraph p = doc.createParagraph();
        p.setAlignment(ParagraphAlignment.LEFT);
        addText(p, index + "." + text, true, FONT_TITLE_2, "", true);
        return p;
    }

    /**
     * @param doc     当前文档
     * @param name    二级标题名称
     * @param index   二级标题索引
     * @param content 文本内容
     * @param dataset 表格数据集
     */
    public static void addPart(CustomXWPFDocument doc, String name, String index, String content, Map<String, String> colors, Map<String, String[][]> dataset) {
        //添加二级段落
        XWPFParagraph para1 = WordUtils.addParagraphLevel2(doc, name, index);
        if (content != null && !"".equals(content)) {
            //添加文本内容
            WordUtils.addText(para1, colors, content, false);
        }
        if (dataset != null && dataset.size() > 0) {
            for (String key : dataset.keySet()) {
                //添加居中段落
                WordUtils.addParagraph(doc, key, false, false, ParagraphAlignment.CENTER);
                //添加表格
                WordUtils.addTable(para1, 8000, dataset.get(key));
            }
        }
    }

    /**
     * @Param: doc
     * @Param: img
     * @Author Chang
     * @Description 添加图片
     * @Date 2020/12/29 10:11
     * @Return void
     **/
    public static void addImgPart(CustomXWPFDocument doc, Map<String, InputStream> img) {
        if (img != null && !img.isEmpty()) {
            for (String key : img.keySet()) {
                WordUtils.addImage(doc, img.get(key), IMG_WIDTH, IMG_HEIGHT);
                WordUtils.addParagraph(doc, key, false, false, ParagraphAlignment.CENTER);
            }
        }
    }

    /**
     * @param doc      文档对象
     * @param fileName 文件名称
     */
    public static void saveDoc(CustomXWPFDocument doc, String fileName) {
        try {
            FileOutputStream out = new FileOutputStream(FILE_OUT_PUT_PATH + fileName);
            try {
                doc.write(out);
                out.close();
                log.info(FILE_OUT_PUT_PATH + fileName + "----------------------------创建成功");
            } catch (IOException e) {

                e.printStackTrace();
                log.info("------------------------------------" + e.getMessage());
            }
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //表格数据集
        Map<String, String[][]> dataset = new HashMap<>();
        String[][] table = {{"正面", "负面"}, {"121", "2323"}};
        dataset.put("这是一个表格", table);
        //文档概括数据集
        Map<String, String> summaryDataset = new HashMap<String, String>() {{
            put("监测时间", "2012年2月16日");
            put("监测范围", "新闻、博客、论坛、贴吧、、、、、等全面监测");
            put("监测方案", "监测方案一、监测方案二、监测方案二");
            put("内容摘要论坛内容", "2012年2月16日信息总量1124条，比2012年2月15日增加75条信息。" +
                    "\n2012年2月16日信息量最多的网站类型是新闻，占总数的35.7%" +
                    "\n2012年2月16日信息量最多的网站是zhidao.baidu.com");
        }};

        //创建word文档
        CustomXWPFDocument doc = new CustomXWPFDocument();
        //添加标题
        WordUtils.addTitle(doc, "智慧商情日报");
        //添加文档综述
        WordUtils.addSummary(doc, new HashMap() {{
            put("新闻", "cc0033");
            put("2012", "cc66ff");
        }}, summaryDataset);
        //添加一级段落
        WordUtils.addParagraphLevel1(doc, "信息综述", "1");
        WordUtils.addPart(doc, "七日传播趋势汇总", "1.1", "2012年2月16日，新闻网站上共监测到信息401条，比2012年2月15日增加64条信息。", null, dataset);
        WordUtils.addImgPart(doc, new HashMap() {{
            JFreeChartUtils jFreeChartUtils = new JFreeChartUtils();
            put("柱状图", jFreeChartUtils.makeBarChart());
            put("饼状图", jFreeChartUtils.makePieChart());
        }});

        //生成doc文件
        WordUtils.saveDoc(doc, "智慧商情日报.docx");
    }
}
