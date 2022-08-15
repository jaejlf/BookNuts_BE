package team.nine.booknutsbackend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import team.nine.booknutsbackend.interceptor.JWTAuthInterceptor;

@Configuration
@RequiredArgsConstructor
public class JWTAuthConfig implements WebMvcConfigurer {

    private final JWTAuthInterceptor jwtAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/auth/**"); //예외 처리할 주소
    }

}