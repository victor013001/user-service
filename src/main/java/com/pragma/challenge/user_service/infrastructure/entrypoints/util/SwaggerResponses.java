package com.pragma.challenge.user_service.infrastructure.entrypoints.util;

import com.pragma.challenge.user_service.domain.exceptions.StandardError;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

public final class SwaggerResponses {

  private SwaggerResponses() throws InstantiationException {
    throw new InstantiationException("Utility class");
  }

  @Data
  @Schema(name = "DefaultMessageResponse")
  @AllArgsConstructor
  public static class DefaultMessageResponse {
    private String data;
  }

  @Data
  @Schema(name = "DefaultErrorResponse")
  @AllArgsConstructor
  public static class DefaultErrorResponse {
    private StandardError error;
  }
}
