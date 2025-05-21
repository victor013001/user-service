package com.pragma.challenge.user_service.domain.exceptions.standard_exception;

import com.pragma.challenge.user_service.domain.enums.ServerResponses;
import com.pragma.challenge.user_service.domain.exceptions.StandardException;

public class BadRequest extends StandardException {
  public BadRequest() {
    super(ServerResponses.BAD_REQUEST);
  }
}
