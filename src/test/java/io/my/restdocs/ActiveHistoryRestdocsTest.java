package io.my.restdocs;

import io.my.active.payload.response.ActiveHistoryResponse;
import io.my.base.base.RestDocAttributes;
import io.my.base.base.RestdocsBase;
import io.my.base.payload.BaseExtentionPagingResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

class ActiveHistoryRestdocsTest extends RestdocsBase {

    @Test
    @DisplayName("활동 내역 조회 API")
    void findActiveHistory() {
        BaseExtentionPagingResponse<List<ActiveHistoryResponse>> responseBody = new BaseExtentionPagingResponse<>();
        List<ActiveHistoryResponse> list = new ArrayList<>();
        for (long index = 12; index > 7; index--) {
            ActiveHistoryResponse response = ActiveHistoryResponse.builder()
                    .id(index)
                    .content("수업 ABC" + index + " 의 시간이 변경되었습니다.")
                    .regDateTime(dateUtil.localDateTimeToUnixTime(LocalDateTime.now()))
                    .modDateTime(dateUtil.localDateTimeToUnixTime(LocalDateTime.now()))
                    .friendsUserId(null)
                    .isFollow(Boolean.FALSE)
                    .imageUrl("http://mysend.co.kr:8080/image/image?fileName=ae679669-1c14-43d0-80d0-23e4160b76fa_default_profile_7.png")
                    .build();

            list.add(response);
        }
        responseBody.setReturnValue(list);
        responseBody.setId(list.get(list.size() - 1).getId());

        Mockito.when(activeService.findActiveHistory(Mockito.anyLong())).thenReturn(Mono.just(responseBody));

        RequestParametersSnippet requestParametersSnippet =
                requestParameters(
                        parameterWithName("id").description("페이징을 위한 ID 값").optional()
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Integer")
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
                        fieldWithPath("id").description("마지막 번호")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Integer")),
                        fieldWithPath("returnValue.[].id").description("활동 내역 번호")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Integer")),
                        fieldWithPath("returnValue.[].content").description("활동 내역")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("returnValue.[].imageUrl").description("이미지 경로")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("returnValue.[].friendsUserId").description("친구추가한 유저 번호").optional()
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Integer")),
                        fieldWithPath("returnValue.[].isFollow").description("나의 상대방에 대한 Follow 여부")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Boolean")),
                        fieldWithPath("returnValue.[].regDateTime").description("생성 시각")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("UnixTime")),
                        fieldWithPath("returnValue.[].modDateTime").description("수정 시각")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("UnixTime"))
                );

        String params = "?id=15";

        getWebTestClient("/active/history" + params).expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(createConsumer("/activehistory", requestParametersSnippet, responseFieldsSnippet));
    }

}
