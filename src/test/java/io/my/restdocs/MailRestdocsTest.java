package io.my.restdocs;

import io.my.base.base.RestDocAttributes;
import io.my.base.base.RestdocsBase;
import io.my.mail.payload.MailCodeResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;
import reactor.core.publisher.Mono;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

class MailRestdocsTest extends RestdocsBase {

    @Test
    @DisplayName("인증 코드 전송 API")
    void sendCode() {
        MailCodeResponse responseBody = new MailCodeResponse();
        responseBody.setMailCode(456);

        Mockito.when(mailService.sendJoinCodeMail(Mockito.anyString(), Mockito.anyInt())).thenReturn(Mono.just(responseBody));

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
                        fieldWithPath("mailCode").description("인증 코드")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Integer"))
                );

        String email = "universitycafeterialife@gmail.com";
        String params = "?" +
                "email" +
                "=" +
                email;

        getWebTestClient("/mail/code" + params).expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(createConsumer("/mailcode", requestParametersSnippet, responseFieldsSnippet));
    }
}
