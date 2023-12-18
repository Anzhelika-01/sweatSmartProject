package bg.softuni.sweatsmartproject.config;

import bg.softuni.sweatsmartproject.domain.interceptor.LoginInterceptor;
import bg.softuni.sweatsmartproject.domain.interceptor.RenameInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@EnableScheduling
public class InterceptorConfig implements WebMvcConfigurer {

    private final RenameInterceptor renameInterceptor;
    private final LoginInterceptor loginInterceptor;

    @Autowired
    public InterceptorConfig(RenameInterceptor renameInterceptor, LoginInterceptor loginInterceptor) {
        this.renameInterceptor = renameInterceptor;
        this.loginInterceptor = loginInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(renameInterceptor);
        registry.addInterceptor(loginInterceptor);
    }
}