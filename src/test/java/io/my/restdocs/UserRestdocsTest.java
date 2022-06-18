package io.my.restdocs;

import io.my.base.base.RestDocAttributes;
import io.my.base.base.RestdocsBase;
import io.my.base.payload.BaseExtentionResponse;
import io.my.base.payload.BaseResponse;
import io.my.user.payload.request.FindEmailRequest;
import io.my.user.payload.request.JoinRequest;
import io.my.user.payload.request.LoginRequest;
import io.my.user.payload.request.PatchUserPasswordRequest;
import io.my.user.payload.response.LoginResponse;
import io.my.user.payload.response.SearchUserResponse;
import io.my.user.payload.response.UserInfoResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.PathParametersSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

class UserRestdocsTest extends RestdocsBase {

    @Test
    @DisplayName("일반 Login API")
    void login() {
        LoginRequest requestBody = LoginRequest.builder().email(EMAIL).password("password").build();

        BaseExtentionResponse<LoginResponse> responseBody =
                new BaseExtentionResponse<>(
                        new LoginResponse(1L, jwtUtil.createAccessToken(1L)));

        Mockito.when(userService.login(Mockito.any())).thenReturn(Mono.just(responseBody));

        RequestFieldsSnippet requestFieldsSnippet =
                requestFields(
                        fieldWithPath("email").description("이메일")
                            .attributes(
                                    RestDocAttributes.length(0),
                                    RestDocAttributes.format("String")),
                        fieldWithPath("password").description("비밀번호")
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
                        fieldWithPath("returnValue.authorization").description("JWT Token")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("returnValue.id").description("회원 ID")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Integer"))
                );

        postWebTestClientUnAuth(requestBody, "/user/login").expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(createConsumer("/userlogin", requestFieldsSnippet, responseFieldsSnippet));
    }

    @Test
    @DisplayName("일반 Join API")
    void join() {
        JoinRequest requestBody = new JoinRequest();
        requestBody.setName("김보성");
        requestBody.setPassword("password");
        requestBody.setEmail(EMAIL);
        requestBody.setDeviceToken("deviceTokendeviceToken");
        requestBody.setNickname("nickname");
        requestBody.setType((byte) 0);
        requestBody.setCollegeId(1L);
        requestBody.setCollegeEmail(EMAIL);
        requestBody.setClassOf(13);
        requestBody.setImageId(1L);

        BaseExtentionResponse<LoginResponse> responseBody =
                new BaseExtentionResponse<>(
                        new LoginResponse(1L, jwtUtil.createAccessToken(1L)));

        Mockito.when(userService.join(Mockito.any())).thenReturn(Mono.just(responseBody));

        RequestFieldsSnippet requestFieldsSnippet =
                requestFields(
                        fieldWithPath("name").description("이름")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("email").description("이메일")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("password").description("비밀번호")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("nickname").description("닉네임")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("deviceToken").description("디바이스 토큰")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("type").description("가입 유형")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("collegeId").description("대학교 번호").optional()
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Integer")),
                        fieldWithPath("collegeEmail").description("대학교 이메일").optional()
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("classOf").description("학번").optional()
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Integer")),
                        fieldWithPath("imageId").description("이미지 번호").optional()
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Integer")),
                        fieldWithPath("id").description("고유번호(요청 파라미터 아님)").optional()
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
                        fieldWithPath("returnValue.authorization").description("JWT Token")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("returnValue.id").description("회원 ID")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Integer"))
                );

        postWebTestClientUnAuth(requestBody, "/user/join").expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(createConsumer("/userjoin", requestFieldsSnippet, responseFieldsSnippet));
    }

    @Test
    @DisplayName("소셜 Login API")
    void socialLogin() {
        BaseExtentionResponse<LoginResponse> responseBody =
                new BaseExtentionResponse<>(
                        new LoginResponse(1L, jwtUtil.createAccessToken(1L)));

        Mockito.when(userService.socialLogin(Mockito.anyString())).thenReturn(Mono.just(responseBody));

        RequestParametersSnippet requestParametersSnippet =
                requestParameters(
                        parameterWithName("email").description("메일 주소")
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
                        fieldWithPath("returnValue.authorization").description("JWT Token")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("returnValue.id").description("회원 ID")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Integer"))
                );

        String params = "?" +
                "email" +
                "=" +
                EMAIL;

        getWebTestClientUnAuth("/user/social/login" + params).expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(createConsumer("/usersociallogin", requestParametersSnippet, responseFieldsSnippet));
    }

