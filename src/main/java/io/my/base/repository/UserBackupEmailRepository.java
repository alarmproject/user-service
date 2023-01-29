package io.my.base.repository;

import io.my.base.entity.UserBackupEmail;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserBackupEmailRepository extends ReactiveCrudRepository<UserBackupEmail, Long> {
    Mono<UserBackupEmail> findByEmail(String email);

}
