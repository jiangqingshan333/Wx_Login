package com.example.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MybatisPlus配置文件
 * @author JiangQingShan
 * @date 2020/12/25 - 10:13
 */
@Configuration//该注解让该类声明为配置类，SpringBoot将Spring之前的配置文件变成配置类然后使用注解实现所需的功能
@EnableTransactionManagement
@MapperScan("com.example.mapper")
public class MybatisPlusConfig {

    @Bean
    public PaginationInterceptor paginationInterceptor(){
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        return paginationInterceptor;
    }
}