    @Test
    @DisplayName("소셜 Join API")
    void socialJoin() {
        JoinRequest requestBody = new JoinRequest();
        requestBody.setName("김보성");
        requestBody.setPassword("password");
        requestBody.setEmail(EMAIL);
        requestBody.setDeviceToken("deviceTokendeviceToken");
        requestBody.setNickname("nickname");
        requestBody.setType((byte) 0);
        requestBody.setCollegeId(1L);
        requestBody.setCollegeEmail(EMAIL);
        requestBody.setClassOf(13);
        requestBody.setImageId(1L);

        BaseExtentionResponse<LoginResponse> responseBody =
                new BaseExtentionResponse<>(
                        new LoginResponse(1L, jwtUtil.createAccessToken(1L)));

        Mockito.when(userService.socialJoin(Mockito.any())).thenReturn(Mono.just(responseBody));

        RequestFieldsSnippet requestFieldsSnippet =
                requestFields(
                        fieldWithPath("name").description("이름")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("email").description("이메일")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("password").description("비밀번호")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("nickname").description("닉네임")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("deviceToken").description("디바이스 토큰")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("type").description("가입 유형")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("collegeId").description("대학교 번호").optional()
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Integer")),
                        fieldWithPath("collegeEmail").description("대학교 이메일").optional()
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("classOf").description("학번").optional()
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Integer")),
                        fieldWithPath("imageId").description("이미지 번호").optional()
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Integer")),
                        fieldWithPath("id").description("고유번호(요청 파라미터 아님)").optional()
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
                        fieldWithPath("returnValue.authorization").description("JWT Token")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("returnValue.id").description("회원 ID")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Integer"))
                );

        postWebTestClientUnAuth(requestBody, "/user/social/join").expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(createConsumer("/usersocialjoin", requestFieldsSnippet, responseFieldsSnippet));
    }

    @Test
    @DisplayName("백업용 이메일 등록 API")
    void registFindEmail() {
        FindEmailRequest requestBody = FindEmailRequest.builder().email(EMAIL).build();
        BaseResponse responseBody = new BaseResponse();
        Mockito.when(userService.registFindEmail(Mockito.anyString())).thenReturn(Mono.just(responseBody));

        RequestFieldsSnippet requestFieldsSnippet =
                requestFields(
                        fieldWithPath("email").description("이메일")
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
                                        RestDocAttributes.format("Integer"))
                );

