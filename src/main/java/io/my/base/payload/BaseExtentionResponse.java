package io.my.base.payload;

import io.my.base.exception.ErrorTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseExtentionResponse<T> extends BaseResponse{
    T returnValue;

    public BaseExtentionResponse(ErrorTypeEnum errorTypeEnum) {
        super(errorTypeEnum);
    }
}
