package com.pragma.challenge.user_service.infrastructure.entrypoints.util;

import com.pragma.challenge.user_service.domain.exceptions.standard_exception.BadRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestValidator {
  private static final String LOG_PREFIX = "[REQUEST_VALIDATOR] >>>";

  private final Validator validator;

  public <T> Mono<T> validate(T requestDto) {
    Set<ConstraintViolation<T>> violations = validator.validate(requestDto);
    if (!violations.isEmpty()) {
      List<String> errors = violations.stream().map(ConstraintViolation::getMessage).toList();
      log.error("{} Constraint violations: {}", LOG_PREFIX, errors);
      return Mono.error(BadRequest::new);
    }
    return Mono.just(requestDto);
  }
}
