package com.pragma.challenge.user_service.infrastructure.entrypoints.handler.impl;

import static com.pragma.challenge.user_service.infrastructure.entrypoints.util.ResponseUtil.buildResponse;
import static com.pragma.challenge.user_service.infrastructure.entrypoints.util.ResponseUtil.buildStandardError;

import com.pragma.challenge.user_service.domain.api.UserServicePort;
import com.pragma.challenge.user_service.domain.constants.ConstantsHeaders;
import com.pragma.challenge.user_service.domain.enums.ServerResponses;
import com.pragma.challenge.user_service.domain.exceptions.StandardException;
import com.pragma.challenge.user_service.domain.exceptions.standard_exception.BadRequest;
import com.pragma.challenge.user_service.infrastructure.entrypoints.dto.RegisterBootcampDto;
import com.pragma.challenge.user_service.infrastructure.entrypoints.handler.UserHandler;
import com.pragma.challenge.user_service.infrastructure.entrypoints.mapper.RegisterBootcampMapper;
import com.pragma.challenge.user_service.infrastructure.entrypoints.mapper.ServerResponseMapper;
import com.pragma.challenge.user_service.infrastructure.entrypoints.util.ParseUtil;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserHandlerImpl implements UserHandler {
  private static final String LOG_PREFIX = "[USER_HANDLER] >>> ";

  private final ServerResponseMapper responseMapper;
  private final RegisterBootcampMapper registerBootcampMapper;
  private final UserServicePort userServicePort;

  @Override
  public Mono<ServerResponse> registerToBootcamp(ServerRequest request) {
    Long userId = mockUserIdFromAuthorization(request);
    return request
        .bodyToMono(RegisterBootcampDto.class)
        .switchIfEmpty(Mono.error(BadRequest::new))
        .flatMap(
            registerBootcampDto -> {
              log.info(
                  "{} Register User with id: {} to bootcamps with ids: {}",
                  LOG_PREFIX,
                  userId,
                  registerBootcampDto);
              return userServicePort.registerBootcamp(
                  userId, registerBootcampMapper.toRegisterBootcamp(registerBootcampDto));
            })
        .flatMap(
            ignore ->
                buildResponse(
                    ServerResponses.SUCCESS_REGISTER_TO_BOOTCAMP.getHttpStatus(),
                    ServerResponses.SUCCESS_REGISTER_TO_BOOTCAMP.getMessage(),
                    null,
                    responseMapper))
        .doOnError(logErrorHandler())
        .onErrorResume(StandardException.class, standardErrorHandler())
        .onErrorResume(genericErrorHandler());
  }

  @Override
  public Mono<ServerResponse> getBootcampWithHigherNumberUsers(ServerRequest request) {
    return userServicePort
        .getBootcampIdWithHigherNumberUsers()
        .flatMap(bootcampId -> buildResponse(HttpStatus.OK, bootcampId, null, responseMapper))
        .doOnError(logErrorHandler())
        .onErrorResume(genericErrorHandler());
  }

  @Override
  public Mono<ServerResponse> getBootcampUsers(ServerRequest request) {
    String id = request.pathVariable("bootcampId");
    return Mono.just(ParseUtil.toLong(id))
        .flatMap(userServicePort::getBootcampUsers)
        .flatMap(userEmails -> buildResponse(HttpStatus.OK, userEmails, null, responseMapper))
        .doOnError(logErrorHandler())
        .onErrorResume(genericErrorHandler());
  }

  private Long mockUserIdFromAuthorization(ServerRequest serverRequest) {
    return Optional.ofNullable(serverRequest.headers().firstHeader(ConstantsHeaders.AUTHORIZATION))
        .map(
            auth -> auth.replaceFirst("(?i)^" + ConstantsHeaders.BEARER_PREFIX + "\\s*", "").trim())
        .filter(id -> !id.isBlank())
        .map(
            id -> {
              try {
                return Long.parseLong(id);
              } catch (NumberFormatException e) {
                throw new BadRequest();
              }
            })
        .orElseThrow(BadRequest::new);
  }

  private Consumer<Throwable> logErrorHandler() {
    return ex ->
        log.error(
            "{} Exception {} caught. Caused by: {}",
            LOG_PREFIX,
            ex.getClass().getSimpleName(),
            ex.getMessage());
  }

  private Function<StandardException, Mono<ServerResponse>> standardErrorHandler() {
    return ex -> buildResponse(ex.getHttpStatus(), null, ex.getStandardError(), responseMapper);
  }

  private Function<Throwable, Mono<ServerResponse>> genericErrorHandler() {
    return ex ->
        buildResponse(
            ServerResponses.SERVER_ERROR.getHttpStatus(),
            null,
            buildStandardError(ServerResponses.SERVER_ERROR),
            responseMapper);
  }
}
