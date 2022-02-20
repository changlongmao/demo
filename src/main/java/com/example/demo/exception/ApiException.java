package com.example.demo.exception;

import com.example.demo.util.ApiResultUtil;
import jodd.util.StringUtil;

/**
 * @Author Chang
 * @Description 服务异常信息维护
 * @Date 2021/8/23 10:05
 **/
public class ApiException extends BaseException {

    /**
     *
     * @author Chang
     * @description 当前服务错误信息必须维护api-error.properties错误码后调用此方法, 若errCode没找到错误信息会报空指针异常，
     *              非当前系统维护的错误信息（如远程调用错误），可调用其父类BaseException维护msg抛出异常
     *              抛出异常后会被kernel依赖包中的GlobalExceptionHandler异常拦截器拦截并处理
     * @date 2021/8/23 11:04
     */
    public ApiException(String errCode, Object... args) {
        super(errCode, String.format(ApiResultUtil.getInstance().getErrorInfo(errCode), args));
    }

    /**
     * @Param: errCode
     * @Param: errMsg
     * @Author Chang
     * @Description 一般用此方法用于抛出非当前服务维护的异常信息，因存在想调用A方法一个%s替换字符串方式会调用此方法，故适配替换字符串调用方式
     * @Date 2021/8/24 9:26
     **/
    public ApiException(String errCode, String errMsg) {
        super(errCode, StringUtil.isEmpty(ApiResultUtil.getInstance().getErrorInfo(errCode)) ? errMsg : String.format(ApiResultUtil.getInstance().getErrorInfo(errCode), errMsg));
    }

}