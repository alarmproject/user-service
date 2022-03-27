package io.my.restdocs;

import io.my.base.base.RestDocAttributes;
import io.my.base.base.RestdocsBase;
import io.my.base.payload.BaseExtentionResponse;
import io.my.department.payload.request.SaveDepartmentRequest;
import io.my.department.payload.response.DepartmentListResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

class DepartmentRestdocsTest extends RestdocsBase {

    @Test
    @DisplayName("학과 등록")
    void saveDepartment() {
        SaveDepartmentRequest requestBody = new SaveDepartmentRequest();
        requestBody.setName("경영학과");

        Mono<BaseExtentionResponse<Long>> responseBody = Mono.just(new BaseExtentionResponse(1L));

        Mockito.when(departmentService.saveDepartment(Mockito.any())).thenReturn(responseBody);

        RequestFieldsSnippet requestFieldsSnippet =
                requestFields(
                        fieldWithPath("name").description("학과명")
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
                        fieldWithPath("returnValue").description("저장된 학과의 번호")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Integer"))
                );
        postWebTestClient(requestBody, "/department").expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(createConsumer("/savedepartment", requestFieldsSnippet, responseFieldsSnippet));
    }

    @Test
    @DisplayName("학과 목록 조회")
    void findDepartmentList() {
        List<DepartmentListResponse> list = new ArrayList<>();
        list.add(DepartmentListResponse.builder()
                .id(1L)
                .name("경영학과")
                .build());
        list.add(DepartmentListResponse.builder()
                .id(2L)
                .name("경제학과")
                .build());
        list.add(DepartmentListResponse.builder()
                .id(3L)
                .name("경영정보학과")
                .build());

        Mockito.when(
                departmentService.findDepartmentList(Mockito.any()))
                .thenReturn(Mono.just(new BaseExtentionResponse<>(list)));

        RequestParametersSnippet requestParametersSnippet =
                requestParameters(
                        parameterWithName("name").description("학과명 검색어")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")
                                )
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
                        fieldWithPath("returnValue.[].id").description("학과 번호")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Integer")),
                        fieldWithPath("returnValue.[].name").description("학과명")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String"))
                );

        String params = "?name=학과";

        getWebTestClient("/department" + params).expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(createConsumer("/finddepartmentlist", requestParametersSnippet, responseFieldsSnippet));
    }

}
