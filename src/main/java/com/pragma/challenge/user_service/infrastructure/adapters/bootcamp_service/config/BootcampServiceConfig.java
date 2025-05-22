package com.pragma.challenge.user_service.infrastructure.adapters.bootcamp_service.config;

import static io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import com.pragma.challenge.user_service.infrastructure.adapters.bootcamp_service.util.BootcampServiceProperties;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
@RequiredArgsConstructor
public class BootcampServiceConfig {

  private final BootcampServiceProperties bootcampServiceProperties;

  @Bean
  public WebClient webClient() {
    return WebClient.builder()
        .baseUrl(bootcampServiceProperties.getBaseUrl())
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .clientConnector(
            getClientHttpConnector(Integer.parseInt(bootcampServiceProperties.getTimeout())))
        .build();
  }

  private ClientHttpConnector getClientHttpConnector(int timeout) {
    return new ReactorClientHttpConnector(
        HttpClient.create()
            .option(CONNECT_TIMEOUT_MILLIS, timeout)
            .doOnConnected(
                connection -> {
                  connection.addHandlerLast(new ReadTimeoutHandler(timeout, MILLISECONDS));
                  connection.addHandlerLast(new WriteTimeoutHandler(timeout, MILLISECONDS));
                })
            .responseTimeout(Duration.ofMillis(timeout)));
  }
}
