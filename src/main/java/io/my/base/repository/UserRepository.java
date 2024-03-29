package io.my.base.repository;

import io.my.base.entity.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {
    Mono<User> findByEmail(String email);
    Mono<User> findByCollegeEmail(String email);
    Mono<User> findByNickname(String nickname);

}
