package com.pragma.challenge.user_service.infrastructure.adapters.persistence.mapper;

import com.pragma.challenge.user_service.domain.model.UserEmail;
import com.pragma.challenge.user_service.infrastructure.adapters.persistence.entity.UserEmailProjection;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserEntityMapper {
  UserEmail toModel(UserEmailProjection projection);
}
