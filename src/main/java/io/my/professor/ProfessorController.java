package io.my.professor;

import io.my.base.payload.BaseExtentionResponse;
import io.my.professor.payload.request.SaveProfessorRequest;
import io.my.professor.payload.response.ProfessorListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/professor")
public class ProfessorController {
    private final ProfessorService professorService;

    @PostMapping
    public Mono<BaseExtentionResponse<Long>> saveProfessor(
            @RequestBody SaveProfessorRequest requestBody) {

        return this.professorService.saveProfessor(requestBody);
    }

    @GetMapping
    public Mono<BaseExtentionResponse<List<ProfessorListResponse>>> findProfessorList(
            @RequestParam("name") String name) {
        return this.professorService.findProfessorList(name);
    }

}
