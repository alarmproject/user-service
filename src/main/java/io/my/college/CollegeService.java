package io.my.college;

import io.my.base.payload.BaseExtentionResponse;
import io.my.base.repository.CollegeRepository;
import io.my.college.payload.response.CollegeSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CollegeService {
    private final CollegeRepository collegeRepository;

    public Mono<BaseExtentionResponse<List<CollegeSearchResponse>>> collegeSearch(String search) {
        return collegeRepository.findByNameContaining(search).map(entity -> {
            List<String> emailPrefixList = null;

            if (entity.getEmail() != null) {
                emailPrefixList = Arrays.stream(entity.getEmail().split("\\|")).collect(Collectors.toList());
            }

            return CollegeSearchResponse.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .emailPrefixList(emailPrefixList)
                    .build();
        }).collectList().map(BaseExtentionResponse::new);
    }
}
