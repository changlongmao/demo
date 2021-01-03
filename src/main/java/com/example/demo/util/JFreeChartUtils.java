package com.example.demo.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import com.example.demo.entity.OlapDataResponse;
import org.jfree.chart.*;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtils;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;

/**
 * @ClassName: JFreeChartTest
 * @Description:
 * @Author: Chang
 * @Date: 2020/12/29 09:09
 **/
public class JFreeChartUtils {

    public static final Logger log = LoggerFactory.getLogger(JFreeChartUtils.class);

    private static Font FONT = new Font("宋体", Font.PLAIN, 10);
    private static Font FONT_BOLD = new Font("宋体", Font.BOLD + Font.PLAIN, 20);
    public static Color[] CHART_COLORS = {
            new Color(31, 129, 188), new Color(92, 92, 97), new Color(144, 237, 125), new Color(255, 188, 117),
            new Color(153, 158, 255), new Color(255, 117, 153), new Color(253, 236, 109), new Color(128, 133, 232),
            new Color(158, 90, 102), new Color(255, 204, 102)};//颜色

    static {
        setChartTheme();
    }

    /**
     * 中文主题样式 解决乱码
     */
    public static void setChartTheme() {
        // 设置中文主题样式 解决乱码
        StandardChartTheme chartTheme = new StandardChartTheme("CN");
        // 设置标题字体，加粗
        chartTheme.setExtraLargeFont(FONT_BOLD);
        // 设置图例的字体
        chartTheme.setRegularFont(FONT);
        // 设置轴向的字体
        chartTheme.setLargeFont(FONT);

        chartTheme.setSmallFont(FONT);
        chartTheme.setTitlePaint(new Color(51, 51, 51));
        chartTheme.setSubtitlePaint(new Color(85, 85, 85));

        chartTheme.setLegendBackgroundPaint(Color.WHITE);// 设置标注
        chartTheme.setLegendItemPaint(Color.BLACK);//
        chartTheme.setChartBackgroundPaint(Color.WHITE);
        // 绘制颜色绘制颜色.轮廓供应商
        // paintSequence,outlinePaintSequence,strokeSequence,outlineStrokeSequence,shapeSequence

        Paint[] OUTLINE_PAINT_SEQUENCE = new Paint[]{Color.WHITE};
        // 绘制器颜色源
        DefaultDrawingSupplier drawingSupplier = new DefaultDrawingSupplier(CHART_COLORS, CHART_COLORS, OUTLINE_PAINT_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE, DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE);
        chartTheme.setDrawingSupplier(drawingSupplier);

        chartTheme.setPlotBackgroundPaint(Color.WHITE);// 绘制区域
        chartTheme.setPlotOutlinePaint(Color.WHITE);// 绘制区域外边框
        chartTheme.setLabelLinkPaint(new Color(8, 55, 114));// 链接标签颜色
        chartTheme.setLabelLinkStyle(PieLabelLinkStyle.CUBIC_CURVE);

        chartTheme.setAxisOffset(new RectangleInsets(5, 12, 5, 12));
        chartTheme.setDomainGridlinePaint(new Color(192, 208, 224));// X坐标轴垂直网格颜色
        chartTheme.setRangeGridlinePaint(new Color(192, 192, 192));// Y坐标轴水平网格颜色

        chartTheme.setBaselinePaint(Color.WHITE);
        chartTheme.setCrosshairPaint(Color.BLUE);// 不确定含义
        chartTheme.setAxisLabelPaint(new Color(51, 51, 51));// 坐标轴标题文字颜色
        chartTheme.setTickLabelPaint(new Color(67, 67, 72));// 刻度数字
        chartTheme.setBarPainter(new StandardBarPainter());// 设置柱状图渲染
        chartTheme.setXYBarPainter(new StandardXYBarPainter());// XYBar 渲染

        chartTheme.setItemLabelPaint(Color.black);
        chartTheme.setThermometerPaint(Color.white);// 温度计

        ChartFactory.setChartTheme(chartTheme);
    }

