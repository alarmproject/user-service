package io.my.restdocs;

import io.my.base.base.RestDocAttributes;
import io.my.base.base.RestdocsBase;
import io.my.base.payload.BaseResponse;
import io.my.user.payload.request.FindEmailRequest;
import io.my.user.payload.request.JoinRequest;
import io.my.user.payload.request.LoginRequest;
import io.my.user.payload.response.FindEmailResponse;
import io.my.user.payload.response.LoginResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;
import reactor.core.publisher.Mono;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

class UserRestdocsTest extends RestdocsBase {
    private final static String EMAIL = "universitycafeterialife@gmail.com";

    @Test
    @DisplayName("일반 Login API")
    public void login() {
        LoginRequest requestBody = new LoginRequest();
        requestBody.setEmail(EMAIL);
        requestBody.setPassword("password");

        LoginResponse responseBody = new LoginResponse();
        responseBody.setAuthorization(jwtUtil.createAccessToken(1L));
        responseBody.setId(1L);

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
                        fieldWithPath("authorization").description("JWT Token")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("id").description("회원 ID")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Integer"))
                );

        postWebTestClient(requestBody, "/user/login").expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(createConsumer("/userlogin", requestFieldsSnippet, responseFieldsSnippet));
    }

    @Test
    @DisplayName("일반 Join API")
    public void join() {
        JoinRequest requestBody = new JoinRequest();
        requestBody.setName("김보성");
        requestBody.setPassword("password");
        requestBody.setEmail(EMAIL);
        requestBody.setDeviceToken("deviceTokendeviceToken");
        requestBody.setNickname("nickname");
        requestBody.setType((byte) 0);
        requestBody.setCollegeId(1L);
        requestBody.setCollegeEmail(EMAIL);

        LoginResponse responseBody = new LoginResponse();
        responseBody.setAuthorization(jwtUtil.createAccessToken(1L));
        responseBody.setId(1L);

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
                        fieldWithPath("authorization").description("JWT Token")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("id").description("회원 ID")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Integer"))
                );

        postWebTestClient(requestBody, "/user/join").expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(createConsumer("/userjoin", requestFieldsSnippet, responseFieldsSnippet));
    }

    @Test
    @DisplayName("소셜 Login API")
    public void socialLogin() {
        LoginResponse responseBody = new LoginResponse();
        responseBody.setAuthorization(jwtUtil.createAccessToken(1L));
        responseBody.setId(1L);

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
                        fieldWithPath("authorization").description("JWT Token")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("id").description("회원 ID")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Integer"))
                );

        String params = "?" +
                "email" +
                "=" +
                EMAIL;

        getWebTestClient("/user/social/login" + params).expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(createConsumer("/usersociallogin", requestParametersSnippet, responseFieldsSnippet));
    }

    @Test
    @DisplayName("소셜 Join API")
    public void socialJoin() {
        JoinRequest requestBody = new JoinRequest();
        requestBody.setName("김보성");
        requestBody.setPassword("password");
        requestBody.setEmail(EMAIL);
        requestBody.setDeviceToken("deviceTokendeviceToken");
        requestBody.setNickname("nickname");
        requestBody.setType((byte) 0);
        requestBody.setCollegeId(1L);
        requestBody.setCollegeEmail(EMAIL);

        LoginResponse responseBody = new LoginResponse();
        responseBody.setAuthorization(jwtUtil.createAccessToken(1L));
        responseBody.setId(1L);

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
                        fieldWithPath("authorization").description("JWT Token")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("id").description("회원 ID")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Integer"))
                );

        postWebTestClient(requestBody, "/user/social/join").expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(createConsumer("/usersocialjoin", requestFieldsSnippet, responseFieldsSnippet));
    }

    @Test
    @DisplayName("백업용 이메일 등록 API")
    public void registFindEmail() {
        FindEmailRequest requestBody = new FindEmailRequest();
        requestBody.setEmail(EMAIL);
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
    public void findEmail() {
        FindEmailResponse responseBody = new FindEmailResponse();
        responseBody.setEmail(EMAIL);

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
                        fieldWithPath("email").description("이메일")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String"))
                );

        String params = "?" +
                "email" +
                "=" +
                EMAIL;

        getWebTestClient("/user/find/email" + params).expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(createConsumer("/userfindemail", requestParametersSnippet, responseFieldsSnippet));

    }

    @Test
    @DisplayName("비밀번호 변경 API")
    public void changePassword() {
        LoginRequest requestBody = new LoginRequest();
        requestBody.setPassword("password");
        requestBody.setEmail(EMAIL);
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

        patchWebTestClient(requestBody, "/user/change/password").expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(createConsumer("/userchangepassword", requestFieldsSnippet, responseFieldsSnippet));


    }


}
