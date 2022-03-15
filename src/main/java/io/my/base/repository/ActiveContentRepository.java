package io.my.base.repository;

import io.my.base.entity.ActiveContent;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ActiveContentRepository extends ReactiveCrudRepository<ActiveContent, Long> {
}