    /**
     * 生成折线图
     */
    public InputStream makeLineAndShapeChart(OlapDataResponse olapDataResponse) {
        List<Object> series = olapDataResponse.getSeries();
        if (!(series.get(0) instanceof List)) return null;
        int size = ((List<?>) series.get(0)).size();
        double[][] data = new double[series.size()][size];
        for (int i = 0; i < series.size(); i++) {

            List<Object> objects = (List<Object>) series.get(i);
            double[] doubleArray = new double[objects.size()];
            for (int i1 = 0; i1 < objects.size(); i1++) {
                doubleArray[i1] = Double.valueOf(objects.get(i1).toString());
            }
            data[i] = doubleArray;
        }
//        double[][] data = new double[][]{{672, 766, 223, 540, 126},
//                {325, 521, 210, 340, 106}, {332, 256, 523, 240, 526}};
        String[] rowKeys = {"苹果", "梨子", "葡萄"};
        String[] columnKeys = {"北京", "上海", "广州", "成都", "深圳"};
        CategoryDataset dataset = getBarData(data, rowKeys, columnKeys);
        return createTimeXYChar("折线图", "x轴", "y轴", dataset, "lineAndShap.jpg");
    }

    /**
     * 生成分组的柱状图
     */
    public InputStream makeBarGroupChart() {
        double[][] data = new double[][]{{672, 766, 223, 540, 126},
                {325, 521, 210, 340, 106}, {332, 256, 523, 240, 526}};
        String[] rowKeys = {"苹果", "梨子", "葡萄"};
        String[] columnKeys = {"北京", "上海", "广州", "成都", "深圳"};
        CategoryDataset dataset = getBarData(data, rowKeys, columnKeys);
        return createBarChart(dataset, "x坐标", "y坐标", "柱状图", "barGroup.png");
    }

    /**
     * 生成柱状图
     */
    public InputStream makeBarChart() {
//        List<Object> series = olapDataResponse.getSeries();
//        if (!(series.get(0) instanceof List)) return null;
//        int size = ((List<?>) series.get(0)).size();
//        double[][] data = new double[series.size()][size];
//        for (int i = 0; i < series.size(); i++) {
//
//            List<Object> objects = (List<Object>) series.get(i);
//            double[] doubleArray = new double[objects.size()];
//            for (int i1 = 0; i1 < objects.size(); i1++) {
//                doubleArray[i1] = Double.valueOf(objects.get(i1).toString());
//            }
//            data[i] = doubleArray;
//        }

        double[][] data = new double[][]{{672, 766, 223, 540, 126}};
        String[] rowKeys = {"苹果"};
        String[] columnKeys = {"北京", "上海", "广州", "成都", "深圳"};
        CategoryDataset dataset = getBarData(data, rowKeys, columnKeys);
        return createBarChart(dataset, "x坐标", "y坐标", "柱状图", "bar.png");
    }

    /**
     * 生成堆栈柱状图
     */
    public InputStream makeStackedBarChart() {
        double[][] data = new double[][]{{0.21, 0.66, 0.23, 0.40, 0.26},
                {0.25, 0.21, 0.10, 0.40, 0.16}};
        String[] rowKeys = {"苹果", "梨子"};
        String[] columnKeys = {"北京", "上海", "广州", "成都", "深圳"};
        CategoryDataset dataset = getBarData(data, rowKeys, columnKeys);
        return createStackedBarChart(dataset, "x坐标", "y坐标", "柱状图", "stsckedBar.png");
    }

    /**
     * 生成饼状图
     */
    public InputStream makePieChart() {
        double[] data = {9, 91};
        String[] keys = {"失败率", "成功率"};

        return createValidityComparePimChar(getDataPieSetByUtil(data, keys), "饼状图",
                "pie2.png", keys);
    }

    // 柱状图,折线图 数据集
    private CategoryDataset getBarData(double[][] data, String[] rowKeys, String[] columnKeys) {
        return DatasetUtils.createCategoryDataset(rowKeys, columnKeys, data);
    }

    // 饼状图 数据集
    private PieDataset getDataPieSetByUtil(double[] data, String[] dataDescription) {

        if (data != null && dataDescription != null) {
            if (data.length == dataDescription.length) {
                DefaultPieDataset dataset = new DefaultPieDataset();
                for (int i = 0; i < data.length; i++) {
                    dataset.setValue(dataDescription[i], data[i]);
                }
                return dataset;
            }
        }

        return null;
    }

