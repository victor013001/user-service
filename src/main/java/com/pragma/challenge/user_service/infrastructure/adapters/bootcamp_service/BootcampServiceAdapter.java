package com.pragma.challenge.user_service.infrastructure.adapters.bootcamp_service;

import com.pragma.challenge.user_service.domain.exceptions.StandardError;
import com.pragma.challenge.user_service.domain.exceptions.standard_exception.GatewayBadRequest;
import com.pragma.challenge.user_service.domain.exceptions.standard_exception.GatewayError;
import com.pragma.challenge.user_service.domain.spi.BootcampServiceGateway;
import com.pragma.challenge.user_service.infrastructure.adapters.bootcamp_service.util.ConstantsGateway;
import com.pragma.challenge.user_service.infrastructure.entrypoints.dto.DefaultServerResponse;
import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.reactor.retry.RetryOperator;
import io.github.resilience4j.retry.Retry;
import java.util.List;
import java.util.concurrent.TimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class BootcampServiceAdapter implements BootcampServiceGateway {
  private static final String LOG_PREFIX = "[BOOTCAMP_SERVICE_GATEWAY] >>>";

  private final WebClient webClient;
  private final Retry retry;
  private final Bulkhead bulkhead;

  @Override
  @CircuitBreaker(name = "bootcampService", fallbackMethod = "fallback")
  public Mono<Boolean> bootcampExists(List<Long> bootcampIds) {
    log.info(
        "{} Starting bootcamp validation process for ids: {} in Bootcamp Service.",
        LOG_PREFIX,
        bootcampIds);
    return webClient
        .get()
        .uri(
            uriBuilder -> {
              UriBuilder builder =
                  uriBuilder.path(ConstantsGateway.BOOTCAMP_SERVICE_BASE_PATH + "/exists");
              bootcampIds.forEach(id -> builder.queryParam(ConstantsGateway.ID_PARAM, id));
              return builder.build();
            })
        .retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(GatewayBadRequest::new))
        .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(GatewayError::new))
        .bodyToMono(
            new ParameterizedTypeReference<DefaultServerResponse<Boolean, StandardError>>() {})
        .map(DefaultServerResponse::data)
        .doOnNext(exists -> log.info("{} Received API response.", LOG_PREFIX))
        .transformDeferred(RetryOperator.of(retry))
        .transformDeferred(mono -> Mono.defer(() -> bulkhead.executeSupplier(() -> mono)))
        .doOnTerminate(
            () ->
                log.info(
                    "{} Completed bootcamps validation process in Bootcamp Service.", LOG_PREFIX))
        .doOnError(
            ignore ->
                log.error(
                    "{} Error calling Bootcamp Service with ids: {}", LOG_PREFIX, bootcampIds));
  }

  public Mono<Boolean> fallback(Throwable t) {
    log.warn("{} Fallback triggered for bootcampService", LOG_PREFIX);
    return Mono.defer(() -> Mono.justOrEmpty(t instanceof TimeoutException ? Boolean.FALSE : null))
        .switchIfEmpty(Mono.error(t));
  }
}
