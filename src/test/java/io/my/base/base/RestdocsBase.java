package io.my.base.base;

import io.my.base.context.JwtContextWebFilter;
import io.my.base.util.JwtUtil;
import io.my.college.CollegeService;
import io.my.mail.MailService;
import io.my.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;
import org.springframework.restdocs.request.RequestPartsSnippet;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.function.Consumer;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class RestdocsBase {
    protected String authorization;

    protected WebTestClient webTestClient;
    protected Snippet defaultRequestHeader;

    private static String BASE_URL = "http://mysend.co.kr:8080/";

    @Autowired
    protected JwtUtil jwtUtil;

    @Autowired
    protected JwtContextWebFilter jwtContextWebFilter;

    @MockBean
    protected MailService mailService;

    @MockBean
    protected UserService userService;

    @MockBean
    protected CollegeService collegeService;

    @BeforeEach
    void setUp(ApplicationContext applicationContext,
               RestDocumentationContextProvider restDocumentation) {
        authorization = "Bearer " + jwtUtil.createAccessToken(1L);

        this.webTestClient = WebTestClient.bindToApplicationContext(applicationContext)
                .configureClient()
                .baseUrl(BASE_URL)
                .filter(documentationConfiguration(restDocumentation))
                .filter(documentationConfiguration(restDocumentation).snippets().withEncoding("UTF-8"))
                .build();

        this.defaultRequestHeader = requestHeaders(
                headerWithName("Authorization")
                        .description("JWT 인증 토큰")
                        .optional()
                        .attributes(
                                RestDocAttributes.length(""),
                                RestDocAttributes.format("")
                        )
        );
    }

    protected WebTestClient.ResponseSpec getWebTestClient(String uri) {
        return this.webTestClient.get().uri(uri).header(HttpHeaders.AUTHORIZATION, authorization).accept(MediaType.APPLICATION_JSON).exchange();
    }

    protected WebTestClient.ResponseSpec getWebTestClient(Object body, String uri) {
        return this.webTestClient.method(HttpMethod.GET).uri(uri).header(HttpHeaders.AUTHORIZATION, authorization).accept(MediaType.APPLICATION_JSON).bodyValue(body).exchange();
    }

    protected WebTestClient.ResponseSpec postWebTestClient(Object body, String uri) {
        return this.webTestClient.post().uri(uri).header(HttpHeaders.AUTHORIZATION, authorization).accept(MediaType.APPLICATION_JSON).bodyValue(body).exchange();
    }

    protected WebTestClient.ResponseSpec postWebTestClient(BodyInserters.MultipartInserter multipartInserter, String uri) {
        return this.webTestClient.post().uri(uri).header(HttpHeaders.AUTHORIZATION, authorization).body(multipartInserter).exchange();
    }

    protected WebTestClient.ResponseSpec postWebTestClient(String uri) {
        return this.webTestClient.post().uri(uri).accept(MediaType.APPLICATION_JSON).exchange();
    }

    protected WebTestClient.ResponseSpec putWebTestClient(Object body, String uri) {
        return this.webTestClient.put().uri(uri).header(HttpHeaders.AUTHORIZATION, authorization).accept(MediaType.APPLICATION_JSON).bodyValue(body).exchange();
    }

    protected WebTestClient.ResponseSpec patchWebTestClient(Object body, String uri) {
        return this.webTestClient.patch().uri(uri).header(HttpHeaders.AUTHORIZATION, authorization).accept(MediaType.APPLICATION_JSON).bodyValue(body).exchange();
    }

    protected WebTestClient.ResponseSpec patchWebTestClient(String uri) {
        return this.webTestClient.patch().uri(uri).header(HttpHeaders.AUTHORIZATION, authorization).accept(MediaType.APPLICATION_JSON).exchange();
    }

    protected WebTestClient.ResponseSpec deleteWebTestClient(String uri) {
        return this.webTestClient.delete().uri(uri).header(HttpHeaders.AUTHORIZATION, authorization).accept(MediaType.APPLICATION_JSON).exchange();
    }

    protected WebTestClient.ResponseSpec getWebTestClientNotAuth(String uri) {
        return this.webTestClient.get().uri(uri).accept(MediaType.APPLICATION_JSON).exchange();
    }

    protected WebTestClient.ResponseSpec getWebTestClientNotAuth(Object body, String uri) {
        return this.webTestClient.method(HttpMethod.GET).uri(uri).accept(MediaType.APPLICATION_JSON).bodyValue(body).exchange();
    }

    protected WebTestClient.ResponseSpec postWebTestClientNotAuth(Object body, String uri) {
        return this.webTestClient.post().uri(uri).accept(MediaType.APPLICATION_JSON).bodyValue(body).exchange();
    }

    protected WebTestClient.ResponseSpec putWebTestClientNotAuth(Object body, String uri) {
        return this.webTestClient.put().uri(uri).accept(MediaType.APPLICATION_JSON).bodyValue(body).exchange();
    }

    protected Consumer<EntityExchangeResult<byte[]>> createConsumer(
            String fileName,
            RequestFieldsSnippet requestFieldsSnippet,
            ResponseFieldsSnippet responseFieldsSnippet) {
        return document(
                this.getClass().getSimpleName().toLowerCase() + fileName,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                defaultRequestHeader,
                requestFieldsSnippet,
                responseFieldsSnippet);
    }

    protected Consumer<EntityExchangeResult<byte[]>> createConsumer(
            String fileName,
            RequestFieldsSnippet requestFieldsSnippet) {
        return document(
                this.getClass().getSimpleName().toLowerCase() + fileName,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                defaultRequestHeader,
                requestFieldsSnippet);
    }

    protected Consumer<EntityExchangeResult<byte[]>> createConsumer(
            String fileName,
            ResponseFieldsSnippet responseFieldsSnippet) {
        return document(
                this.getClass().getSimpleName().toLowerCase() + fileName,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                defaultRequestHeader,
                responseFieldsSnippet);
    }

    protected Consumer<EntityExchangeResult<byte[]>> createConsumer(
            String fileName,
            RequestParametersSnippet requestParametersSnippet,
            ResponseFieldsSnippet responseFieldsSnippet) {
        return document(
                this.getClass().getSimpleName().toLowerCase() + fileName,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                defaultRequestHeader,
                requestParametersSnippet,
                responseFieldsSnippet);
    }

    protected Consumer<EntityExchangeResult<byte[]>> createConsumer(
            String fileName,
            RequestParametersSnippet requestParametersSnippet) {
        return document(
                this.getClass().getSimpleName().toLowerCase() + fileName,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                defaultRequestHeader,
                requestParametersSnippet);
    }

    protected Consumer<EntityExchangeResult<byte[]>> createConsumer(
            String fileName,
            RequestPartsSnippet requestPartsSnippet,
            ResponseFieldsSnippet responseSnippet) {
        return document(
                this.getClass().getSimpleName().toLowerCase() + fileName,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                defaultRequestHeader,
                requestPartsSnippet,
                responseSnippet);
    }

    protected Consumer<EntityExchangeResult<byte[]>> createConsumerAuthorization(
            String fileName,
            ResponseFieldsSnippet responseSnippet) {

        Snippet responseHeader = responseHeaders(
                headerWithName("Authorization")
                        .description("갱신된 JWT 인증 토큰")
                        .optional()
                        .attributes(
                                RestDocAttributes.length(""),
                                RestDocAttributes.format("")
                        )
        );

        return document(
                this.getClass().getSimpleName().toLowerCase() + fileName,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                defaultRequestHeader,
                responseHeader,
                responseSnippet);
    }



}
