package io.my.active;


import io.my.active.payload.response.ActiveHistoryResponse;
import io.my.base.context.JwtContextHolder;
import io.my.base.payload.BaseExtentionPagingResponse;
import io.my.base.payload.BaseExtentionResponse;
import io.my.base.payload.BaseResponse;
import io.my.base.repository.ActiveHistoryRepository;
import io.my.base.repository.custom.CustomActiveHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActiveService {
    private final CustomActiveHistoryRepository customActiveHistoryRepository;

    public Mono<BaseExtentionPagingResponse<List<ActiveHistoryResponse>>> findActiveHistory(Long id) {
        return JwtContextHolder.getMonoUserId().flatMapMany(userId -> {
            Integer limit = 10;
            return customActiveHistoryRepository.findActiveHistoryPaging(
                    (id == null || id == 0) ? Long.MAX_VALUE : id, userId, limit
            );
        })
        .map(entity -> {
            ActiveHistoryResponse response = new ActiveHistoryResponse();
            response.setContent(entity.getContent());
            response.setId(entity.getId());
            response.setModDateTime(entity.getModDateTime());
            response.setRegDateTime(entity.getRegDateTime());
            return response;
        })
        .collectList()
        .map(list -> {
            BaseExtentionPagingResponse<List<ActiveHistoryResponse>> responseBody = new BaseExtentionPagingResponse<>();

            if (list != null && list.size() > 0) {
                responseBody.setReturnValue(list);
                responseBody.setId(list.get(list.size() - 1).getId());
            }

            return responseBody;
        })
        .switchIfEmpty(Mono.just(new BaseExtentionPagingResponse<>()))
        ;

    }

}
