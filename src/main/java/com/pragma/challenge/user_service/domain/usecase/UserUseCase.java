package com.pragma.challenge.user_service.domain.usecase;

import com.pragma.challenge.user_service.domain.api.UserServicePort;
import com.pragma.challenge.user_service.domain.exceptions.standard_exception.BadRequest;
import com.pragma.challenge.user_service.domain.model.RegisterBootcamp;
import com.pragma.challenge.user_service.domain.model.UserBootcamp;
import com.pragma.challenge.user_service.domain.spi.BootcampServiceGateway;
import com.pragma.challenge.user_service.domain.spi.UserPersistencePort;
import com.pragma.challenge.user_service.domain.validation.ValidListAnnotation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase implements UserServicePort {

  private final UserPersistencePort userPersistencePort;
  private final BootcampServiceGateway bootcampServiceGateway;

  @Override
  public Mono<List<UserBootcamp>> registerBootcamp(Long userId, RegisterBootcamp registerBootcamp) {
    return Mono.just(registerBootcamp)
        .flatMap(
            registerBootcamp1 -> {
              ValidListAnnotation.valid(registerBootcamp1);
              return Mono.just(registerBootcamp1);
            })
        .flatMap(
            valid ->
                bootcampServiceGateway
                    .bootcampExists(registerBootcamp.bootcampIds())
                    .filter(Boolean.TRUE::equals)
                    .switchIfEmpty(Mono.error(BadRequest::new))
                    .flatMap(
                        exists ->
                            userPersistencePort
                                .filterUnrelatedBootcamps(userId, registerBootcamp.bootcampIds())
                                .flatMap(
                                    bootcampId ->
                                        userPersistencePort.registerBootcamp(
                                            new UserBootcamp(userId, bootcampId)))
                                .collectList()));
  }
}
