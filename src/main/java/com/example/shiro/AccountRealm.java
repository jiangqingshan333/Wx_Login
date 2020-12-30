package com.example.shiro;


import cn.hutool.core.bean.BeanUtil;
import com.example.entity.UserData;
import com.example.service.UserDataService;
import com.example.util.JwtUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 自定义的AuthorizingRealm------->JwtToken传入后跟数据库进行匹配（根据id查询匹配），匹配成功后将信息返回给Shiro，Shiro就能进入权限接口里面
 * @author JiangQingShan
 * @date 2020/12/26 - 19:37
 */
@Component//把自定义的Realm注入到ShiroConfig的Realm参数里面
public class AccountRealm extends AuthorizingRealm {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserDataService userDataService;


    @Override
    //令牌支持，为了Realm支持jwt的凭证校验
    public boolean supports(AuthenticationToken token) {
        //返回的token是JwtToken
        return token instanceof JwtToken;
    }

    @Override
    //权限获取，检测到用户之后获取用户的权限信息，将权限信息封装成AuthorizationInfo返回给shiro
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    @Override
    //身份验证，在获取到token后进行密码校验等逻辑，校验成功将token封装成AuthenticationInfo
    //默认使用UsernamePasswordToken
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        //将UsernamePasswordToken强转为自定义的JwtToken
        JwtToken jwtToken = (JwtToken) token;
        //登陆校验，获取到的token信息把里面的用户信息提取出来，跟数据库里面的数据做对比
        //getSubject()获取到id，getPrincipal()获取到token
        String userId = jwtUtils.getClaimByToken((String) jwtToken.getPrincipal()).getSubject();
        //根据id查询数据库记录
        UserData user = userDataService.getById(Long.valueOf(userId));
        if (userId == null){
            //抛出异常
            throw new UnknownAccountException("账户不存在");
        }
        //用户表的状态字段为-1则被锁定
        if (user.getStatus() == -1){
            //抛出异常
            throw new LockedAccountException("账户已被锁定");
        }

        AccountProFile proFile = new AccountProFile();
        BeanUtil.copyProperties(user,proFile);

        /**
         * 实现自定义的AuthorizingRealm，实现权限配置和用户认证
         */
        //返回的数据有用户的基本信息（principal），密钥信息（credentials），Realm名字（realmName）
        //返回的用户信息不能全部都返回比如密码，所以要把返回用户可公开的信息封装起来
        return new SimpleAuthenticationInfo(proFile,jwtToken.getCredentials(),getName());
    }
}