    /**
     * 柱状图
     *
     * @param dataset    数据集
     * @param xName      x轴的说明（如种类，时间等）
     * @param yName      y轴的说明（如速度，时间等）
     * @param chartTitle 图标题
     * @param charName   生成图片的名字
     * @return
     */
    private InputStream createBarChart(CategoryDataset dataset, String xName,
                                 String yName, String chartTitle, String charName) {
        JFreeChart chart = ChartFactory.createBarChart(chartTitle, // 图表标题
                xName, // 目录轴的显示标签
                yName, // 数值轴的显示标签
                dataset, // 数据集
                PlotOrientation.VERTICAL, // 图表方向：水平、垂直
                true, // 是否显示图例(对于简单的柱状图必须是false)
                false, // 是否生成工具
                false // 是否生成URL链接
        );
        Font labelFont = new Font("SansSerif", Font.PLAIN, 12);
        /*
         * VALUE_TEXT_ANTIALIAS_OFF表示将文字的抗锯齿关闭,
         * 使用的关闭抗锯齿后，字体尽量选择12到14号的宋体字,这样文字最清晰好看
         */
        // chart.getRenderingHints().put(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        chart.setTextAntiAlias(false);
        chart.setBackgroundPaint(Color.white);
        // create plot  获取图表区域对象
        CategoryPlot plot = chart.getCategoryPlot();
        // 设置横虚线可见
        plot.setRangeGridlinesVisible(true);
        // 虚线色彩
        plot.setRangeGridlinePaint(Color.gray);

        // 数据轴精度
        NumberAxis vn = (NumberAxis) plot.getRangeAxis();
        // vn.setAutoRangeIncludesZero(true);
        DecimalFormat df = new DecimalFormat("#0.00");
        vn.setNumberFormatOverride(df); // 数据轴数据标签的显示格式
        // x轴设置
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setLabelFont(labelFont);// 轴标题
        domainAxis.setTickLabelFont(labelFont);// 轴数值

        // Lable（Math.PI/3.0）度倾斜
        // domainAxis.setCategoryLabelPositions(CategoryLabelPositions
        // .createUpRotationLabelPositions(Math.PI / 3.0));

        domainAxis.setMaximumCategoryLabelWidthRatio(0.6f);// 横轴上的 Lable 是否完整显示

        // 设置距离图片左端距离
        domainAxis.setLowerMargin(0.1);
        // 设置距离图片右端距离
        domainAxis.setUpperMargin(0.1);
        // 设置 columnKey 是否间隔显示
        // domainAxis.setSkipCategoryLabelsToFit(true);

        plot.setDomainAxis(domainAxis);
        // 设置柱图背景色（注意，系统取色的时候要使用16位的模式来查看颜色编码，这样比较准确）
        plot.setBackgroundPaint(new Color(255, 255, 204));

        // y轴设置
        ValueAxis rangeAxis = plot.getRangeAxis();
        rangeAxis.setLabelFont(labelFont);
        rangeAxis.setTickLabelFont(labelFont);
        // 设置最高的一个 Item 与图片顶端的距离
        rangeAxis.setUpperMargin(0.15);
        // 设置最低的一个 Item 与图片底端的距离
        rangeAxis.setLowerMargin(0.15);
        plot.setRangeAxis(rangeAxis);

        BarRenderer renderer = new BarRenderer();
        // 设置柱子宽度
        renderer.setMaximumBarWidth(0.05);
        // 设置柱子高度
        renderer.setMinimumBarLength(0.2);
        // 设置柱子边框颜色
        renderer.setDefaultOutlinePaint(Color.BLACK);
        //renderer.setBaseOutlinePaint(Color.BLACK);
        // 设置柱子边框可见
        renderer.setDrawBarOutline(true);

        // // 设置柱的颜色
        renderer.setSeriesPaint(0, new Color(204, 255, 255));
        renderer.setSeriesPaint(1, new Color(153, 204, 255));
        renderer.setSeriesPaint(2, new Color(51, 204, 204));

        // 设置每个地区所包含的平行柱的之间距离
        renderer.setItemMargin(0.0);

        // 显示每个柱的数值，并修改该数值的字体属性
        renderer.setIncludeBaseInRange(true);
        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setDefaultItemLabelsVisible(true);
        //renderer
        //		.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        //renderer.setBaseItemLabelsVisible(true);

        plot.setRenderer(renderer);
        // 设置柱的透明度
        plot.setForegroundAlpha(1.0f);

        return getChartStream(chart, 500, 500);
    }

