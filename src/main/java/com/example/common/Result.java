package com.example.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一返回结果的类
 */
@Data
public class Result {

    private Boolean success;

    private Integer code;

    private String message;

    private Map<String, Object> data = new HashMap<>();

    // 构造方法私有化，禁止其他类访问
    private Result() {
    }

    /**
     * 成功
     */
    public static Result success() {
        Result r = new Result();
        r.setSuccess(true);
        r.setCode(ResultCodeEnum.SUCCESS.getCode());
        r.setMessage(ResultCodeEnum.SUCCESS.getMessage());
        return r;
    }

    /**
     * 失败
     */
    public static Result error() {
        Result r = new Result();
        r.setSuccess(false);
        r.setCode(ResultCodeEnum.ERROR.getCode());
        r.setMessage(ResultCodeEnum.ERROR.getMessage());
        return r;
    }

    /**
     * 失败
     */
    public static Result error(String error) {
        Result r = new Result();
        r.setSuccess(false);
        r.setCode(3001);
        r.setMessage(error);
        return r;
    }

    public static Result error(ResultCodeEnum resultCodeEnum) {
        Result r = new Result();
        r.setSuccess(false);
        r.setCode(resultCodeEnum.getCode());
        r.setMessage(resultCodeEnum.getMessage());
        return r;
    }


    // 下面都是被引用后可自定义值的方法
    public Result success(Boolean success) {
        this.setSuccess(success);
        return this;
    }

    public Result code(Integer code) {
        this.setCode(code);
        return this;
    }

    public Result message(String message) {
        this.setMessage(message);
        return this;
    }

    public Result data(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    public Result data(Map<String, Object> data) {
        this.setData(data);
        return this;
    }

}
