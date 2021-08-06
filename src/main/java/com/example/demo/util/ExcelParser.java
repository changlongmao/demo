//package com.example.demo.util;
//
//import com.alibaba.excel.EasyExcelFactory;
//import com.alibaba.excel.ExcelReader;
//import com.alibaba.excel.exception.ExcelAnalysisException;
//import com.alibaba.excel.read.builder.ExcelReaderBuilder;
//import com.alibaba.excel.read.metadata.ReadSheet;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.config.ConfigurableBeanFactory;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.Scope;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.function.BiFunction;
//import java.util.function.Function;
//import java.util.function.Supplier;
//
///**
// * 基于easyexcel封装的简单excel导入工具
// *
// * @author xuxu
// *
// *
// *
// *         DEMO:批量解析
// *
// *         try(InputStream in=file.getInputStream()) {
// *         List<ErrorReport> errorList = ExcelParser.getInstance()
// *         .fromSource(in)
// *         .batch(10) //每次解析10条
// *         .batchParse(CompanyAccount.class,(list<CompanyAccount>)->{
// *
// *         //每批数据的处理逻辑  需要返回 List<ErrorReport>
// *
// *         });
// *         }catch(IOException e) {
// *         return "fail";
// *         }
// */
//
///**
// *
// * DEMO:批量解析
// *
// *  try(InputStream in=file.getInputStream()) {
// *		List<ErrorReport> errorList = ExcelParser.getInstance()
// *		      .fromSource(in)
// *		      .batch(10) //每次解析10条
// *		      .batchParse(CompanyAccount.class,(list<CompanyAccount>)->{
// *
// *		          //每批数据的处理逻辑  需要返回 List<ErrorReport>
// *
// *              });
// *        }catch(IOException e) {
// *			return "fail";
// *        }
// *
// */
//
///**
// *
// * DEMO:一次性解析
// *
// * try(InputStream in=file.getInputStream()) { List<CompanyAccount>
// * list=ExcelParser.getInstance() .fromSource(in) .parse(CompanyAccount.class);
// *
// * //list 处理逻辑....
// *
// * }catch(IOException e) { return "fail"; }
// *
// */
//@Component
//@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//public class ExcelParser {
//
//    private Logger logger = LoggerFactory.getLogger(ExcelParser.class);
//
//    private static final int DEFAULT_TEMP_SIZE = 1000;
//
//    private ExcelReaderBuilder excelReaderBuilder;
//
//    private int batchSize = DEFAULT_TEMP_SIZE;
//
//    @Resource
//    private ApplicationContext applicationContext;
//
//    /**
//     * 非单例模式
//     *
//     * @return
//     */
//    public static ExcelParser getInstance() {
//        return SpringUtils.getApplicationContext().getBean(ExcelParser.class);
//    }
//
//    private ExcelParser() {
//    }
//
//    /**
//     * 传入解析的流
//     *
//     * @param in
//     * @return
//     */
//    public ExcelParser fromSource(InputStream in) {
//        excelReaderBuilder = EasyExcelFactory.read(in);
//        return this;
//    }
//
//    /**
//     * 传入批量性解析出excel中的行
//     *
//     * @return
//     */
//    public ExcelParser batch(int batchSize) {
//        this.batchSize = batchSize;
//        return this;
//    }
//
//    /**
//     * 一次性解析出excel中的全部行(调用此方法无需调用 batch方法 )
//     *
//     * @return
//     */
//    public <T> List<T> parse(Class<T> clazz) {
//        NormalDataAnalysisEventListener<T> listener = new NormalDataAnalysisEventListener<>();
//        excelReaderBuilder.registerReadListener(listener).head(clazz);
//        ExcelReader excelReader = excelReaderBuilder.build();
//        excelReader.readAll();
//        excelReader.finish();
//        try {
//            return listener.getResult();
//        } catch (Exception e) {
//            logger.error("ExcelParser 发生异常：{}", e);
//            throw new ExcelAnalysisException("ExcelParser 发生异常");
//        }
//    }
//
//    /**
//     * 批量解析
//     *
//     * 每批数据解析后的处理逻辑
//     *
//     * @param <T>   解析的数据类型
//     * @param <R>   错误报告数据类型
//     * @param clazz 解析的数据类型
//     * @param func
//     * @return
//     * @throws InterruptedException
//     */
//    public <T, R> List<R> batchParse(Class<T> clazz, Function<List<T>, List<R>> func) throws InterruptedException {
//        BigDataAnalysisEventListener<T> listener = new BigDataAnalysisEventListener<>(batchSize);
//        excelReaderBuilder.registerReadListener(listener).head(clazz);
//        ExcelReader excelReader = excelReaderBuilder.build();
//        Thread mainThread = Thread.currentThread();
//        // 异步线程解析excel
//        applicationContext.getBean(ThreadPoolTaskExecutor.class).submit(() -> {
//            try {
//                excelReader.readAll();
//                excelReader.finish();
//            } catch (Exception e) {
//                mainThread.interrupt();
//                logger.error("easyExcel 解析异常{}", e);
//            }
//        });
//        List<R> errorList = new ArrayList<>();
//        // 同步从解析结果中读取
//        while (!listener.isCompleted()) {
//            List<T> list = listener.getResult();
//            errorList.addAll(func.apply(list));
//        }
//        return errorList;
//    }
//
//    /**
//     *
//     * @param <T>          解析的数据类型
//     * @param <R>          中间返回结果
//     * @param <E>          最终返回结果
//     * @param clazz
//     * @param func         批量数据处理逻辑
//     * @param biFunction   中间结果转最终结果处理逻辑
//     * @param returnResult 最终结果
//     * @return
//     * @throws InterruptedException
//     */
//    public <T, R, E> E batchParse(Class<T> clazz, Function<List<T>, R> func, BiFunction<R, E, E> biFunction,
//            Supplier<E> returnResult) throws InterruptedException {
//        BigDataAnalysisEventListener<T> listener = new BigDataAnalysisEventListener<>(batchSize);
//        excelReaderBuilder.registerReadListener(listener).head(clazz);
//        ExcelReader excelReader = excelReaderBuilder.build();
//        // 异步线程解析excel
//        Thread mainThread = Thread.currentThread();
//        applicationContext.getBean(ThreadPoolTaskExecutor.class).submit(() -> {
//            try {
//                excelReader.readAll();
//                excelReader.finish();
//            } catch (Exception e) {
//                mainThread.interrupt();
//                logger.error("easyExcel 解析异常{}", e);
//            }
//        });
//        // 同步从解析结果中读取
//        E e = returnResult.get();
//        while (!listener.isCompleted()) {
//            List<T> list = listener.getResult();
//            e = biFunction.apply(func.apply(list), e);
//        }
//        return e;
//
//    }
//
//    /**
//     * 批量解析并封装
//     *
//     * 每批数据解析后的处理逻辑
//     *
//     * @param <T>   解析的数据类型
//     * @param <R>   错误报告数据类型
//     * @param clazz 解析的数据类型
//     * @param func
//     * @return
//     * @throws InterruptedException
//     */
//    public <T, R> List<R> batchParsePackage(Class<T> clazz, Function<List<T>, R> func) throws InterruptedException {
//        BigDataAnalysisEventListener<T> listener = new BigDataAnalysisEventListener<>(batchSize);
//        excelReaderBuilder.registerReadListener(listener).head(clazz);
//        ExcelReader excelReader = excelReaderBuilder.build();
//        Thread mainThread = Thread.currentThread();
//        // 异步线程解析excel
//        applicationContext.getBean(ThreadPoolTaskExecutor.class).submit(() -> {
//            try {
//                excelReader.readAll();
//                excelReader.finish();
//            } catch (Exception e) {
//                mainThread.interrupt();
//                logger.error("easyExcel 解析异常{}", e);
//            }
//        });
//        List<R> applyList = new ArrayList<>();
//        // 同步从解析结果中读取
//        while (!listener.isCompleted()) {
//            List<T> list = listener.getResult();
//            applyList.add(func.apply(list));
//        }
//        return applyList;
//    }
//
//    /**
//     * 指定sheetNo解析
//     * @param clazz
//     * @param in
//     * @param sheetNo
//     * @param <T>
//     * @return
//     */
//    public <T> List<T> parseBySheetNo(Class<T> clazz, InputStream in, Integer sheetNo) {
//        try {
//            NormalDataAnalysisEventListener<T> listener = new NormalDataAnalysisEventListener<>();
//            ExcelReader excelReader = EasyExcelFactory.read(in).build();
//            ReadSheet readSheet1 = EasyExcelFactory.readSheet(sheetNo).head(clazz).registerReadListener(listener)
//                    .build();
//            excelReader.read(readSheet1);
//            excelReader.finish();
//            return listener.getResult();
//        } catch (Exception e) {
//            logger.error("ExcelParser 发生异常：{}", e);
//            throw new ExcelAnalysisException("ExcelParser 发生异常");
//        }
//    }
//
//}
