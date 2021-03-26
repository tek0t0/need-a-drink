package bg.softuni.needadrink.config;


import bg.softuni.needadrink.web.interceptors.FaviconInterceptor;
import bg.softuni.needadrink.web.interceptors.TitleInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApplicationWebConfiguration implements WebMvcConfigurer {

    private final FaviconInterceptor faviconInterceptor;
    private final TitleInterceptor titleInterceptor;

    @Autowired
    public ApplicationWebConfiguration(FaviconInterceptor faviconInterceptor, TitleInterceptor titleInterceptor) {
        this.faviconInterceptor = faviconInterceptor;
        this.titleInterceptor = titleInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.faviconInterceptor);
        registry.addInterceptor(this.titleInterceptor);
    }
}
