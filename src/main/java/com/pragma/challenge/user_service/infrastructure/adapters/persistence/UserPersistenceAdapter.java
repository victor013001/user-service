package com.pragma.challenge.user_service.infrastructure.adapters.persistence;

import com.pragma.challenge.user_service.domain.model.UserBootcamp;
import com.pragma.challenge.user_service.domain.spi.UserPersistencePort;
import com.pragma.challenge.user_service.infrastructure.adapters.persistence.mapper.UserBootcampEntityMapper;
import com.pragma.challenge.user_service.infrastructure.adapters.persistence.repository.UserBootcampRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserPersistencePort {
  private static final String LOG_PREFIX = "[PROFILE_PERSISTENCE_ADAPTER] >>>";

  private final UserBootcampRepository userBootcampRepository;
  private final UserBootcampEntityMapper userBootcampEntityMapper;
  private final TransactionalOperator transactionalOperator;

  @Override
  public Flux<Long> filterUnrelatedBootcamps(Long userId, List<Long> bootcampIds) {
    log.info(
        "{} Filtering bootcamps from list {} that are already linked to user id: {}.",
        LOG_PREFIX,
        bootcampIds,
        userId);
    return userBootcampRepository
        .findBootcamps(userId)
        .collectList()
        .flatMapMany(
            existingIds ->
                Flux.fromIterable(
                    bootcampIds.stream().filter(id -> !existingIds.contains(id)).toList()));
  }

  @Override
  public Mono<UserBootcamp> registerBootcamp(UserBootcamp userBootcamp) {
    log.info(
        "{} Register user with id: {} to bootcamp with id: {}.",
        LOG_PREFIX,
        userBootcamp.userId(),
        userBootcamp.bootcampId());
    return userBootcampRepository
        .save(userBootcampEntityMapper.toEntity(userBootcamp))
        .map(userBootcampEntityMapper::toModel)
        .as(transactionalOperator::transactional);
  }
}
