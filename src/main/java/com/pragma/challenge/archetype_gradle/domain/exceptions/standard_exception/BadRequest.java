package com.pragma.challenge.archetype_gradle.domain.exceptions.standard_exception;

import com.pragma.challenge.archetype_gradle.domain.exceptions.StandardException;
import com.pragma.challenge.archetype_gradle.domain.enums.ServerResponses;

public class BadRequest extends StandardException {
  public BadRequest() {
    super(ServerResponses.BAD_REQUEST);
  }
}
