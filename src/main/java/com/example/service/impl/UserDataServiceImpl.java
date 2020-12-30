package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.common.Result;
import com.example.config.ConstantCode;
import com.example.dto.UserDataDto;
import com.example.entity.UserData;
import com.example.mapper.UserDataMapper;
import com.example.service.UserDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.util.AESUtil;
import com.example.util.JwtUtils;
import com.example.vo.LoginVo;
import com.example.vo.PasswordVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * InnoDB free: 4096 kB 服务实现类
 * </p>
 *
 * @author jiangqingshan
 * @since 2020-12-27
 */
@Service
public class UserDataServiceImpl extends ServiceImpl<UserDataMapper, UserData> implements UserDataService {

    @Autowired
    JwtUtils jwtUtils;
    /**
     * 用户注册
     * @param
     * @return
     */
    @Override
    public Result addUser(UserData userData) {
        LambdaQueryWrapper<UserData> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserData::getAccount,userData.getAccount());
        List<UserData> userTests = this.baseMapper.selectList(wrapper);

        if (!userTests.isEmpty()) return Result.error().message("该账户已存在");
        //使用自定义的加密工具类对输入的密码进行加密
        String jaMi = AESUtil.encrypt(userData.getPassWord(), ConstantCode.ORIGINAL_KEY, ConstantCode.IV);
        userData.setPassWord(jaMi);

        userData.setModifyTime(new Date());
        this.save(userData);

        return Result.success().message("注册成功");
    }

//    /**
//     * 用户登录
//     * @param
//     * @return
//     */
//    @Override
//    public Result userLogin(LoginVo loginVo) {
//
//        //需要在登录成功后显示出用户的ID与用户名,需要创建一个数据传输类，在登录成功时，将注册的值传入给该类要显示的属性信息
//        LambdaQueryWrapper<UserData> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(UserData::getAccount,loginVo.getAccount());
//        UserData userData = this.baseMapper.selectOne(wrapper);
//        if (userData == null) return Result.error().message("账号不存在");
//
//        //String jaMi = AESUtil.encrypt(loginVo.getPassword(), ConstantCode.ORIGINAL_KEY, ConstantCode.IV);
//        if (!loginVo.getPassword().equals(userData.getPassWord())) return Result.error().message("密码错误");
//
//        UserDataDto userDto = new UserDataDto();
//        userDto.setId(userData.getId());
//        userDto.setUserName(userData.getUserName());
//        userDto.setPhoto(userData.getPhoto());
//
//        return Result.success().message("登录成功").data("欢迎",userDto);
//
//        /*LambdaQueryWrapper<UserTest> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(UserTest::getAccount,userLoginVo.getAccount());
//        UserTest userTest = this.baseMapper.selectOne(wrapper);
//        if (userTest == null) return Result.error().message("账号不存在");
//
//        String jaMi = AESUtil.encrypt(userLoginVo.getPas(), ConstantCode.ORIGINAL_KEY, ConstantCode.IV);
//        if (!jaMi.equals(userTest.getPassWord())) return Result.error().message("密码错误");
//        return Result.success().message("登录成功");
//        //以上代码登录成功时，并不会显示出登录账号的用户信息*/
//    }

    /**
     * 重置密码
     * @param
     * @return
     */
    @Override
    public Result gaiPassword(PasswordVo passwordVo) {

        UserData passById = this.getById(passwordVo.getId());
        if (passById == null) return Result.error().message("该账号不存在");

        String jaMi = AESUtil.encrypt(passwordVo.getPassword(), ConstantCode.ORIGINAL_KEY, ConstantCode.IV);
        if (!jaMi.equals(passById.getPassWord())) return Result.error().message("原密码不正确");

        String newJami = AESUtil.encrypt(passwordVo.getNewPass(), ConstantCode.ORIGINAL_KEY, ConstantCode.IV);
        if (newJami.equals(jaMi)) return Result.error().message("新旧密码不能一样");

        passById.setPassWord(newJami);
        passById.setModifyTime(new Date());

        this.baseMapper.updateById(passById);

        return Result.success().message("修改成功");
    }
}
