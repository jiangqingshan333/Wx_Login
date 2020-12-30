package com.example.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.NonFinal;
import org.antlr.v4.runtime.misc.NotNull;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * InnoDB free: 4096 kB
 * </p>
 *
 * @author jiangqingshan
 * @since 2020-12-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_data")
public class UserData implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private String id;
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String userName;
    /**
     * 账号
     */
    @NotBlank(message = "账号不能为空")
    private String account;
    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String passWord;
    /**
     * 头像
     */
    private String photo;
    /**
     * 状态
     */
    //@NotBlank(message = "用户状态不能为空")该注释用于字符串
    private Integer status;
    /**
     * 登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:dd",timezone = "GMT+8")
    private Date modifyTime;


}
