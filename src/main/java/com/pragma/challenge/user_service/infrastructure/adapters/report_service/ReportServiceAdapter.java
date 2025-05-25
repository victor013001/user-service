package com.pragma.challenge.user_service.infrastructure.adapters.report_service;

import com.pragma.challenge.user_service.domain.exceptions.StandardError;
import com.pragma.challenge.user_service.domain.exceptions.standard_exception.GatewayBadRequest;
import com.pragma.challenge.user_service.domain.exceptions.standard_exception.GatewayError;
import com.pragma.challenge.user_service.domain.model.BootcampIdList;
import com.pragma.challenge.user_service.domain.spi.ReportServiceGateway;
import com.pragma.challenge.user_service.infrastructure.entrypoints.dto.DefaultServerResponse;
import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.reactor.retry.RetryOperator;
import io.github.resilience4j.retry.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class ReportServiceAdapter implements ReportServiceGateway {
  private static final String LOG_PREFIX = "[REPORT_SERVICE_GATEWAY] >>>";

  private final String BASE_PATH = "api/v1/report";

  private final WebClient webClient;
  private final Retry retry;
  private final Bulkhead bulkhead;

  public ReportServiceAdapter(
      @Qualifier("reportServiceWebClient") WebClient webClient,
      @Qualifier("reportServiceRetryPolicy") Retry retry,
      @Qualifier("reportServiceBulkhead") Bulkhead bulkhead) {
    this.webClient = webClient;
    this.retry = retry;
    this.bulkhead = bulkhead;
  }

  public Mono<Void> updateBootcampsUserCount(BootcampIdList bootcampIds) {
    log.info(
        "{} Starting report update for bootcamp ids: {} in Report Service.",
        LOG_PREFIX,
        bootcampIds);
    return webClient
        .patch()
        .uri(uriBuilder -> uriBuilder.path(BASE_PATH + "/bootcamp").build())
        .bodyValue(bootcampIds)
        .retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(GatewayBadRequest::new))
        .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(GatewayError::new))
        .bodyToMono(
            new ParameterizedTypeReference<DefaultServerResponse<String, StandardError>>() {})
        .map(DefaultServerResponse::data)
        .doOnNext(exists -> log.info("{} Received Report Service response.", LOG_PREFIX))
        .transformDeferred(RetryOperator.of(retry))
        .transformDeferred(mono -> Mono.defer(() -> bulkhead.executeSupplier(() -> mono)))
        .doOnTerminate(
            () -> log.info("{} Completed report update process in Report Service.", LOG_PREFIX))
        .doOnError(
            ignore ->
                log.error(
                    "{} Error calling Report Service with request: {}", LOG_PREFIX, bootcampIds))
        .then();
  }
}
