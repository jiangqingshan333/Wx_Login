package com.example.shiro;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author JiangQingShan
 * @date 2020/12/27 - 18:30
 */
@Data
public class AccountProFile implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String userName;

    private String account;

    private String photo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:dd",timezone = "GMT+8")
    private LocalDateTime modifyTime;
}
