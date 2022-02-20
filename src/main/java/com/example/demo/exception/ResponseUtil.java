package com.example.demo.exception;


import com.example.demo.common.BaseResponse;

/**
 * Created by bzl on 2016-12-14 下午 1:16
 */
public class ResponseUtil {
	private final static short SUCCESS = 1; //成功
	private final static short ERROR = 0; //错误
	public static BaseResponse success(){
    	
        /**
         * 无返回数据的成功响应
         * Created by bzl on 2016-12-14 13:28:42
         * @param []
         * @return  com.fy.dp.bean.base.BaseResponse
         * @TODO
         */
        BaseResponse response = new BaseResponse();
        response.setSuccess(SUCCESS);
        return response;
    }

    public static BaseResponse success(Object data){
        /**
         * 有返回数据的成功响应
         * Created by bzl on 2016-12-14 13:32:35
         * @param [Data]
         * @return  com.fy.dp.bean.base.BaseResponse
         * @TODO
         */
        BaseResponse response = new BaseResponse();
        response.setSuccess(SUCCESS);
        response.setData(data);
        return response;
    }

    public static BaseResponse error(String message){
        /**
         * Created by bzl on 2016-12-14 13:30:45
         * @param [message] 错误信息
         * @return  com.fy.dp.bean.base.BaseResponse
         * @TODO
         */
        BaseResponse response = new BaseResponse();
        response.setSuccess(ERROR);
        response.setMessage(message);
        return response;
    }

    public static BaseResponse error(String message,Integer errorCode){
        /**
         * Created by bzl on 2017-01-20 09:32:21
         * @param [message, errorCode]
         * @return  com.fy.dp.bean.base.BaseResponse
         * @TODO
         */
        BaseResponse response = new BaseResponse();
        response.setSuccess(ERROR);
        response.setMessage(message);
        response.setErrorCode(errorCode);
        return response;
    }

}
