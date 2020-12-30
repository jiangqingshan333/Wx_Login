package com.example.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author JiangQingShan
 * @date 2020/12/28 - 11:15
 */
@Data
public class UserDataDto implements Serializable {
    @NotBlank(message = "id不能为空")
    private Long id;

    @NotBlank(message = "用户名不能为空")
    private String userName;

    /**
     * 头像
     */
    private String photo;
}