    /**
     * 横向图
     *
     * @param dataset    数据集
     * @param xName      x轴的说明（如种类，时间等）
     * @param yName      y轴的说明（如速度，时间等）
     * @param chartTitle 图标题
     * @param charName   生成图片的名字
     * @return
     */
    private InputStream createHorizontalBarChart(CategoryDataset dataset,
                                           String xName, String yName, String chartTitle, String charName) {
        JFreeChart chart = ChartFactory.createBarChart(chartTitle, // 图表标题
                xName, // 目录轴的显示标签
                yName, // 数值轴的显示标签
                dataset, // 数据集
                PlotOrientation.VERTICAL, // 图表方向：水平、垂直
                true, // 是否显示图例(对于简单的柱状图必须是false)
                false, // 是否生成工具
                false // 是否生成URL链接
        );

        CategoryPlot plot = chart.getCategoryPlot();
        // 数据轴精度
        NumberAxis vn = (NumberAxis) plot.getRangeAxis();
        // 设置刻度必须从0开始
        // vn.setAutoRangeIncludesZero(true);
        DecimalFormat df = new DecimalFormat("#0.00");
        vn.setNumberFormatOverride(df); // 数据轴数据标签的显示格式

        CategoryAxis domainAxis = plot.getDomainAxis();

        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45); // 横轴上的
        // Lable
        Font labelFont = new Font("SansSerif", Font.TRUETYPE_FONT, 12);

        domainAxis.setLabelFont(labelFont);// 轴标题
        domainAxis.setTickLabelFont(labelFont);// 轴数值

        domainAxis.setMaximumCategoryLabelWidthRatio(0.8f);// 横轴上的 Lable 是否完整显示
        // domainAxis.setVerticalCategoryLabels(false);
        plot.setDomainAxis(domainAxis);

        ValueAxis rangeAxis = plot.getRangeAxis();
        // 设置最高的一个 Item 与图片顶端的距离
        rangeAxis.setUpperMargin(0.15);
        // 设置最低的一个 Item 与图片底端的距离
        rangeAxis.setLowerMargin(0.15);
        plot.setRangeAxis(rangeAxis);
        BarRenderer renderer = new BarRenderer();
        // 设置柱子宽度
        renderer.setMaximumBarWidth(0.03);
        // 设置柱子高度
        renderer.setMinimumBarLength(30);

        renderer.setDefaultOutlinePaint(Color.BLACK);

        // 设置柱的颜色
        renderer.setSeriesPaint(0, Color.GREEN);
        renderer.setSeriesPaint(1, new Color(0, 0, 255));
        // 设置每个地区所包含的平行柱的之间距离
        renderer.setItemMargin(0.5);
        // 显示每个柱的数值，并修改该数值的字体属性
        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        // 设置柱的数值可见
        renderer.setDefaultItemLabelsVisible(true);

        plot.setRenderer(renderer);
        // 设置柱的透明度
        plot.setForegroundAlpha(0.6f);

