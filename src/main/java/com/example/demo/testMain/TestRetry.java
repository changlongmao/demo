package com.example.demo.testMain;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author ChangLF 2022-11-02
 */
@Slf4j
public class TestRetry {
    public static void main(String[] args) {

    }


    /**
     * 重试方法，失败后等待30秒后重试
     * @param retryNumber 失败后最大重试次数，不包括第一次
     * @param supplier 方法执行
     * @author ChangLF 2022/10/31 14:58
     * @return java.lang.Boolean 方法是否执行成功
     **/
    public static Boolean retryFail(int retryNumber, Supplier<Boolean> supplier) {
        int init = 0;
        Boolean flag = false;
        while (!flag && init <= retryNumber) {
            log.info("执行方法次数{}", init + 1);
            try {
                flag = supplier.get();
            } catch (Exception e) {
                init++;
                log.error("方法执行错误", e);
                try {
                    TimeUnit.SECONDS.sleep(30);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return flag;
    }

}
