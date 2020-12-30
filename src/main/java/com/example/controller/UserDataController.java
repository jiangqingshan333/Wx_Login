package com.example.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.common.Result;
import com.example.config.ConstantCode;
import com.example.entity.UserData;
import com.example.service.UserDataService;
import com.example.util.AESUtil;
import com.example.util.JwtUtils;
import com.example.vo.LoginVo;
import com.example.vo.PasswordVo;
import io.jsonwebtoken.lang.Assert;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * InnoDB free: 4096 kB 前端控制器
 * </p>
 *
 * @author jiangqingshan
 * @since 2020-12-27
 */
@RestController
@RequestMapping("/user-data")
public class UserDataController {
    @Autowired
    private UserDataService userDataService;

    @Autowired
    private JwtUtils jwtUtils;


    /**
     * 注册
     * @param
     * @return
     */
    @RequiresAuthentication//需要进行登录才能访问该接口
    @PostMapping("/addUser")
    public Result addUser(@Validated @RequestBody UserData userData){
        return userDataService.addUser(userData);
    }

    /**
     * 登录
     * @param
     * @param
     * @return
     */
    @PostMapping("/userLogin")
    public Result userLogin(@Validated @RequestBody LoginVo loginVo,HttpServletResponse response){
        UserData account = userDataService.getOne(new QueryWrapper<UserData>().eq("account", loginVo.getAccount()));
        Assert.notNull(account,"该用户不存在");
        String jaMi = AESUtil.encrypt(loginVo.getPassword(), ConstantCode.ORIGINAL_KEY, ConstantCode.IV);
        if (!jaMi.equals(account.getPassWord())) return Result.error().message("密码错误");

        //生成jwt
        String jwt = jwtUtils.generateToken(account.getId());
        response.setHeader("Authorization",jwt);
        response.setHeader("Access-control-Expose-Headers","Authorization");

        return Result.success().message("登录成功").data("欢迎",MapUtil.builder()
                .put("id",account.getId())
                .put("userName",account.getUserName())
                .put("photo",account.getPhoto())
                .map()
        );
    }

//    /**
//     * 退出登录（前端完成）
//     * @return
//     */
//    @RequiresAuthentication//需要登录认证
//    @PostMapping("/logout")
//    public Result logout(){
//        SecurityUtils.getSubject().logout();
//        return Result.success().message("已退出");
//    }

    /**
     * 修改密码
     * @param passwordVo
     * @return
     */
    @RequiresAuthentication//Shiro注解，需要进行登录才能访问该接口
    @PostMapping("/gaiPassword")
    public Result gaiPassword(@RequestBody PasswordVo passwordVo){
        return userDataService.gaiPassword(passwordVo);
    }

//    @RequiresAuthentication//需要进行登录才能访问该接口
//    @GetMapping("/index")
//    public Result index(Long id){
//        UserData userData = userDataService.getById(id);
//        return Result.success().message("查询成功").data("查询结果",userData);
//    }

}
