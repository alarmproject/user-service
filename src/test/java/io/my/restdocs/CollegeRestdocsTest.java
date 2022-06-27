package io.my.restdocs;

import io.my.base.base.RestDocAttributes;
import io.my.base.base.RestdocsBase;
import io.my.base.payload.BaseExtentionResponse;
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
        BaseExtentionResponse<List<CollegeSearchResponse>> responseBody = new BaseExtentionResponse<>();
        List<CollegeSearchResponse> list = new ArrayList<>();
        CollegeSearchResponse firstEntity = CollegeSearchResponse.builder()
                .id(1L)
                .name("서울대학교")
                .emailPrefixList(List.of("sun.ac.kr"))
                .build();
        CollegeSearchResponse secondEntity = CollegeSearchResponse.builder()
                .id(2L)
                .name("고려대학교")
                .emailPrefixList(List.of("korea.ac.kr"))
                .build();

        list.add(firstEntity);
        list.add(secondEntity);

        responseBody.setReturnValue(list);

        Mockito.when(collegeService.collegeSearch(Mockito.anyString())).thenReturn(Mono.just(responseBody));

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
                        fieldWithPath("returnValue.[].id").description("대학교 번호")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Integer")),
                        fieldWithPath("returnValue.[].name").description("대학교 이름")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("returnValue.[].emailPrefixList.[]").description("대학교 이메일 목록")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Array"))
                );
        String params = "?search=대학";

        getWebTestClientUnAuth("/college/list"+ params).expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(createConsumer("/collegesearch", requestParametersSnippet, responseFieldsSnippet));
    }

}
