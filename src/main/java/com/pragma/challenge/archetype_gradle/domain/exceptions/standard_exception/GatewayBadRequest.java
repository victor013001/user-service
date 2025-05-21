package com.pragma.challenge.archetype_gradle.domain.exceptions.standard_exception;

import com.pragma.challenge.archetype_gradle.domain.enums.ServerResponses;
import com.pragma.challenge.archetype_gradle.domain.exceptions.StandardException;

public class GatewayBadRequest extends StandardException {
  public GatewayBadRequest() {
    super(ServerResponses.GATEWAY_BAD_REQUEST);
  }
}
