package com.pragma.challenge.user_service.infrastructure.adapters.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("user_bootcamp")
public class UserBootcampEntity {
  @Id private Long id;
  private Long userId;
  private Long bootcampId;
}
