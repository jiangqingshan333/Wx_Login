package com.example.config;

import com.example.shiro.AccountRealm;
import com.example.shiro.JwtFilter;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro配置文件
 * Shiro启用注解拦截器，前后端的凭证设为jwt，JwtFilter负责将jwt封装成token
 * @author JiangQingShan
 * @date 2020/12/26 - 18:38
 */
@Configuration
public class ShiroConfig {

    @Autowired
    JwtFilter jwtFilter;//创建的JwtFilter需要继承shiro内置的过滤器

    /**
     * 会话管理器
     * @param redisSessionDAO
     * @return
     */
    @Bean
    public SessionManager sessionManager(RedisSessionDAO redisSessionDAO){
        //默认使用的SessionManager，用于JavaSE环境的session管理
        DefaultSessionManager sessionManager = new DefaultSessionManager();

        //inject redisSessionDAO
        sessionManager.setSessionDAO(redisSessionDAO);
        return sessionManager;
    }

    /**
     * 安全管理器
     * @param accountRealm  ：Shiro与应用安全数据间的“桥梁”或者“连接器”
     * @param sessionManager ：会话管理器，管理着应用中所有Subject的会话，包括会话的创建、维护、删除、失效、验证等工作
     * @param redisCacheManager：redis替换EhCache（shiro默认）来实现缓存
     * @return
     */
    @Bean
    //List<Realm> realms为自定义参数，其自定义为：AccountRealm accountRealm
    public SessionsSecurityManager securityManager(AccountRealm accountRealm, SessionManager sessionManager,
                                                   RedisCacheManager redisCacheManager){

        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager(accountRealm);

        //inject sessionManager
        securityManager.setSessionManager(sessionManager);
        //inject redisCacheManager
        securityManager.setCacheManager(redisCacheManager);
        return securityManager;
    }

    @Bean
    //定义哪一些连接需要经过哪一些过滤器
    public ShiroFilterChainDefinition shiroFilterChainDefinition(){
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();

        LinkedHashMap<String, String> filterMap = new LinkedHashMap<>();
        //所有连接都需要经过该过滤器
        filterMap.put("/**","jwt");//主要通过注解检验权限
        //filterMap.put("/**","authc");//验证登录（登录过滤器）
        chainDefinition.addPathDefinitions(filterMap);
        return chainDefinition;
    }

    /**
     * Shiro过滤器配置
     */
    @Bean("shiroFilterFactoryBean")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager,
                                                         ShiroFilterChainDefinition shiroFilterChainDefinition){

        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        // Shiro的核心安全接口,这个属性是必须的
        shiroFilter.setSecurityManager(securityManager);
        //自定义过滤
        HashMap<String, Filter> filters = new HashMap<>();
        //所有请求需要JwtToken认证
        filters.put("jwt",jwtFilter);
        shiroFilter.setFilters(filters);

        Map<String, String> filterMap = shiroFilterChainDefinition.getFilterChainMap();

        shiroFilter.setFilterChainDefinitionMap(filterMap);
        return shiroFilter;

    }

//    JwtFilter jwtFilter(){
//        return new JwtFilter();
//    }
}
