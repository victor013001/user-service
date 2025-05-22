package com.pragma.challenge.user_service.util;

import com.pragma.challenge.user_service.domain.model.RegisterBootcamp;
import java.util.List;

public class RegisterBootcampData {
  private RegisterBootcampData() throws InstantiationException {
    throw new InstantiationException("Data class cannot be instantiated");
  }

  public static RegisterBootcamp getRegisterBootcamp() {
    return new RegisterBootcamp(List.of(1L, 2L, 3L, 4L));
  }

  public static RegisterBootcamp getInvalidRegisterBootcamp() {
    return new RegisterBootcamp(List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L));
  }
}
