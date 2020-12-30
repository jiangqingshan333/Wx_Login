package com.example.service;

import com.example.common.Result;
import com.example.entity.UserData;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.vo.LoginVo;
import com.example.vo.PasswordVo;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * InnoDB free: 4096 kB 服务类
 * </p>
 *
 * @author jiangqingshan
 * @since 2020-12-27
 */
public interface UserDataService extends IService<UserData> {

    Result addUser(UserData userData);

    //Result userLogin(LoginVo loginVo);

    Result gaiPassword(PasswordVo passwordVo);

}
