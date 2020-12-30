package com.example.controller;

import cn.hutool.json.JSONObject;
import com.example.common.Result;
import com.example.util.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


/**
 * @author JiangQingShan
 * @date 2020/12/29 - 10:16
 */
@RestController
@RequestMapping("/wx")
public class WeiXinController {
    //用于发送HTTP请求，该组件不能直接注入，需要创建一个配置文件
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 微信小程序授权登录,首先客户端授权登录时会发送一个code给服务端（后端），然后后端根据code(临时凭证)、小程序开发者Id、小程序开发者密匙发送微信规定
     * 的请求会获得access_token(session_key与openid)，这东西也就只有openid会用到，之后可使用自己创建的token来代替code来解除临时凭证的限制
     * 然后返回token、openid给前端，前端就会根据openid来获取到用户的信息交给后端让后端来创建该信息的实体类存入数据库中，前端访问接口时就必须携带token
     * 否则无法访问设有Shiro拦截的接口。
     * @param code  临时登录凭证
     * @return
     */
    @PostMapping("/login")
    //原返回值：Map<String,Object>，HashMap是Java程序员使用频率最高的用于映射(键值对)处理的数据类型
    public Result wxLogin(@RequestParam("code") String code){
        if (code.equals(null)) return Result.error().message("code为空");
        //拼接URL--->appId(小程序开发者Id) + secret(小程序开发者密匙) + code(由前端调用wx.login()并发送给后端，一个只能验证一次)
        String appId = "wxcfcbe5690d46b5be";
        String secret = "8b417e21d2624496b1c5bd347dde3133";
        //请求以下链接可获得 access_token，access_token有效期是7200s
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+ appId
                +"&secret="+ secret
                +"&js_code="+ code
                +"&grant_type=authorization_code";
        //拼接好的URL字符串,发送一个HTTP GET请求，返回的请求体将access_token映射为一个对象，将session_key与openid存入对象
        String jsonData = this.restTemplate.getForObject(url, String.class);

        //返回结果的json对象
        JSONObject jsonObject = new JSONObject(jsonData);
        //获取会话密钥（session_key）
        String session_key = jsonObject.get("session_key").toString();
        //用户的唯一标识（openid）
        String openId = (String) jsonObject.get("openid");
        //校验失败,如果映射的对象包含errcode(错误信息的一部分)
        if (StringUtils.contains(jsonData,"errcode")) return Result.error().message("授权失败");

        //生成jwtToken代替jsonData(access_token)
        String jwtToken = jwtUtils.generateToken(openId);

        //给前端返回自定义登录态（凭证）,还返回openid是为了让前端容易根据这个openid来获取用户的信息，最后返回给后端，后端根据信息来生成实体类
        return Result.success().message("登录成功").data("token",jwtToken).data("openid",openId);
    }

    /**
     * 根据获取到的登录凭证进行判断该凭证是否存在Redis中(有效)
     * @return
     */
    @RequiresAuthentication//Shiro注解，需要进行登录才能访问该接口
    @GetMapping("/index")
    public Result index() {
        return Result.error().message("访问成功");
    }
}
