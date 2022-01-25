package com.example.demo.util;

/**
 * 异常类基类
 *
 * @author Spark
 */
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = -7940987991045402921L;

    private String code;

    public BaseException(String code, String desc) {
        super(desc);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
