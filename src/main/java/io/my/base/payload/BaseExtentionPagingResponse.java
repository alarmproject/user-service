package io.my.base.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseExtentionPagingResponse<T> extends BaseExtentionResponse<T>{
    private Long id;

}
