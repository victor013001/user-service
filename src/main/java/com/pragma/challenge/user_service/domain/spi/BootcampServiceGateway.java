package com.pragma.challenge.user_service.domain.spi;

import java.util.List;
import reactor.core.publisher.Mono;

public interface BootcampServiceGateway {
  Mono<Boolean> bootcampExists(List<Long> bootcampIds);
}
