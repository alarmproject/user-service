package io.my.restdocs;

import io.my.base.base.RestDocAttributes;
import io.my.base.base.RestdocsBase;
import io.my.base.payload.BaseExtentionResponse;
import io.my.professor.payload.request.SaveProfessorRequest;
import io.my.professor.payload.response.ProfessorListResponse;
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

class ProfessorRestdocsTest extends RestdocsBase {

    @Test
    @DisplayName("교수 등록 API")
    void saveProfessor() {
        Mockito.when(professorService.saveProfessor(Mockito.any())).thenReturn(Mono.just(new BaseExtentionResponse<>(1L)));
        SaveProfessorRequest requestBody = new SaveProfessorRequest();
        requestBody.setName("김교수");
        requestBody.setDepartmentId(1L);
        requestBody.setImageId(1L);

        RequestFieldsSnippet requestFieldsSnippet =
                requestFields(
                        fieldWithPath("name").description("교수명")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("departmentId").description("학과 번호")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Integer")),
                        fieldWithPath("imageId").description("사진 번호").optional()
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Integer"))
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
                        fieldWithPath("returnValue").description("교수 번호")
                               .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Integer"))
                );

        postWebTestClient(requestBody, "/professor").expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(createConsumer("/saveprofessor", requestFieldsSnippet, responseFieldsSnippet));

    }

    @Test
    @DisplayName("교수 검색 API")
    void findProfessorList() {
        List<ProfessorListResponse> list = new ArrayList<>();
        Mockito.when(professorService.findProfessorList(Mockito.any())).thenReturn(Mono.just(new BaseExtentionResponse<>(list)));

        list.add(ProfessorListResponse.builder()
                .id(1L)
                .name("김교수")
                .departmentName("경영학과")
                .imageUrl("http://mysend.co.kr:8080/image/image?fileName=c91a6281-d9bd-4119-95ac-d57c17c0451a_charactor.jpeg")
                .build());
        list.add(ProfessorListResponse.builder()
                .id(2L)
                .name("이교수")
                .departmentName("경영학과")
                .imageUrl("http://mysend.co.kr:8080/image/image?fileName=c91a6281-d9bd-4119-95ac-d57c17c0451a_charactor.jpeg")
                .build());
        ;

        RequestParametersSnippet requestParametersSnippet =
                requestParameters(
                        parameterWithName("name").description("교수명")
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
                        fieldWithPath("returnValue.[].id").description("교수 번호")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Integer")),
                        fieldWithPath("returnValue.[].name").description("교수명")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("returnValue.[].departmentName").description("학과명")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("returnValue.[].imageUrl").description("사진 주소")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String"))
                );

        String params = "?name=교수";

        getWebTestClient("/professor" + params).expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(createConsumer("/findprofessorlist", requestParametersSnippet, responseFieldsSnippet));
    }

}
