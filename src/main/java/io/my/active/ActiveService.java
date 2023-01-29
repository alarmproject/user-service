package io.my.active;


import io.my.active.payload.response.ActiveHistoryResponse;
import io.my.base.context.JwtContextHolder;
import io.my.base.payload.BaseExtentionPagingResponse;
import io.my.base.repository.dao.ActiveHistoryDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActiveService {
    private final ActiveHistoryDAO activeHistoryDAO;

    public Mono<BaseExtentionPagingResponse<List<ActiveHistoryResponse>>> findActiveHistory(Long id) {
        return JwtContextHolder.getMonoUserId().flatMapMany(userId -> {
            Integer limit = 10;
            return activeHistoryDAO.findActiveHistoryPaging(id, userId, limit);
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
