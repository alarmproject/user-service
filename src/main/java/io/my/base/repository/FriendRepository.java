package io.my.base.repository;

import io.my.base.entity.Friend;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface FriendRepository extends ReactiveCrudRepository<Friend, Long> {
    Mono<Void> deleteByUserIdAndFollowUserId(
            @Param("userId") Long userId, @Param("followUserId") Long followUserId);
}
