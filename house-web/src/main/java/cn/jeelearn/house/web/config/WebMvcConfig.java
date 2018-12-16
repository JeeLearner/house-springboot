package cn.jeelearn.house.web.config;

import cn.jeelearn.house.web.interceptor.AuthActionInterceptor;
import cn.jeelearn.house.web.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2018/12/16
 * @Version:v1.0
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private AuthActionInterceptor authActionInterceptor;
    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(authInterceptor).excludePathPatterns("/static").addPathPatterns("/**");
        registry
                .addInterceptor(authActionInterceptor).addPathPatterns("/house/toAdd")
                .addPathPatterns("/accounts/profile").addPathPatterns("/accounts/profileSubmit")
                .addPathPatterns("/house/bookmarked").addPathPatterns("/house/del")
                .addPathPatterns("/house/ownlist").addPathPatterns("/house/add")
                .addPathPatterns("/house/toAdd").addPathPatterns("/agency/agentMsg")
                .addPathPatterns("/comment/leaveComment").addPathPatterns("/comment/leaveBlogComment");
        super.addInterceptors(registry);
    }



}

