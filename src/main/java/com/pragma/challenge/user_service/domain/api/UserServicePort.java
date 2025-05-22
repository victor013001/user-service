package com.pragma.challenge.user_service.domain.api;

import com.pragma.challenge.user_service.domain.model.RegisterBootcamp;
import com.pragma.challenge.user_service.domain.model.UserBootcamp;
import java.util.List;
import reactor.core.publisher.Mono;

public interface UserServicePort {

  Mono<List<UserBootcamp>> registerBootcamp(Long userId, RegisterBootcamp registerBootcamp);
}
