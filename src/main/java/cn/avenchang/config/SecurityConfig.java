package cn.avenchang.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by 53068 on 2018/3/7 0007.
 */
@Configuration
public class SecurityConfig extends WebMvcConfigurerAdapter{

    @Bean
    public TokenVerifyInterceptor tokenVerifyInterceptor(){
        return new TokenVerifyInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenVerifyInterceptor())
                .addPathPatterns("/user/**", "/venue/**", "/manager/**").excludePathPatterns("/user/paid");
//                .excludePathPatterns("/", "/register", "/login/");
        super.addInterceptors(registry);
    }
}
