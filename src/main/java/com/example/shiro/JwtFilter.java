package com.example.shiro;

import cn.hutool.json.JSONUtil;
import com.example.common.Result;
import com.example.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;


import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Shiro拦截器/过滤器----->将jwt封装成token，然后将JwtToken传到AccountRealm(自定义)
 * @author JiangQingShan
 * @date 2020/12/26 - 21:14
 */
@Component
//AuthenticatingFilter是shiro内置的实现一些基本自动登录的逻辑
public class JwtFilter extends AuthenticatingFilter {
    @Autowired
    JwtUtils jwtUtils;

    @Override
    //shiro的supports默认的token是UsernamePasswordToken，
    // 但是我们需要的是jwt的token，所以我们要自己定义一个jwtToken来完成shiro的supports方法

    //AuthenticatingFilter的executeLogin会调用createToken然后复制给token(生成token)，最后shiro根据token进行登录的处理（Realm处理）
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        //在用户登录后jwt返回给客户，所以客户访问接口时jwt是存在Header里面
        //所以将servletRequest请求强制转化为HttpServletRequest请求才能获取到Header
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt = request.getHeader("Authorization");
        //如果jwt为空即为游客访问
        if (StringUtils.isEmpty(jwt)){
            //没必要登录
            return null;
        }
        //生成token
        return new JwtToken(jwt);
    }

    @Override
    //拦截
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt = request.getHeader("Authorization");
        if (StringUtils.isEmpty(jwt)){
            return true;
        }
        //校验jwt
        Claims claim = jwtUtils.getClaimByToken(jwt);
        //如果jwt为空或者过期
        if (claim == null || jwtUtils.isTokenExpired(claim.getExpiration()))
            //抛出异常
            throw new ExpiredCredentialsException("令牌为空或已失效，请重新登录");
        //执行登录
        return executeLogin(servletRequest,servletResponse);
    }

    @Override
    //登录异常
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        //强转response，使用它返回json数据
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        //获取异常
        Throwable throwable = e.getCause() == null ? e : e.getCause();
        //统一返回结果
        Result result = Result.error().data("异常", throwable.getMessage());
        //使用json格式的方式返回
        String jsonStr = JSONUtil.toJsonStr(result);
        try {
            httpServletResponse.getWriter().print(jsonStr);
        } catch (IOException ioException) {
            //ioException.printStackTrace();
        }
        return false;
    }

    @Override
    /**
     * 跨域处理
     */
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        //跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(org.springframework.http.HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }
}
