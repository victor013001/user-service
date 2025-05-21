package com.pragma.challenge.archetype_gradle.infrastructure.adapters.service.config;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BulkheadConfiguration {

  private final BulkheadRegistry bulkheadRegistry;

  public BulkheadConfiguration(BulkheadRegistry bulkheadRegistry) {
    this.bulkheadRegistry = bulkheadRegistry;
  }

  @Bean
  public Bulkhead serviceBulkhead() {
    return bulkheadRegistry.bulkhead("serviceBulkhead");
  }
}
