package io.my.active;

import io.my.active.payload.response.ActiveHistoryResponse;
import io.my.base.annotation.Logger;
import io.my.base.payload.BaseExtentionPagingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/active")
public class ActiveController {
    private final ActiveService activeService;

    @Logger
    @GetMapping("/history")
    public Mono<BaseExtentionPagingResponse<List<ActiveHistoryResponse>>> findActiveHistory(
            @RequestParam(name = "id", required = false) Long id) {
        return activeService.findActiveHistory(id);
    }

}
