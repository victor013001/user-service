package com.pragma.challenge.user_service.domain.enums;

import com.pragma.challenge.user_service.domain.constants.ConstantsMsg;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ServerResponses {
  BAD_REQUEST("E000", HttpStatus.BAD_REQUEST, ConstantsMsg.BAD_REQUEST_MSG),
  SERVER_ERROR("E001", HttpStatus.INTERNAL_SERVER_ERROR, ConstantsMsg.SERVER_ERROR_MSG),
  GATEWAY_ERROR("E002", HttpStatus.INTERNAL_SERVER_ERROR, ConstantsMsg.GATEWAY_ERROR_MSG),
  GATEWAY_BAD_REQUEST("E003", HttpStatus.BAD_REQUEST, ConstantsMsg.GATEWAY_BAD_REQUEST_MSG),
  SUCCESS_REGISTER_TO_BOOTCAMP(
      "", HttpStatus.CREATED, ConstantsMsg.SUCCESS_REGISTER_TO_BOOTCAMP_MSG);

  private final String code;
  private final HttpStatus httpStatus;
  private final String message;
}
