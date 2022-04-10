package io.my.professor;

import io.my.base.context.JwtContextHolder;
import io.my.base.entity.Professor;
import io.my.base.payload.BaseExtentionResponse;
import io.my.base.properties.ServerProperties;
import io.my.base.repository.ProfessorRepository;
import io.my.base.repository.UserRepository;
import io.my.base.repository.dao.ProfessorDAO;
import io.my.professor.payload.request.SaveProfessorRequest;
import io.my.professor.payload.response.ProfessorListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfessorService {
    private final ProfessorDAO professorDAO;
    private final UserRepository userRepository;
    private final ServerProperties serverProperties;
    private final ProfessorRepository professorRepository;

    public Mono<BaseExtentionResponse<Long>> saveProfessor(SaveProfessorRequest requestBody) {
        return JwtContextHolder.getMonoUserId().flatMap(userRepository::findById)
                .flatMap(user -> {
                    Professor entity = Professor.builder()
                            .collegeId(user.getCollegeId())
                            .departmentId(requestBody.getDepartmentId())
                            .name(requestBody.getName())
                            .imageId(requestBody.getImageId())
                            .content(requestBody.getContent())
                            .build();
                    return professorRepository.save(entity);
                })
                .map(entity -> new BaseExtentionResponse<>(entity.getId()));
    }

    public Mono<BaseExtentionResponse<List<ProfessorListResponse>>> findProfessorList(String name) {
        return JwtContextHolder.getMonoUserId().flatMap(userRepository::findById)
                .flatMapMany(user -> professorDAO.findByCollegeIdAndNameContaining(user.getCollegeId(), name))
                .map(entity -> ProfessorListResponse.builder()
                            .id(entity.getId())
                            .departmentName(entity.getDepartment().getName())
                            .name(entity.getName())
                            .imageUrl(
                                    entity.getImage().getFileName() != null ?
                                            serverProperties.getImageUrl() +
                                            serverProperties.getImagePath() +
                                            entity.getImage().getFileName() :
                                            null
                            )
                            .content(entity.getContent())
                            .build())
                .collectList()
                .map(BaseExtentionResponse::new)
                ;
    }
}
