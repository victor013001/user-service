package com.pragma.challenge.user_service.infrastructure.adapters.bootcamp_service.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("bootcamp-service")
public class BootcampServiceProperties {
  private String baseUrl;
  private String timeout;
}
