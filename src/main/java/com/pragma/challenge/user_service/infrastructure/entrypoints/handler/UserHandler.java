package com.pragma.challenge.user_service.infrastructure.entrypoints.handler;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface UserHandler {
  Mono<ServerResponse> registerToBootcamp(ServerRequest request);
}
