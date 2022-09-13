package fcode.backend.management.config;


import fcode.backend.management.config.interceptor.GatewayInterceptor;
import fcode.backend.management.model.request.GoogleAuthRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    @Autowired
    GatewayInterceptor gatewayInterceptor;
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
    @Bean
    public GoogleAuthRequest googleAuthRequest() {return new GoogleAuthRequest();}
    @Bean
    public static WebClient.Builder getWebClientBuilder() {
        return WebClient.builder();
    }
    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(gatewayInterceptor).addPathPatterns("/**").
                excludePathPatterns(
                        "/v2/api-docs",
                        "/swagger-resources/**",
                        "/swagger-ui.html",
                        "/webjars/**"
                );
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("GET","PUT","POST","DELETE");
    }
}
