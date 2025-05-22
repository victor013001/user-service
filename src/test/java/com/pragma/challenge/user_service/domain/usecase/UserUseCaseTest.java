package com.pragma.challenge.user_service.domain.usecase;

import static com.pragma.challenge.user_service.util.RegisterBootcampData.getInvalidRegisterBootcamp;
import static com.pragma.challenge.user_service.util.RegisterBootcampData.getRegisterBootcamp;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.pragma.challenge.user_service.domain.exceptions.standard_exception.BadRequest;
import com.pragma.challenge.user_service.domain.model.UserBootcamp;
import com.pragma.challenge.user_service.domain.spi.BootcampServiceGateway;
import com.pragma.challenge.user_service.domain.spi.UserPersistencePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

  @InjectMocks UserUseCase userUseCase;

  @Mock UserPersistencePort userPersistencePort;
  @Mock BootcampServiceGateway bootcampServiceGateway;

  @Test
  void registerBootcamp() {
    var userId = 1L;
    var registerBootcamp = getRegisterBootcamp();
    when(bootcampServiceGateway.bootcampExists(anyList())).thenReturn(Mono.just(true));
    when(userPersistencePort.filterUnrelatedBootcamps(anyLong(), anyList()))
        .thenReturn(Flux.fromIterable(registerBootcamp.bootcampIds()));
    when(userPersistencePort.registerBootcamp(any(UserBootcamp.class)))
        .thenAnswer(invocationOnMock -> Mono.just(invocationOnMock.getArgument(0)));
    StepVerifier.create(userUseCase.registerBootcamp(userId, registerBootcamp))
        .consumeNextWith(
            userBootcamps -> {
              assertEquals(registerBootcamp.bootcampIds().size(), userBootcamps.size());
            })
        .verifyComplete();
  }

  @Test
  void registerBootcampBadRequest() {
    var userId = 1L;
    var registerBootcamp = getInvalidRegisterBootcamp();
    StepVerifier.create(userUseCase.registerBootcamp(userId, registerBootcamp))
        .expectError(BadRequest.class)
        .verify();
  }
}
