package io.my.base.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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
}
