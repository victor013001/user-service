package com.pragma.challenge.user_service.infrastructure.entrypoints.mapper;

import com.pragma.challenge.user_service.infrastructure.entrypoints.dto.DefaultServerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DefaultServerResponseMapper {
  DefaultServerResponse<Object> toResponse(Object data);
}
