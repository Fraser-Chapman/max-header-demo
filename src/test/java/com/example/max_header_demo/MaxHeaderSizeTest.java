package com.example.max_header_demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import wiremock.org.eclipse.jetty.http.HttpStatus;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ActiveProfiles("test")
@AutoConfigureWireMock(port = MaxHeaderSizeTest.WIRE_MOCK_PORT)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = {MaxHeaderDemoApplication.class})
public class MaxHeaderSizeTest {

    static final int WIRE_MOCK_PORT = 1035;
    private static final String LOCALHOST = "localhost";
    private static final String SOME_ENDPOINT = "/test/some-endpoint";

    @Autowired
    private ApplicationContext applicationContext;

    private WebTestClient webClient;

    @BeforeEach
    public void beforeEach() {
        webClient = WebTestClient.bindToApplicationContext(applicationContext).build();
    }

    @Test
    void givenRequestExceedsMaxHeaderSize_returns431() {
        // Given
        stubFor(get(urlEqualTo(SOME_ENDPOINT)).withHost(equalTo(LOCALHOST)).withPort(WIRE_MOCK_PORT)
                .willReturn(ok())
        );

        final String giantHeader = "x".repeat(9000);

        webClient.get().uri("/test/some-endpoint")
                .header("x-massive-header", giantHeader)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.REQUEST_HEADER_FIELDS_TOO_LARGE_431);
    }
}
