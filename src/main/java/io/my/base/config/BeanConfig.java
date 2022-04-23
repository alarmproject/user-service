package io.my.base.config;

import io.my.base.exception.NotFoundExceptionHandler;
import io.my.base.util.ExceptionUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfig {
    private final ExceptionUtil exceptionUtil;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public NotFoundExceptionHandler changeWebHandler() {
        return new NotFoundExceptionHandler(exceptionUtil);
    }

}
