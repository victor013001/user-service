package com.pragma.challenge.user_service.domain.usecase;

import com.pragma.challenge.user_service.domain.api.UserServicePort;
import com.pragma.challenge.user_service.domain.exceptions.standard_exception.BadRequest;
import com.pragma.challenge.user_service.domain.model.BootcampIdList;
import com.pragma.challenge.user_service.domain.model.RegisterBootcamp;
import com.pragma.challenge.user_service.domain.model.UserBootcamp;
import com.pragma.challenge.user_service.domain.model.UserEmail;
import com.pragma.challenge.user_service.domain.spi.BootcampServiceGateway;
import com.pragma.challenge.user_service.domain.spi.ReportServiceGateway;
import com.pragma.challenge.user_service.domain.spi.UserPersistencePort;
import com.pragma.challenge.user_service.domain.validation.ValidListAnnotation;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@RequiredArgsConstructor
public class UserUseCase implements UserServicePort {

  private final UserPersistencePort userPersistencePort;
  private final BootcampServiceGateway bootcampServiceGateway;
  private final ReportServiceGateway reportServiceGateway;

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
                                .collectList()))
        .doOnSuccess(
            userBootcamps ->
                updateUserCount(
                        userBootcamps.stream()
                            .map(UserBootcamp::bootcampId)
                            .distinct()
                            .collect(Collectors.toList()))
                    .subscribeOn(Schedulers.boundedElastic())
                    .subscribe(null, ex -> log.error("Unable to update report")));
  }

  @Override
  public Mono<Long> getBootcampIdWithHigherNumberUsers() {
    return userPersistencePort.findBootcampIdWithHigherNumberUsers();
  }

  @Override
  public Mono<List<UserEmail>> getBootcampUsers(Long bootcampId) {
    return userPersistencePort.findBootcampUsers(bootcampId);
  }

  private final Mono<Void> updateUserCount(List<Long> bootcampIds) {
    return reportServiceGateway.updateBootcampsUserCount(new BootcampIdList(bootcampIds));
  }
}
