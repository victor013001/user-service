package com.pragma.challenge.archetype_gradle.infrastructure.adapters.service.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("service")
public class ServiceProperties {
  private String baseUrl;
  private String timeout;
}
