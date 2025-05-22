package com.pragma.challenge.user_service.infrastructure.adapters.persistence.mapper;

import com.pragma.challenge.user_service.domain.model.UserBootcamp;
import com.pragma.challenge.user_service.infrastructure.adapters.persistence.entity.UserBootcampEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserBootcampEntityMapper {
  UserBootcamp toModel(UserBootcampEntity entity);

  UserBootcampEntity toEntity(UserBootcamp model);
}
