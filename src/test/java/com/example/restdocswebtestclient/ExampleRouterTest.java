package com.example.restdocswebtestclient;

import com.fasterxml.jackson.databind.JsonNode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.example.restdocswebtestclient.ApiDocumentUtils.getDocumentRequest;
import static com.example.restdocswebtestclient.ApiDocumentUtils.getDocumentResponse;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
@ExtendWith({SpringExtension.class, RestDocumentationExtension.class})
@SpringBootTest
public class ExampleRouterTest {

    WebTestClient webTestClient;

    @Autowired
    private ExampleRouter router;

    @Autowired
    private ExampleHandler handler;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        webTestClient = WebTestClient.bindToRouterFunction(router.routes(handler))
                .configureClient()
                .filter(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    public void restDocsTest() {
        // Arrange

        // Act
        webTestClient.get()
                .uri("/user/listAll")
                .exchange()
                .expectStatus().isOk()
                .expectBody(JsonNode.class)
                .value(result -> {
                    Assertions.assertThat(result.get("result")).isNotNull();
                    Assertions.assertThat(result.get("result").get(0).get("id").textValue()).isEqualTo("id_1");
                })
                .consumeWith(document("user-listAll",
                        getDocumentRequest(), getDocumentResponse(),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.ARRAY).description("목록"),
                                fieldWithPath("result[].id").type(JsonFieldType.STRING).description("아이디"),
                                fieldWithPath("result[].name").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("result[].password").type(JsonFieldType.STRING).description("비밀번호")
                        )
                        ));


        // Assert
    }
}
