package io.my.base.repository;

import io.my.base.entity.ActiveHistory;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ActiveHistoryRepository extends ReactiveCrudRepository<ActiveHistory, Long> {
}
