package com.example.demo.entity;

import cn.hutool.core.exceptions.ExceptionUtil;
import org.springframework.http.HttpStatus;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 结果数据封装
 * 
 * @author ${paramConfig.author}
 * @version 1.0.0 ${today}
 */
@ApiModel(description = "结果数据封装")
public class Result<T> extends OverrideBeanMethods {
    private static final long serialVersionUID = -7857956965424828813L;

    @ApiModelProperty(value = "是否成功标识")
    private boolean flag = true;

    @ApiModelProperty(value = "成功或错误的编码")
    private Integer code;

    @ApiModelProperty(value = "返回的提示消息")
    private String msg = "调用成功";

    @ApiModelProperty(value = "返回的异常消息")
    private String exceptionMsg;

    @ApiModelProperty(value = "返回的数据对象")
    private T data;

    /**
     * 返回成功结果数据
     *
     * @return 成功结果数据
     */
    public static <T> Result<T> ok() {
        return buildOkResult(null, null);
    }

    /**
     * 返回成功结果数据
     *
     * @param data 返回数据
     * @return 成功结果数据
     */
    public static <T> Result<T> ok(T data) {
        return buildOkResult(null, data);
    }

    /**
     * 返回成功结果数据
     *
     * @param msg 成功消息
     * @param data 返回数据
     * @return 成功结果数据
     */
    public static <T> Result<T> ok(String msg, T data) {
        return buildOkResult(msg, data);
    }

    /**
     * 返回失败结果数据
     *
     * @return 失败结果数据
     */
    public static <T> Result<T> failed() {
        return buildFailedResult(null, null);
    }

    /**
     * 返回失败结果数据
     *
     * @param msg 失败消息
     * @return 失败结果数据
     */
    public static <T> Result<T> failed(String msg) {
        return buildFailedResult(msg, null);
    }

    /**
     * 返回失败结果数据
     *
     * @param msg 失败消息
     * @param data 返回数据
     * @return 失败结果数据
     */
    public static <T> Result<T> failed(String msg, T data) {
        return buildFailedResult(msg, data);
    }

    /**
     * 返回错误结果数据
     *
     * @param msg 错误消息
     * @return 错误结果数据
     */
    public static <T> Result<T> error(String msg) {
        return buildErrorResult(msg, null);
    }

    /**
     * 返回错误结果数据
     *
     * @param msg       错误消息
     * @param throwable 异常对象
     * @return 错误结果数据
     */
    public static <T> Result<T> error(String msg, Throwable throwable) {
        return buildErrorResult(msg, throwable);
    }

    /**
     * 返回成功或失败的结果数据
     *
     * @param isOk 是否成功
     * @return 结果数据
     */
    public static <T> Result<T> okOrFailed(boolean isOk) {
        if (isOk) {
            return Result.ok();
        } else {
            return Result.failed();
        }
    }

    /**
     * 创建成功结果
     *
     * @param msg  成功消息
     * @param data 返回数据
     * @return 结果数据
     */
    private static <T> Result<T> buildOkResult(String msg, T data) {
        Result<T> result = new Result<>();
        return result.setFlag(true).setCode(HttpStatus.OK.value()).setMsg(msg).setData(data);
    }

    /**
     * 创建失败结果
     *
     * @param msg  失败消息
     * @param data 返回数据
     * @return 结果数据
     */
    private static <T> Result<T> buildFailedResult(String msg, T data) {
        Result<T> result = new Result<>();
        return result.setFlag(false).setCode(HttpStatus.PRECONDITION_FAILED.value()).setMsg(msg).setData(data);
    }

    /**
     * 创建错误结果
     *
     * @param msg       错误消息
     * @param throwable 异常对象
     * @return 结果数据
     */
    private static <T> Result<T> buildErrorResult(String msg, Throwable throwable) {
        Result<T> result = new Result<>();
        return result.setFlag(false).setCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).setMsg(msg).setExceptionMsg(throwable);
    }

    /**
     * 设置异常消息
     *
     * @param throwable 异常对象
     */
    public Result<T> setExceptionMsg(Throwable throwable) {
        if (throwable != null) {
            this.exceptionMsg = throwable.getMessage();
        }
        return this;
    }

    /**
     * 获取是否成功标识
     *
     * @return 是否成功标识
     */
    public boolean getFlag() {
        return flag;
    }

    /**
     * 设置是否成功标识
     *
     * @param flag
     *          是否成功标识
     * @return 结果数据
     */
    public Result<T> setFlag(boolean flag) {
        this.flag = flag;
        return this;
    }

    /**
     * 获取成功或错误的编码
     *
     * @return 成功或错误的编码
     */
    public Integer getCode() {
        return code;
    }

    /**
     * 设置成功或错误的编码
     *
     * @param code
     *          成功或错误的编码
     * @return 结果数据
     */
    public Result<T> setCode(Integer code) {
        this.code = code;
        return this;
    }

    /**
     * 获取返回的提示消息
     *
     * @return 返回的提示消息
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 设置返回的提示消息
     *
     * @param msg
     *          返回的提示消息
     * @return 结果数据
     */
    public Result<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    /**
     * 获取返回的异常消息
     *
     * @return 返回的异常消息
     */
    public String getExceptionMsg() {
        return exceptionMsg;
    }

    /**
     * 设置返回的异常消息
     *
     * @param exceptionMsg
     *          返回的异常消息
     * @return 结果数据
     */
    public Result<T> setExceptionMsg(String exceptionMsg) {
        this.exceptionMsg = exceptionMsg;
        return this;
    }

    /**
     * 获取返回的数据对象
     *
     * @return 返回的数据对象
     */
    public T getData() {
        return data;
    }

    /**
     * 设置返回的数据对象
     *
     * @param data
     *          返回的数据对象
     * @return 结果数据
     */
    public Result<T> setData(T data) {
        this.data = data;
        return this;
    }
}