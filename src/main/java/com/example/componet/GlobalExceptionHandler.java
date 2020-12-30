package com.example.componet;


import com.example.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

//@ControllerAdvice
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 捕获全局异常
     */
    @ExceptionHandler(Exception.class) //指定不管出现什么异常执行这个方法
    @ResponseBody //为了返回数据
    public Result error(Exception e) {
        //日志
        log.error("捕获的异常-----------{}",e.getMessage());
        return Result.error();
    }

    /**
     * 捕获参数校验异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody // 为了返回数据
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("参数校验出现问题:{},异常类型:{}", e.getMessage(), e.getClass());
        BindingResult bindingResult = e.getBindingResult();
        List<String> list = new ArrayList<>();
        bindingResult.getFieldErrors().forEach(fieldError -> {
            list.add(fieldError.getDefaultMessage());
        });
        return Result.error().message(StringUtils.join(list, ','));
    }

    /**
     * 捕获运行异常
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RuntimeException.class)
    public Result runtimeHandler(RuntimeException e){
        //日志输出
        log.error("运行时异常:--------------{}",e);
        return Result.error(e.getMessage());
    }

    /**
     * 捕获Shiro异常
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = ShiroException.class)
    public Result shiroHandler(ShiroException e){
        //日志输出
        log.error("Shiro异常:--------------{}",e);
        return Result.error("未通过身份验证，请登录");
    }

    /**
     * 捕获Assert异常
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Result assertHandler(IllegalArgumentException e){
        //日志输出
        log.error("Assert异常:--------------{}",e);
        return Result.error(e.getMessage());
    }


}
