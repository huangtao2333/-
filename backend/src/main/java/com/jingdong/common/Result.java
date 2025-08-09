package com.jingdong.common;

import lombok.Data;

/**
 * 统一响应结果类
 * 用于封装所有接口的返回数据格式
 * 
 * @author huangtao
 * @date 2025-08-09
 */
@Data
public class Result<T> {
    
    /**
     * 响应状态码
     */
    private Integer code;
    
    /**
     * 响应消息
     */
    private String message;
    
    /**
     * 响应数据
     */
    private T data;
    
    /**
     * 私有构造方法，防止外部直接实例化
     */
    private Result() {}
    
    /**
     * 成功响应（无数据）
     * 
     * @return 成功响应结果
     */
    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.code = 200;
        result.message = "操作成功";
        return result;
    }
    
    /**
     * 成功响应（带数据）
     * 
     * @param data 响应数据
     * @return 成功响应结果
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.code = 200;
        result.message = "操作成功";
        result.data = data;
        return result;
    }
    
    /**
     * 成功响应（自定义消息和数据）
     * 
     * @param message 响应消息
     * @param data 响应数据
     * @return 成功响应结果
     */
    public static <T> Result<T> success(String message, T data) {
        Result<T> result = new Result<>();
        result.code = 200;
        result.message = message;
        result.data = data;
        return result;
    }
    
    /**
     * 失败响应（默认消息）
     * 
     * @return 失败响应结果
     */
    public static <T> Result<T> error() {
        Result<T> result = new Result<>();
        result.code = 500;
        result.message = "操作失败";
        return result;
    }
    
    /**
     * 失败响应（自定义消息）
     * 
     * @param message 错误消息
     * @return 失败响应结果
     */
    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.code = 500;
        result.message = message;
        return result;
    }
    
    /**
     * 失败响应（自定义状态码和消息）
     * 
     * @param code 状态码
     * @param message 错误消息
     * @return 失败响应结果
     */
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.code = code;
        result.message = message;
        return result;
    }
}
