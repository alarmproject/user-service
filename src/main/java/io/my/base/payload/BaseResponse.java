package io.my.base.payload;

import io.my.base.exception.ErrorTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
@AllArgsConstructor
public class BaseResponse {
    private int code;
    private String result;

    public BaseResponse() {
        this.result = "success";
    }

    public BaseResponse(String result) {
        this.result = result;
    }

    public BaseResponse(ErrorTypeEnum errorTypeEnum) {
        this.code = errorTypeEnum.getCode();
        this.result = errorTypeEnum.getResult();
    }
}
