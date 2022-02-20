package io.my.college;

import io.my.base.entity.College;
import io.my.base.repository.CollegeRepository;
import io.my.college.payload.CollegeIdAndName;
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

    public Mono<CollegeSearchResponse> colleageSearch(String search) {
        return collegeRepository.findByNameContaining(search).collectList()
            .map(list -> {
                List<CollegeIdAndName> collegeList = new ArrayList<>();

                for (College entity : list)
                    collegeList.add(new CollegeIdAndName(entity.getId(), entity.getName()));

                CollegeSearchResponse responseBody = new CollegeSearchResponse();
                responseBody.setList(collegeList);

                return responseBody;
            });
    }

}
