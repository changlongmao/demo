package com.example.demo.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 从资源文件中读取api的错误信息
 *
 * @author zhangsheng
 * @version 1.0
 */
@Slf4j
public final class ApiResultUtil {
    private final Properties p = new Properties();

    private static volatile ApiResultUtil singleInstance;

    private ApiResultUtil() {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("api-error.properties");
        try {
            p.load(inputStream);
        } catch (IOException e) {
            log.error("读取文件出错", e);
        }
    }

    public static ApiResultUtil getInstance() {
        if (singleInstance == null) {
            synchronized(ApiResultUtil.class){
                if(singleInstance == null){
                    singleInstance = new ApiResultUtil();
                }
            }
        }
        return singleInstance;
    }

    public String getErrorInfo(String key) {
        return p.getProperty(key);
    }
}
