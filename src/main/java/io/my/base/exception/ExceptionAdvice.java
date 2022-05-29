package io.my.base.exception;

import io.my.base.exception.object.DatabaseException;
import io.my.base.exception.object.MailSenderException;
import io.my.base.exception.object.PasswordWrongException;
import io.my.base.payload.BaseResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(MailSenderException.class)
    protected ResponseEntity<BaseResponse> exceptionAdvice(MailSenderException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new BaseResponse(
                        ErrorTypeEnum.MAIL_EXCEPTION.getCode(),
                        ErrorTypeEnum.MAIL_EXCEPTION.getResult()));
    }

    @ExceptionHandler(PasswordWrongException.class)
    protected ResponseEntity<BaseResponse> exceptionAdvice(PasswordWrongException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new BaseResponse(
                        ErrorTypeEnum.WRONG_PASSWORD.getCode(),
                        ErrorTypeEnum.WRONG_PASSWORD.getResult()
                ));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<BaseResponse> exceptionAdvice(Exception e) {

        return ResponseEntity.internalServerError()
                .body(new BaseResponse(
                        ErrorTypeEnum.SERVER_ERROR.getCode(),
                        ErrorTypeEnum.SERVER_ERROR.getResult()));
    }

    @ExceptionHandler(DatabaseException.class)
    protected ResponseEntity<BaseResponse> exceptionAdvice(DatabaseException e) {
        return ResponseEntity.internalServerError()
                .body(new BaseResponse(
                        ErrorTypeEnum.DATABASE_EXCEPTION.getCode(),
                        ErrorTypeEnum.DATABASE_EXCEPTION.getResult()
                ));
    }


}
