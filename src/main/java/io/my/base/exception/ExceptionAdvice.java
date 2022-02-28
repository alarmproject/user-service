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
        return EntityResponse.fromObject(
                new BaseResponse(
                        ErrorTypeEnum.SERVER_ERROR.getCode(),
                        ErrorTypeEnum.SERVER_ERROR.getResult()))
                .status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(JwtException.class)
    protected Mono<EntityResponse<BaseResponse>> exceptionAdvice(JwtException e) {
        e.printStackTrace();
        return EntityResponse.fromObject(
                new BaseResponse(
                        ErrorTypeEnum.JWT_EXCEPTION.getCode(),
                        ErrorTypeEnum.JWT_EXCEPTION.getResult()))
                .status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(MailSenderException.class)
    protected Mono<EntityResponse<BaseResponse>> exceptionAdvice(MailSenderException e) {
        e.printStackTrace();
        return EntityResponse.fromObject(
                new BaseResponse(
                        ErrorTypeEnum.MAIL_EXCEPTION.getCode(),
                        ErrorTypeEnum.MAIL_EXCEPTION.getResult()))
                .status(HttpStatus.BAD_REQUEST).build();
    }

}
