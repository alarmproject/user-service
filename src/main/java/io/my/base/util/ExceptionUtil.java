package io.my.base.util;

import io.my.base.exception.ErrorTypeEnum;
import io.my.base.payload.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
@RequiredArgsConstructor
public class ExceptionUtil {
    private final JsonUtil jsonUtil;

    public DefaultDataBuffer createExceptionBody(ErrorTypeEnum errorTypeEnum) {
        BaseResponse response = new BaseResponse();
        response.setResult(errorTypeEnum.getResult());
        response.setCode(errorTypeEnum.getCode());

        return new org.springframework.core.io.buffer.DefaultDataBufferFactory().wrap(
                jsonUtil.objectToByteArray(response)
        );
    }

    public void setExceptionHeaders(ServerWebExchange exchange, HttpStatus httpStatus) {
        exchange.getResponse().setStatusCode(httpStatus);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
    }

}
