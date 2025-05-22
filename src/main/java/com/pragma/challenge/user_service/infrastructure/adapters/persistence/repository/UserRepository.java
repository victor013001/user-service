package com.pragma.challenge.user_service.infrastructure.adapters.persistence.repository;

import com.pragma.challenge.user_service.infrastructure.adapters.persistence.entity.UserEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ReactiveCrudRepository<UserEntity, Long> {}
