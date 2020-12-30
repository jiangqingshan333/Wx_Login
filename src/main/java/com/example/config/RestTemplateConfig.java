package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate初始化配置
 * @author JiangQingShan
 * @date 2020/12/29 - 10:29
 */
@Configuration
public class RestTemplateConfig {
    @Bean//加载到容器中
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
