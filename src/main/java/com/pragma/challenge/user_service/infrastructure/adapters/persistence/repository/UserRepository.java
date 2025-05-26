package com.pragma.challenge.user_service.infrastructure.adapters.persistence.repository;

import com.pragma.challenge.user_service.infrastructure.adapters.persistence.entity.UserEmailProjection;
import com.pragma.challenge.user_service.infrastructure.adapters.persistence.entity.UserEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface UserRepository extends ReactiveCrudRepository<UserEntity, Long> {
  @Query(
"""
SELECT u.name, u.email
FROM "user" u
JOIN user_bootcamp ub ON u.id = ub.user_id
WHERE ub.bootcamp_id = :bootcampId
""")
  Flux<UserEmailProjection> findByBootcampId(Long bootcampId);
}
