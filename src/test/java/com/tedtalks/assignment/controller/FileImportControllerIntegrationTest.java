package com.tedtalks.assignment.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application.properties")
class FileImportControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void importCsv_Success() {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource(
                "title,author,date,views,likes,link\nTest Talk,Test Author,December 2023,1000,100,http://test.com"
                        .getBytes()) {
            @Override
            public String getFilename() {
                return "test.csv";
            }
        });

        webTestClient.post()
                .uri("/api/v1/ted-talks/import")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(body))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.successfulRecordCount").isEqualTo(1)
                .jsonPath("$.totalRecordCount").isEqualTo(1)
                .jsonPath("$.failedRecordCount").isEqualTo(0);
    }

    @Test
    void importCsv_EmptyFile() {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource(new byte[0]) {
            @Override
            public String getFilename() {
                return "empty.csv";
            }
        });

        webTestClient.post()
                .uri("/api/v1/ted-talks/import")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(body))
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void importCsv_InvalidFileType() {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new org.springframework.core.io.ByteArrayResource("Invalid content".getBytes()) {
            @Override
            public String getFilename() {
                return "test.txt";
            }
        });

        webTestClient.post()
                .uri("/api/v1/ted-talks/import")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(body))
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(String.class);

    }
}