package com.liyisoft.hr.common.core.domain;

import lombok.Data;
import java.io.Serializable;

/**
 * 全局统一响应体对象
 * 
 * 用于封装对前端的所有接口响应数据，保证数据格式的统一。
 * 
 * @param <T> 响应的数据泛型
 */
@Data
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 响应状态码，例如 200 表示成功，500 表示服务器错误
     */
    private int code;

    /**
     * 响应的提示信息，通常用于在前端展示给用户
     */
    private String msg;

    /**
     * 响应的具体数据内容
     */
    private T data;

    /**
     * 构建无数据的成功响应
     * 
     * @param <T> 数据泛型
     * @return 成功响应结果
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 构建包含数据的成功响应
     * 
     * @param data 要返回的数据
     * @param <T> 数据泛型
     * @return 成功响应结果
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMsg("操作成功");
        result.setData(data);
        return result;
    }

    /**
     * 构建错误响应
     * 
     * @param msg 错误提示信息
     * @param <T> 数据泛型
     * @return 失败响应结果
     */
    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.setCode(500);
        result.setMsg(msg);
        return result;
    }
}
