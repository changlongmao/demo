package com.example.demo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 从资源文件中读取api的错误信息
 *
 * @author zhangsheng
 * @version 1.0
 */
public final class ApiResultUtil {
    private final Properties p = new Properties();
    private static ApiResultUtil instance;
    private static final Logger logger = LoggerFactory.getLogger(ApiResultUtil.class);

    private ApiResultUtil() {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("api-error.properties");
        try {
            p.load(inputStream);
        } catch (IOException e1) {
            logger.error("读取文件出错" + e1.getMessage());
        }
    }

    public static ApiResultUtil getInstance() {
        if (instance == null) {
            instance = new ApiResultUtil();
        }
        return instance;
    }

    public String getErrorInfo(String key) {
        return p.getProperty(key);
    }
}
