package com.pragma.challenge.user_service.domain.usecase;

import static org.junit.jupiter.api.Assertions.*;

import com.pragma.challenge.user_service.domain.model.RegisterBootcamp;
import com.pragma.challenge.user_service.domain.validation.ValidListAnnotation;
import java.util.List;
import org.junit.jupiter.api.Test;

class UserUseCaseTest {

  @Test
  void test() {
    var t = new RegisterBootcamp(List.of());
    ValidListAnnotation.valid(t);
  }
}
