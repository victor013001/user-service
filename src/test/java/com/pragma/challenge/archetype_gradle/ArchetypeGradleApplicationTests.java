package com.pragma.challenge.archetype_gradle;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class ArchetypeGradleApplicationTests {

  @Test
  void contextLoads() {
    /**
     * This method is intentionally left empty to verify that the Spring application context loads
     * correctly.
     */
  }
}
