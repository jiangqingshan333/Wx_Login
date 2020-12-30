package com.example.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 自定义token
 * 实现JwtFilter生成token的方法
 * @author JiangQingShan
 * @date 2020/12/26 - 21:30
 */
public class JwtToken implements AuthenticationToken {

    private String token;

    public JwtToken(String jwt){
        this.token = jwt;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    //密钥
    public Object getCredentials() {
        return token;
    }
}
