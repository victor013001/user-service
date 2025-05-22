package com.pragma.challenge.user_service.application.config;

import com.pragma.challenge.user_service.domain.api.UserServicePort;
import com.pragma.challenge.user_service.domain.spi.BootcampServiceGateway;
import com.pragma.challenge.user_service.domain.spi.UserPersistencePort;
import com.pragma.challenge.user_service.domain.usecase.UserUseCase;
import com.pragma.challenge.user_service.infrastructure.adapters.persistence.UserPersistenceAdapter;
import com.pragma.challenge.user_service.infrastructure.adapters.persistence.mapper.UserBootcampEntityMapper;
import com.pragma.challenge.user_service.infrastructure.adapters.persistence.repository.UserBootcampRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.reactive.TransactionalOperator;

@Configuration
@RequiredArgsConstructor
public class UseCasesConfig {

  private final UserBootcampRepository userBootcampRepository;
  private final UserBootcampEntityMapper userBootcampEntityMapper;
  private final TransactionalOperator transactionalOperator;

  @Bean
  public UserServicePort userServicePort(
      UserPersistencePort userPersistencePort, BootcampServiceGateway bootcampServiceGateway) {
    return new UserUseCase(userPersistencePort, bootcampServiceGateway);
  }

  @Bean
  public UserPersistencePort userPersistencePort() {
    return new UserPersistenceAdapter(
        userBootcampRepository, userBootcampEntityMapper, transactionalOperator);
  }
}
