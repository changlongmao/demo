package com.example.demo.exception;

import com.example.demo.common.BaseResDTO;
import com.example.demo.enums.ErrorCode;
import com.example.demo.util.JsonUtils;
import com.example.demo.util.RequestTrack;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

/**
 * @author Deolin 2021-08-05
 */
//@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * HttpRequestMethodNotSupportedException 异常处理
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public BaseResDTO<Object> httpRequestMethodNotSupportedExceptionHandler(Exception e) {
        BaseResDTO<Object> br = new BaseResDTO<>();
        br.setError(ErrorCode.E000005);
        log.error(e.getMessage(), e);
        return br;
    }

    /**
     * HttpMediaTypeNotSupportedException异常处理(http请求媒体类型不支持)
     */
    @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
    public BaseResDTO<Object> httpMediaTypeNotSupportedExceptionHandler(HttpMediaTypeNotSupportedException e) {
        BaseResDTO<Object> br = new BaseResDTO<>();
        br.setError(ErrorCode.E000003);
        log.error(e.getMessage(), e);
        return br;
    }

    /**
     * HttpMessageNotReadableException异常处理(http 请求body为空)
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public BaseResDTO<Object> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e) {
        BaseResDTO<Object> br = new BaseResDTO<>();
        br.setError(ErrorCode.E000004);
        log.error(e.getMessage(), e);
        return br;
    }

    /**
     * 请求Body内字段没有通过注解校验（通过参数级@Valid 启用的参数校验）
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public BaseResDTO<Object> exceptionHandler(MethodArgumentNotValidException e) {
        List<InvalidDto> invalids = Lists.newArrayList();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            InvalidDto invalid = new InvalidDto();
            if (Boolean.FALSE.equals(fieldError.getRejectedValue())) {
            } else {
                invalid.setPath(fieldError.getField());
            }
            invalid.setValue(fieldError.getRejectedValue());
            invalid.setReason(fieldError.getDefaultMessage());
            invalids.add(invalid);
        }

        log.error("参数校验异常：{}", JsonUtils.toJsonPrettily(invalids));
        BaseResDTO<Object> br = new BaseResDTO<>();
        br.setError(ErrorCode.E000007.getCode(), invalids.get(0).getReason());
        br.setInvalids(invalids);
        return br;
    }

    /**
     * BaseException异常异常处理
     */
    @ExceptionHandler(value = BaseException.class)
    public BaseResDTO<Object> handleBaseException(BaseException e) {
        BaseResDTO<Object> br = new BaseResDTO<>();
        br.setError(e.getCode(), e.getMessage());
        log.error("Report BaseException", e);
        return br;
    }

    /**
     * 异常处理
     */
    @ExceptionHandler(value = Throwable.class)
    public BaseResDTO<Object> handleThrowable(Throwable e) {
        BaseResDTO<Object> br = new BaseResDTO<>();
        String errMsg = String.format("系统错误，请联系技术支持！（错误代码：%s）", RequestTrack.getCurrent().getInsignia());
        br.setError(ErrorCode.E000001.getCode(), errMsg);
        log.error("Report Throwable", e);
        return br;
    }

    @Data
    @Accessors(chain = true)
    public static class InvalidDto {

        private String path;

        private Object value;

        private String reason;

    }

}
