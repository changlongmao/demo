package com.example.demo.exception;

import com.example.demo.common.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.stream.Collectors;

/**
 * @Author Chang
 * @Description 自定义全局异常拦截器，对异常统一处理
 * @Date 2020/10/22 16:17
 **/
@RestControllerAdvice
public class BusinessExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(BusinessExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public BaseResponse handleBusinessException(BusinessException e) {
        log.error(e.getMessage(), e);
        return ResponseUtil.error(e.getMessage(), e.getCode());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public BaseResponse handlerNoFoundException(NoHandlerFoundException e) {
        log.error(e.getMessage(), e);
        return ResponseUtil.error("路径不存在，请检查路径是否正确", HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public BaseResponse handleDuplicateKeyException(DuplicateKeyException e) {
        log.error(e.getMessage(), e);
        return ResponseUtil.error("数据库中已存在该记录", HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @ExceptionHandler(NumberFormatException.class)
    public BaseResponse handleNumberFormatException(NumberFormatException e) {
        log.error(e.getMessage(), e);
        return ResponseUtil.error("数字解析错误，参数需数字得文字", HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @ExceptionHandler(Exception.class)
    public BaseResponse handleException(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseUtil.error("未知异常，请联系管理员", HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * 请求Body内字段没有通过注解校验（通过参数级@Valid 启用的参数校验）
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public BaseResponse methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        String msg = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + fieldError.getDefaultMessage()).collect(Collectors.joining(","));
        return ResponseUtil.error("请求参数校验失败：" + msg, HttpStatus.BAD_REQUEST.value());
    }
}
