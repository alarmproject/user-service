package io.my.restdocs;

import io.my.base.base.RestDocAttributes;
import io.my.base.base.RestdocsBase;
import io.my.college.payload.CollegeIdAndName;
import io.my.college.payload.response.CollegeSearchResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

class CollegeRestdocsTest extends RestdocsBase {

    @Test
    @DisplayName("CollegeSearch Controller")
    void collegeSearch() {
        CollegeSearchResponse responseBody = new CollegeSearchResponse();
        List<CollegeIdAndName> list = new ArrayList<>();
        CollegeIdAndName firstEntity = new CollegeIdAndName();
        firstEntity.setId(1L);
        firstEntity.setName("서울대학교");
        CollegeIdAndName secondEntity = new CollegeIdAndName();
        secondEntity.setId(2L);
        secondEntity.setName("고려대학교");

        list.add(firstEntity);
        list.add(secondEntity);

        responseBody.setList(list);

        Mockito.when(collegeService.colleageSearch(Mockito.anyString())).thenReturn(Mono.just(responseBody));

        RequestParametersSnippet requestParametersSnippet =
                requestParameters(
                    parameterWithName("search").description("검색어")
                            .attributes(
                                    RestDocAttributes.length(0),
                                    RestDocAttributes.format("String"))
                );

        ResponseFieldsSnippet responseFieldsSnippet =
                responseFields(
                        fieldWithPath("result").description("결과 메시지")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("code").description("결과 코드")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Integer")),
                        fieldWithPath("list.[].id").description("대학교 번호")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Integer")),
                        fieldWithPath("list.[].name").description("대학교 이름")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String"))
                );
        String params = "?search=대학";

        getWebTestClientUnAuth("/college/list"+ params).expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(createConsumer("/collegesearch", requestParametersSnippet, responseFieldsSnippet));
    }

}
