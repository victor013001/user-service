package com.pragma.challenge.user_service.domain.spi;

import com.pragma.challenge.user_service.domain.model.UserBootcamp;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserPersistencePort {
  Flux<Long> filterUnrelatedBootcamps(Long userId, List<Long> longs);

  Mono<UserBootcamp> registerBootcamp(UserBootcamp userBootcamp);
}
