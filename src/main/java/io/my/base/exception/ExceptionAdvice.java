package io.my.base.exception;

import io.my.base.exception.object.JwtException;
import io.my.base.exception.object.MailSenderException;
import io.my.base.payload.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.server.EntityResponse;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(Exception.class)
    protected Mono<EntityResponse<BaseResponse>> exceptionAdvice(Exception e) {
        e.printStackTrace();
        return EntityResponse.fromObject(new BaseResponse(1, "Server Error")).status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(JwtException.class)
    protected Mono<EntityResponse<BaseResponse>> exceptionAdvice(JwtException e) {
        e.printStackTrace();
        return EntityResponse.fromObject(new BaseResponse(2, "Don't have jwt or jwt is wrong")).status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(MailSenderException.class)
    protected Mono<EntityResponse<BaseResponse>> exceptionAdvice(MailSenderException e) {
        e.printStackTrace();
        return EntityResponse.fromObject(new BaseResponse(3, "Mail Sender is error")).status(HttpStatus.BAD_REQUEST).build();
    }

}
