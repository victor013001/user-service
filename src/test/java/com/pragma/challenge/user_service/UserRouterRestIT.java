package com.pragma.challenge.user_service;

import static com.pragma.challenge.user_service.util.RegisterBootcampDtoData.getInvalidRegisterBootcampDto;
import static com.pragma.challenge.user_service.util.RegisterBootcampDtoData.getRegisterBootcampDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.pragma.challenge.user_service.domain.constants.ConstantsHeaders;
import com.pragma.challenge.user_service.domain.constants.ConstantsRoute;
import com.pragma.challenge.user_service.domain.enums.ServerResponses;
import com.pragma.challenge.user_service.domain.exceptions.StandardError;
import com.pragma.challenge.user_service.infrastructure.adapters.bootcamp_service.util.ConstantsGateway;
import com.pragma.challenge.user_service.infrastructure.adapters.persistence.entity.UserEntity;
import com.pragma.challenge.user_service.infrastructure.adapters.persistence.repository.UserBootcampRepository;
import com.pragma.challenge.user_service.infrastructure.adapters.persistence.repository.UserRepository;
import com.pragma.challenge.user_service.infrastructure.entrypoints.dto.DefaultServerResponse;
import java.util.List;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@ActiveProfiles("it")
@AutoConfigureWebTestClient
@AutoConfigureWireMock(port = 0)
@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserRouterRestIT {
  @Autowired WebTestClient webTestClient;
  @Autowired UserRepository userRepository;
  @Autowired UserBootcampRepository userBootcampRepository;

  @BeforeEach
  void setUp() {
    userRepository
        .saveAll(
            List.of(
                UserEntity.builder().name("Victor").email("victor@example.com").age(24).build()))
        .blockLast();
  }

  @Test
  void registerBootcamps() {
    WireMock.stubFor(
        WireMock.get(
                WireMock.urlPathEqualTo(
                    ConstantsGateway.BOOTCAMP_SERVICE_BASE_PATH
                        + ConstantsGateway.BOOTCAMP_SERVICE_EXISTS_PATH))
            .withQueryParam(ConstantsGateway.ID_PARAM, WireMock.matching(".*"))
            .willReturn(
                WireMock.aResponse()
                    .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .withBodyFile("bootcampsExists.json")));

    webTestClient
        .post()
        .uri(ConstantsRoute.BASE_PATH + ConstantsRoute.BOOTCAMP_PATH)
        .header(ConstantsHeaders.AUTHORIZATION, ConstantsHeaders.BEARER_PREFIX + " 1")
        .bodyValue(getRegisterBootcampDto())
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody(DefaultServerResponse.class)
        .consumeWith(
            exchangeResult -> {
              var response = exchangeResult.getResponseBody();
              assertNotNull(response);
              assertEquals(
                  ServerResponses.SUCCESS_REGISTER_TO_BOOTCAMP.getMessage(), response.data());
            });
  }

  @Test
  void registerBootcampsBadRequest() {
    webTestClient
        .post()
        .uri(ConstantsRoute.BASE_PATH + ConstantsRoute.BOOTCAMP_PATH)
        .header(ConstantsHeaders.AUTHORIZATION, ConstantsHeaders.BEARER_PREFIX + " 1")
        .bodyValue(getInvalidRegisterBootcampDto())
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody(
            new ParameterizedTypeReference<DefaultServerResponse<Object, StandardError>>() {})
        .consumeWith(
            exchangeResult -> {
              var response = exchangeResult.getResponseBody();
              assertNotNull(response);
              assertEquals(
                  ServerResponses.BAD_REQUEST.getMessage(), response.error().getDescription());
            });
  }

  @Test
  void registerBootcampsBootcampsNotExists() {
    WireMock.stubFor(
        WireMock.get(
                WireMock.urlPathEqualTo(
                    ConstantsGateway.BOOTCAMP_SERVICE_BASE_PATH
                        + ConstantsGateway.BOOTCAMP_SERVICE_EXISTS_PATH))
            .withQueryParam(ConstantsGateway.ID_PARAM, WireMock.matching(".*"))
            .willReturn(
                WireMock.aResponse()
                    .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .withBodyFile("bootcampsNotExists.json")));

    webTestClient
        .post()
        .uri(ConstantsRoute.BASE_PATH + ConstantsRoute.BOOTCAMP_PATH)
        .header(ConstantsHeaders.AUTHORIZATION, ConstantsHeaders.BEARER_PREFIX + " 1")
        .bodyValue(getRegisterBootcampDto())
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody(
            new ParameterizedTypeReference<DefaultServerResponse<Object, StandardError>>() {})
        .consumeWith(
            exchangeResult -> {
              var response = exchangeResult.getResponseBody();
              assertNotNull(response);
              assertEquals(
                  ServerResponses.BAD_REQUEST.getMessage(), response.error().getDescription());
            });
  }
}
