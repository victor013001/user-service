package com.pragma.challenge.user_service.util;

import com.pragma.challenge.user_service.infrastructure.entrypoints.dto.RegisterBootcampDto;
import java.util.List;

public class RegisterBootcampDtoData {
  private RegisterBootcampDtoData() throws InstantiationException {
    throw new InstantiationException("Data class cannot be instantiated");
  }

  public static RegisterBootcampDto getRegisterBootcampDto() {
    return new RegisterBootcampDto(List.of(1L, 2L, 3L, 4L));
  }

  public static RegisterBootcampDto getInvalidRegisterBootcampDto() {
    return new RegisterBootcampDto(List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L));
  }
}
