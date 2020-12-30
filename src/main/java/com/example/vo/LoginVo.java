package com.example.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author JiangQingShan
 * @date 2020/12/28 - 11:16
 */
@Data
public class LoginVo implements Serializable {
    /**
     * 账号
     */
    @NotBlank(message = "账号不能为空")
    private String account;
    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;
}
