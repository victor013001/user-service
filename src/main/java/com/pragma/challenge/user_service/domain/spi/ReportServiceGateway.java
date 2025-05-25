package com.pragma.challenge.user_service.domain.spi;

import com.pragma.challenge.user_service.domain.model.BootcampIdList;
import reactor.core.publisher.Mono;

public interface ReportServiceGateway {
  Mono<Void> updateBootcampsUserCount(BootcampIdList bootcampIds);
}
