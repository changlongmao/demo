package com.example.demo.common;

import com.example.demo.enums.ErrorCode;
import com.example.demo.exception.GlobalExceptionHandler;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

/**
 * 消息体
 *
 * @author Spark
 */
public class BaseResDTO<E> implements Serializable {

    private static final long serialVersionUID = -1112042902806267142L;

    protected boolean flag = true;// 调用结果标志位 false:失败 true:成功

    protected String errorCode = ErrorCode.S000000.getCode();// 错误编码

    protected String errorMsg = ErrorCode.S000000.getDesc();// 错误信息

    /**
     * 参数校验不通过时，用于开发者自查的非法参数信息
     */
    @Getter
    @Setter
    protected List<GlobalExceptionHandler.InvalidDto> invalids;

    protected long pageCount = 0;// 分页数据的总数

    protected E data;// 返回的内容

    public boolean isFlag() {
        return ErrorCode.S000000.getCode().equals(errorCode);
    }

    public BaseResDTO() {
        isFlag();
    }

    public BaseResDTO(E data) {
        this.data = data;
        isFlag();
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }

    public long getPageCount() {
        return pageCount;
    }

    public void setPageCount(long pageCount) {
        this.pageCount = pageCount;
    }

    public void setError(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        isFlag();
    }

    public void setError(ErrorCode errorCode) {
        this.errorCode = errorCode.getCode();
        this.errorMsg = errorCode.getDesc();
        isFlag();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
