package com.pragma.challenge.user_service.infrastructure.entrypoints.mapper;

import com.pragma.challenge.user_service.domain.model.RegisterBootcamp;
import com.pragma.challenge.user_service.infrastructure.entrypoints.dto.RegisterBootcampDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RegisterBootcampMapper {
  RegisterBootcamp toRegisterBootcamp(RegisterBootcampDto registerBootcampDto);
}
