package com.example.common;

public enum ResultCodeEnum {

    SUCCESS(3000, "操作成功！"),
    ERROR(3001, "操作失败！"),

    ERROR_BAD_REQUEST(3101, "请求参数不正确！"),
    ERROR_NOT_FIND_USER(3102, "用户不存在！"),
    ERROR_ALREADY_EXISTS_USER(3103, "用户已存在！"),
    ERROR_PIC_UPLOAD(3104,"部分图片上传失败！");

    private Integer code;
    private String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
