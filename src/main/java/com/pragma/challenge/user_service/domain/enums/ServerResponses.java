package com.pragma.challenge.user_service.domain.enums;

import com.pragma.challenge.user_service.domain.constants.ConstantsMsg;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ServerResponses {
  BAD_REQUEST(
      "E000", org.springframework.http.HttpStatus.BAD_REQUEST, ConstantsMsg.BAD_REQUEST_MSG),
  SERVER_ERROR(
      "E001",
      org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR,
      ConstantsMsg.SERVER_ERROR_MSG),
  RESOURCE_NOT_FOUND(
      "E002", org.springframework.http.HttpStatus.NOT_FOUND, ConstantsMsg.RESOURCE_NOT_FOUND_MSG),
  GATEWAY_ERROR(
      "E003",
      org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR,
      ConstantsMsg.GATEWAY_ERROR_MSG),
  GATEWAY_BAD_REQUEST(
      "E004",
      org.springframework.http.HttpStatus.BAD_REQUEST,
      ConstantsMsg.GATEWAY_BAD_REQUEST_MSG),
  SUCCESS_REGISTER_TO_BOOTCAMP(
      "", org.springframework.http.HttpStatus.OK, ConstantsMsg.SUCCESS_REGISTER_TO_BOOTCAMP_MSG);

  private final String code;
  private final HttpStatus httpStatus;
  private final String message;
}