        return getChartStream(chart, 500, 500);
    }

    /**
     * 饼状图
     *
     * @param dataset    数据集
     * @param chartTitle 图标题
     * @param charName   生成图的名字
     * @param pieKeys    分饼的名字集
     * @return
     */
    private InputStream createValidityComparePimChar(PieDataset dataset,
                                               String chartTitle, String charName, String[] pieKeys) {
        JFreeChart chart = ChartFactory.createPieChart3D(chartTitle, // chart
                // title
                dataset,// data
                true,// include legend
                true, false);

        // 使下说明标签字体清晰,去锯齿类似于
        // chart.getRenderingHints().put(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);的效果
        chart.setTextAntiAlias(false);
        // 图片背景色
        chart.setBackgroundPaint(Color.white);
        // 设置图标题的字体重新设置title
        Font font = new Font("隶书", Font.BOLD, 25);
        TextTitle title = new TextTitle(chartTitle);
        title.setFont(font);
        chart.setTitle(title);

        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        // 图片中显示百分比:默认方式

        // 指定饼图轮廓线的颜色
        // plot.setBaseSectionOutlinePaint(Color.BLACK);
        // plot.setBaseSectionPaint(Color.BLACK);

        // 设置无数据时的信息
        plot.setNoDataMessage("无对应的数据，请重新查询。");

        // 设置无数据时的信息显示颜色
        plot.setNoDataMessagePaint(Color.red);

        // 图片中显示百分比:自定义方式，{0} 表示选项， {1} 表示数值， {2} 表示所占比例 ,小数点后两位
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
                "{0}={1}({2})", NumberFormat.getNumberInstance(),
                new DecimalFormat("0.00%")));
        // 图例显示百分比:自定义方式， {0} 表示选项， {1} 表示数值， {2} 表示所占比例
        plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator(
                "{0}={1}({2})"));

        plot.setLabelFont(new Font("SansSerif", Font.TRUETYPE_FONT, 12));

        // 指定图片的透明度(0.0-1.0)
        plot.setForegroundAlpha(0.65f);
        // 指定显示的饼图上圆形(false)还椭圆形(true)
        plot.setCircular(false, true);

        // 设置第一个 饼块section 的开始位置，默认是12点钟方向
        plot.setStartAngle(90);

        // // 设置分饼颜色
        plot.setSectionPaint(pieKeys[0], new Color(244, 194, 144));
        plot.setSectionPaint(pieKeys[1], new Color(144, 233, 144));

        return getChartStream(chart, 500, 500);

    }

    /**
     * 判断文件夹是否存在，如果不存在则新建
     *
     * @param chartPath
     */
    private void isChartPathExist(String chartPath) {
        File file = new File(chartPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 折线图
     *
     * @param chartTitle
     * @param x
     * @param y
     * @param xyDataset
     * @param charName
     * @return
     */
    private InputStream createTimeXYChar(String chartTitle, String x, String y,
                                   CategoryDataset xyDataset, String charName) {

        JFreeChart chart = ChartFactory.createLineChart(chartTitle, x, y,
                xyDataset, PlotOrientation.VERTICAL, true, true, false);

        chart.setTextAntiAlias(false);
        chart.setBackgroundPaint(Color.WHITE);
        // 设置图标题的字体重新设置title
        Font font = new Font("隶书", Font.BOLD, 25);
        TextTitle title = new TextTitle(chartTitle);
        title.setFont(font);
        chart.setTitle(title);
        // 设置面板字体
        Font labelFont = new Font("SansSerif", Font.TRUETYPE_FONT, 12);

        chart.setBackgroundPaint(Color.WHITE);

        CategoryPlot categoryplot = (CategoryPlot) chart.getPlot();
        // x轴 // 分类轴网格是否可见
        categoryplot.setDomainGridlinesVisible(true);
        // y轴 //数据轴网格是否可见
        categoryplot.setRangeGridlinesVisible(true);

        categoryplot.setRangeGridlinePaint(Color.WHITE);// 虚线色彩

        categoryplot.setDomainGridlinePaint(Color.WHITE);// 虚线色彩

        categoryplot.setBackgroundPaint(Color.lightGray);

        // 设置轴和面板之间的距离
        // categoryplot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D));

        CategoryAxis domainAxis = categoryplot.getDomainAxis();

        domainAxis.setLabelFont(labelFont);// 轴标题
        domainAxis.setTickLabelFont(labelFont);// 轴数值

        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45); // 横轴上的
        // Lable
        // 45度倾斜
        // 设置距离图片左端距离
        domainAxis.setLowerMargin(0.0);
        // 设置距离图片右端距离
        domainAxis.setUpperMargin(0.0);

        NumberAxis numberaxis = (NumberAxis) categoryplot.getRangeAxis();
        numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        numberaxis.setAutoRangeIncludesZero(true);

        // 获得renderer 注意这里是下嗍造型到lineandshaperenderer！！
        LineAndShapeRenderer lineandshaperenderer = (LineAndShapeRenderer) categoryplot
                .getRenderer();

        lineandshaperenderer.setDefaultLinesVisible(true); // series 点（即数据点）可见
        lineandshaperenderer.setDefaultLinesVisible(true); // series 点（即数据点）间有连线可见

        // 显示折点数据
        // lineandshaperenderer.setBaseItemLabelGenerator(new
        // StandardCategoryItemLabelGenerator());
        // lineandshaperenderer.setBaseItemLabelsVisible(true);

        return getChartStream(chart, 500, 510);

    }

    /**
     * 堆栈柱状图
     *
     * @param dataset
     * @param xName
     * @param yName
     * @param chartTitle
     * @param charName
     * @return
     */
    private InputStream createStackedBarChart(CategoryDataset dataset, String xName,
                                        String yName, String chartTitle, String charName) {
        // 1:得到 CategoryDataset

        // 2:JFreeChart对象
        JFreeChart chart = ChartFactory.createStackedBarChart(chartTitle, // 图表标题
                xName, // 目录轴的显示标签
                yName, // 数值轴的显示标签
                dataset, // 数据集
                PlotOrientation.VERTICAL, // 图表方向：水平、垂直
                true, // 是否显示图例(对于简单的柱状图必须是false)
                false, // 是否生成工具
                false // 是否生成URL链接
        );
        // 图例字体清晰
        chart.setTextAntiAlias(false);

        chart.setBackgroundPaint(Color.WHITE);

        // 2 ．2 主标题对象 主标题对象是 TextTitle 类型
        chart
                .setTitle(new TextTitle(chartTitle, new Font("隶书", Font.BOLD,
                        25)));
        // 2 ．2.1:设置中文
        // x,y轴坐标字体
        Font labelFont = new Font("SansSerif", Font.TRUETYPE_FONT, 12);

        // 2 ．3 Plot 对象 Plot 对象是图形的绘制结构对象
        CategoryPlot plot = chart.getCategoryPlot();

        // 设置横虚线可见
        plot.setRangeGridlinesVisible(true);
        // 虚线色彩
        plot.setRangeGridlinePaint(Color.gray);

        // 数据轴精度
        NumberAxis vn = (NumberAxis) plot.getRangeAxis();
        // 设置最大值是1
        vn.setUpperBound(1);
        // 设置数据轴坐标从0开始
        // vn.setAutoRangeIncludesZero(true);
        // 数据显示格式是百分比
        DecimalFormat df = new DecimalFormat("0.00%");
        vn.setNumberFormatOverride(df); // 数据轴数据标签的显示格式
        // DomainAxis （区域轴，相当于 x 轴）， RangeAxis （范围轴，相当于 y 轴）
        CategoryAxis domainAxis = plot.getDomainAxis();

        domainAxis.setLabelFont(labelFont);// 轴标题
        domainAxis.setTickLabelFont(labelFont);// 轴数值

        // x轴坐标太长，建议设置倾斜，如下两种方式选其一，两种效果相同
        // 倾斜（1）横轴上的 Lable 45度倾斜
        // domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        // 倾斜（2）Lable（Math.PI 3.0）度倾斜
        // domainAxis.setCategoryLabelPositions(CategoryLabelPositions
        // .createUpRotationLabelPositions(Math.PI / 3.0));

        domainAxis.setMaximumCategoryLabelWidthRatio(0.6f);// 横轴上的 Lable 是否完整显示

        plot.setDomainAxis(domainAxis);

        // y轴设置
        ValueAxis rangeAxis = plot.getRangeAxis();
        rangeAxis.setLabelFont(labelFont);
        rangeAxis.setTickLabelFont(labelFont);
        // 设置最高的一个 Item 与图片顶端的距离
        rangeAxis.setUpperMargin(0.15);
        // 设置最低的一个 Item 与图片底端的距离
        rangeAxis.setLowerMargin(0.15);
        plot.setRangeAxis(rangeAxis);

        // Renderer 对象是图形的绘制单元
        StackedBarRenderer renderer = new StackedBarRenderer();
        // 设置柱子宽度
        renderer.setMaximumBarWidth(0.05);
        // 设置柱子高度
        renderer.setMinimumBarLength(0.1);
        // 设置柱的边框颜色
        renderer.setDefaultOutlinePaint(Color.BLACK);
        // 设置柱的边框可见
        renderer.setDrawBarOutline(true);

        // // 设置柱的颜色(可设定也可默认)
        renderer.setSeriesPaint(0, new Color(204, 255, 204));
        renderer.setSeriesPaint(1, new Color(255, 204, 153));

        // 设置每个地区所包含的平行柱的之间距离
        renderer.setItemMargin(0.4);

        plot.setRenderer(renderer);
        // 设置柱的透明度(如果是3D的必须设置才能达到立体效果，如果是2D的设置则使颜色变淡)
        // plot.setForegroundAlpha(0.65f);

        return getChartStream(chart, 500, 500);
    }

    private InputStream getChartStream(JFreeChart jfreechart, int weight, int height) {
        InputStream input = null;
        try {
            BufferedImage bufferedImage = jfreechart.createBufferedImage(weight, height);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", os);
            input = new ByteArrayInputStream(os.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            log.info("------------------------------------" + e.getMessage());
        }
        return input;

    }
}
