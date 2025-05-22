package com.pragma.challenge.user_service.infrastructure.adapters.persistence.repository;

import com.pragma.challenge.user_service.infrastructure.adapters.persistence.entity.UserBootcampEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface UserBootcampRepository extends ReactiveCrudRepository<UserBootcampEntity, Long> {
  @Query("SELECT bootcamp_id FROM user_bootcamp WHERE user_id = :userId")
  Flux<Long> findBootcamps(Long userId);
}
