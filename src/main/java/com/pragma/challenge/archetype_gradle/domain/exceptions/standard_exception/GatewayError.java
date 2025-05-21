package com.pragma.challenge.archetype_gradle.domain.exceptions.standard_exception;

import com.pragma.challenge.archetype_gradle.domain.enums.ServerResponses;
import com.pragma.challenge.archetype_gradle.domain.exceptions.StandardException;

public class GatewayError extends StandardException {
  public GatewayError() {
    super(ServerResponses.GATEWAY_ERROR);
  }
}