        postWebTestClient(requestBody, "/user/find/email").expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(createConsumer("/userregistfindemail", requestFieldsSnippet, responseFieldsSnippet));
    }

    @Test
    @DisplayName("아이디(이메일) 찾기 API")
    void findEmail() {
        BaseExtentionResponse<String> responseBody = new BaseExtentionResponse<>();
        responseBody.setReturnValue(EMAIL);

        Mockito.when(userService.findEmail(Mockito.anyString())).thenReturn(Mono.just(responseBody));

        RequestParametersSnippet requestParametersSnippet =
                requestParameters(
                        parameterWithName("email").description("메일 주소")
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
                        fieldWithPath("returnValue").description("이메일")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String"))
                );

        String params = "?" +
                "email" +
                "=" +
                EMAIL;

        getWebTestClientUnAuth("/user/find/email" + params).expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(createConsumer("/userfindemail", requestParametersSnippet, responseFieldsSnippet));

    }

    @Test
    @DisplayName("비밀번호 변경 API")
    void changePassword() {
        LoginRequest requestBody = LoginRequest.builder().password("password").email(EMAIL).build();
        BaseResponse responseBody = new BaseResponse();

        Mockito.when(userService.changePassword(Mockito.anyString(), Mockito.anyString())).thenReturn(Mono.just(responseBody));

        RequestFieldsSnippet requestFieldsSnippet =
                requestFields(
                        fieldWithPath("email").description("이메일")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("password").description("변경할 비밀번호")
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
                                        RestDocAttributes.format("Integer"))
                );

        patchWebTestClientUnAuth(requestBody, "/user/password").expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(createConsumer("/changepassword", requestFieldsSnippet, responseFieldsSnippet));
    }

    @Test
    @DisplayName("회원 검색 API")
    void searchUser() {
        BaseExtentionResponse<List<SearchUserResponse>> responseBody = new BaseExtentionResponse<>();
        List<SearchUserResponse> list = new ArrayList<>();

        SearchUserResponse entity = new SearchUserResponse();
        entity.setId(1L);
        entity.setEmail(EMAIL);
        entity.setName("name");
        entity.setNickname("nickname");
        entity.setFileName("fileName");

        list.add(entity);
        responseBody.setReturnValue(list);

        Mockito.when(userService.searchUserByName(Mockito.any(), Mockito.anyString())).thenReturn(Mono.just(responseBody));
        Mockito.when(userService.searchUserByNickname(Mockito.any(), Mockito.anyString())).thenReturn(Mono.just(responseBody));

        RequestParametersSnippet requestParametersSnippet =
                requestParameters(
                        parameterWithName("search").description("검색어")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")
                                ),
                        parameterWithName("searchType").description("검색어 타입, 0: 이름(default), 1: 닉네임").optional()
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Integer")
                                ),
                        parameterWithName("isSameCollege").description("같은 학교만 검색(default: false)").optional()
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Boolean")
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
                        fieldWithPath("returnValue.[].id").description("회원 번호")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Integer")),
                        fieldWithPath("returnValue.[].email").description("회원 이메일")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("returnValue.[].nickname").description("회원 닉네임")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("returnValue.[].name").description("회원 이름")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("returnValue.[].fileName").description("이미지 경로(수정 예정)")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String"))
                );

        String params = "?" +
                "search=" + "nickname" +
                "&searchType=1"
        ;

        getWebTestClient("/user/search" + params).expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(createConsumer("/usersearch", requestParametersSnippet, responseFieldsSnippet));
    }

    @Test
    @DisplayName("프로필 사진 변경 API")
    void changeImage() {
        Mockito.when(userService.changeImage(Mockito.anyLong())).thenReturn(Mono.just(new BaseResponse()));

        RequestParametersSnippet requestParametersSnippet =
                requestParameters(
                        parameterWithName("id").description("이미지 번호")
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

        String params = "?id=1";

        patchWebTestClient("/user/image" + params).expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(createConsumer("/changeimage", requestParametersSnippet, responseFieldsSnippet));
    }

    @Test
    @DisplayName("닉네 변경 API")
    void changeNickname() {
        Mockito.when(userService.changeNickname(Mockito.anyString())).thenReturn(Mono.just(new BaseResponse()));

        RequestParametersSnippet requestParametersSnippet =
                requestParameters(
                        parameterWithName("nickname").description("닉네임")
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
                                        RestDocAttributes.format("Integer"))
                );

        String params = "?nickname=nickname";

        patchWebTestClient("/user/nickname" + params).expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(createConsumer("/changenickname", requestParametersSnippet, responseFieldsSnippet));
    }

    @Test
    @DisplayName("이메일 중복체크 API")
    void checkEmail() {
        Mockito.when(userService.checkEmail(Mockito.anyString())).thenReturn(Mono.just(new BaseExtentionResponse<>(Boolean.FALSE)));

        RequestParametersSnippet requestParametersSnippet =
                requestParameters(
                        parameterWithName("email").description("이메일")
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
                        fieldWithPath("returnValue").description("true: 이미 존재, false: 없는 이메일")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Boolean"))
                );

        String params = "?email=" + EMAIL;

        getWebTestClientUnAuth("/user/check/email" + params).expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(createConsumer("/checkemail", requestParametersSnippet, responseFieldsSnippet));
    }

    @Test
    @DisplayName("닉네임 중복체크 API")
    void checkNickname() {
        Mockito.when(userService.checkNickname(Mockito.anyString())).thenReturn(Mono.just(new BaseExtentionResponse<>(Boolean.FALSE)));

        RequestParametersSnippet requestParametersSnippet =
                requestParameters(
                        parameterWithName("nickname").description("닉네임")
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
                        fieldWithPath("returnValue").description("true: 이미 존재, false: 없는 이메일")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Boolean"))
                );

        String params = "?nickname=nickname";

        getWebTestClientUnAuth("/user/check/nickname" + params).expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(createConsumer("/checknickname", requestParametersSnippet, responseFieldsSnippet));
    }

    @Test
    @DisplayName("사용자 이미지 링크 조회")
    void getImageLink() {
        String link = "http://mysend.co.kr:8080/image?fileName=65632a55-0280-4afb-b19d-c62fdf15b87e_charactor.jpeg";
        Mockito.when(userService.getImageLink(Mockito.any())).thenReturn(Mono.just(new BaseExtentionResponse<>(link)));

        PathParametersSnippet pathParametersSnippet = pathParameters(
                parameterWithName("id").description("유저 번호")
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
                        fieldWithPath("returnValue").description("이미지 주소")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("link"))
                );

        getWebTestClientPathVariable(1, "/user/image/{id}").expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(createConsumer("/getimagelink", pathParametersSnippet, responseFieldsSnippet));
    }

    @Test
    @DisplayName("사용자 정보 조회")
    void getUserInfo() {
        var responseBody = new BaseExtentionResponse<>(
                UserInfoResponse.builder()
                        .nickname("nickname")
                        .name("name")
                        .email("email")
                        .collegeEmail("email@seoul.ac.kr")
                        .classOf(13)
                        .imageUrl("http://mysend.co.kr:8080/image?fileName=65632a55-0280-4afb-b19d-c62fdf15b87e_charactor.jpeg")
                        .imageId(1L)
                        .collegeId(1L)
                        .collegeName("서울대학교")
                        .build()
        );

        Mockito.when(userService.getUserInfo(Mockito.any())).thenReturn(Mono.just(responseBody));

        PathParametersSnippet pathParametersSnippet = pathParameters(
                parameterWithName("id").description("유저 번호")
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
                        fieldWithPath("returnValue.nickname").description("닉네임")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("returnValue.name").description("이름")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("returnValue.email").description("이메일")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("returnValue.collegeEmail").description("학교 이메일")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("returnValue.collegeId").description("학교 번호")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Integer")),
                        fieldWithPath("returnValue.collegeName").description("학교명")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("returnValue.classOf").description("학번")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Integer")),
                        fieldWithPath("returnValue.imageId").description("이미지 번호")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Integer")),
                        fieldWithPath("returnValue.imageUrl").description("이미지 주소")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("returnValue.friendsCount").description("친구 수")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Integer"))
                );

        getWebTestClientPathVariable(1, "/user/{id}").expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(createConsumer("/getuserinfo", pathParametersSnippet, responseFieldsSnippet));
    }

    @Test
    @DisplayName("비밀번호 변경 API")
    void changeUserPassword() {
        PatchUserPasswordRequest requestBody = PatchUserPasswordRequest.builder().password("password").newPassword("newPasswod").build();
        BaseResponse responseBody = new BaseResponse();

        Mockito.when(userService.changePassword(Mockito.any())).thenReturn(Mono.just(responseBody));

        RequestFieldsSnippet requestFieldsSnippet =
                requestFields(
                        fieldWithPath("password").description("기존 비밀번호")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("newPassword").description("변경할 비밀번호")
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
                                        RestDocAttributes.format("Integer"))
                );

        patchWebTestClientUnAuth(requestBody, "/user/change/password").expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(createConsumer("/userchangepassword", requestFieldsSnippet, responseFieldsSnippet));
    }

    @Test
    @DisplayName("이메일 변경 API")
    void changeUserEmail() {
        Mockito.when(userService.changeUserEmail(Mockito.anyString())).thenReturn(Mono.just(new BaseResponse()));

        RequestParametersSnippet requestParametersSnippet =
                requestParameters(
                        parameterWithName("email").description("변경할 이메일")
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
                                        RestDocAttributes.format("Integer"))
                );
        String params = "?email=" + EMAIL;

        patchWebTestClient("/user/change/email" + params).expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(createConsumer("/userchangeemail", requestParametersSnippet, responseFieldsSnippet));
    }

    @Test
    @DisplayName("유저 학교 변경 API")
    void changeUserCollege() {
        Mockito.when(userService.changeUserCollege(Mockito.anyLong(), Mockito.anyString())).thenReturn(Mono.just(new BaseResponse()));

        RequestParametersSnippet requestParametersSnippet =
                requestParameters(
                        parameterWithName("collegeId").description("변경할 학교 번호")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Integer")
                                ),
                        parameterWithName("collegeEmail").description("변경할 학교 이메일")
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
                                        RestDocAttributes.format("Integer"))
                );
        String params = "?collegeId=1&&collegeEmail=" + EMAIL;

        patchWebTestClient("/user/change/college" + params).expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(createConsumer("/userchangecollege", requestParametersSnippet, responseFieldsSnippet));
    }


}
