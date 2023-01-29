package io.my.base.exception;

import io.my.base.util.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.DispatcherHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
public class NotFoundExceptionHandler extends DispatcherHandler implements BeanPostProcessor {
    private final ExceptionUtil exceptionUtil;

    public Object postProcessAfterInitialization(Object bean, String beanName) {

        if (bean instanceof DispatcherHandler) {
            return this;
        }

        return bean;
    }

    public NotFoundExceptionHandler(ExceptionUtil exceptionUtil) {
        this.exceptionUtil = exceptionUtil;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange) {
        return super.handle(exchange).onErrorResume(throwable -> {
            if (throwable instanceof ResponseStatusException) {
                return this.handleException(exchange, (ResponseStatusException) throwable);
            }
            return Mono.error(throwable);
        });
    }

    private Mono<Void> handleException(ServerWebExchange exchange, ResponseStatusException exception) {
        if (exception.getStatus() != HttpStatus.NOT_FOUND) {
            return Mono.error(exception);
        }

        exceptionUtil.setExceptionHeaders(exchange, HttpStatus.NOT_FOUND);
        DefaultDataBuffer buffer = exceptionUtil.createExceptionBody(ErrorTypeEnum.NOT_FOUND_EXCEPTION);
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}
