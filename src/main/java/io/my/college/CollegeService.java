package io.my.college;

import io.my.base.entity.College;
import io.my.base.payload.BaseExtentionResponse;
import io.my.base.repository.CollegeRepository;
import io.my.college.payload.response.CollegeSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CollegeService {
    private final CollegeRepository collegeRepository;

    public Mono<BaseExtentionResponse<List<CollegeSearchResponse>>> colleageSearch(String search) {
        return collegeRepository.findByNameContaining(search).collectList()
            .map(list -> {
                List<CollegeSearchResponse> collegeList = new ArrayList<>();

                for (College entity : list)
                    collegeList.add(new CollegeSearchResponse(entity.getId(), entity.getName()));

                BaseExtentionResponse<List<CollegeSearchResponse>> responseBody = new BaseExtentionResponse<>();
                responseBody.setReturnValue(collegeList);

                return responseBody;
            });
    }

}
