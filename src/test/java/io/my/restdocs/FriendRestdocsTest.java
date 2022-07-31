package io.my.restdocs;

import io.my.base.base.RestDocAttributes;
import io.my.base.base.RestdocsBase;
import io.my.base.payload.BaseExtentionResponse;
import io.my.base.payload.BaseResponse;
import io.my.friend.payload.response.FriendListResponse;
import io.my.friend.payload.response.SearchFriendsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

class FriendRestdocsTest extends RestdocsBase {

    @Test
    @DisplayName("친구 목록 조회 API")
    void getFriendsList() {
        BaseExtentionResponse<List<FriendListResponse>> responseBody = new BaseExtentionResponse<>();
        List<FriendListResponse> list = new ArrayList<>();

        FriendListResponse response = new FriendListResponse();
        response.setName("name");
        response.setNickname("nickname");
        response.setEmail(EMAIL);
        response.setUserId(1L);
        response.setUserImageUrl("mysend.co.kr:8080/image/file_name.jpg");
        response.setUserImageId(1L);

        list.add(response);
        responseBody.setReturnValue(list);

        Mockito.when(friendService.getFriends()).thenReturn(Mono.just(responseBody));

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
                        fieldWithPath("returnValue.[].id").description("친구 테이블의 번호")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Integer")),
                        fieldWithPath("returnValue.[].userId").description("회원 테이블의 번호")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Integer")),
                        fieldWithPath("returnValue.[].name").description("이름")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("returnValue.[].nickname").description("닉네임")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("returnValue.[].email").description("이메일")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("returnValue.[].userImageId").description("이미지 번호")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Integer")),
                        fieldWithPath("returnValue.[].userImageUrl").description("이미지 경로")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String"))
                );

        getWebTestClient("/friend").expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(createConsumer("/getfriendlist", responseFieldsSnippet));
    }

    @Test
    @DisplayName("친구 추가 API")
    void addFriend() {
        BaseResponse responseBody = new BaseResponse();
        Mockito.when(friendService.addFriend(Mockito.anyLong())).thenReturn(Mono.just(responseBody));

        RequestParametersSnippet requestParametersSnippet =
                requestParameters(
                        parameterWithName("id").description("추가할 친구 회원 번호")
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
                                        RestDocAttributes.format("Integer"))
                );

        String params = "?id=" + 1;

        postWebTestClient("/friend" + params).expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(createConsumer("/addfriend", requestParametersSnippet, responseFieldsSnippet));

    }


    @Test
    @DisplayName("친구 삭제 API")
    void removeFriend() {
        BaseResponse responseBody = new BaseResponse();
        Mockito.when(friendService.removeFriend(Mockito.anyLong())).thenReturn(Mono.just(responseBody));

        RequestParametersSnippet requestParametersSnippet =
                requestParameters(
                        parameterWithName("id").description("추가할 친구 회원 번호")
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
                                        RestDocAttributes.format("Integer"))
                );

        String params = "?id=" + 1;

        deleteWebTestClient("/friend" + params).expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(createConsumer("/removefriend", requestParametersSnippet, responseFieldsSnippet));

    }

    @Test
    @DisplayName("친구 검색 API")
    void searchFriends() {
        BaseExtentionResponse<List<SearchFriendsResponse>> responseBody = new BaseExtentionResponse<>();
        List<SearchFriendsResponse> list = new ArrayList<>();

        SearchFriendsResponse response = SearchFriendsResponse.builder()
                .email(EMAIL)
                .name("name")
                .nickname("nickname")
                .id(1L)
                .imageUrl("mysend.co.kr:8080/image/file_name.jpg")
                .build();

        list.add(response);
        responseBody.setReturnValue(list);

        Mockito.when(friendService.searchFriends(Mockito.any(), Mockito.any())).thenReturn(Mono.just(responseBody));

        RequestParametersSnippet requestParametersSnippet =
                requestParameters(
                        parameterWithName("id").description("마지막 회원 번호(페이징용)").optional()
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Integer")
                                ),
                        parameterWithName("name").description("이름")
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
                        fieldWithPath("returnValue.[].id").description("유저 번호")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Integer")),
                        fieldWithPath("returnValue.[].name").description("이름")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("returnValue.[].nickname").description("닉네임")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("returnValue.[].email").description("이메일")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("returnValue.[].imageUrl").description("이미지 경로")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String"))
                );

        String param = "?id=10&name=김";

        getWebTestClient("/friend/search" + param).expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(createConsumer("/searchfriends", responseFieldsSnippet));
    }

}
