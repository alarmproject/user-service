package io.my.base.context;

import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.function.Function;

public class JwtContextHolder {
    private static final Class<JwtContext> JWT_CONTEXT_KEY = JwtContext.class;

    public static JwtContext getContext() {
        return Mono.deferContextual(Mono::just)
                .filter(c -> c.hasKey(JWT_CONTEXT_KEY))
                .flatMap(c -> c.<Mono<JwtContext>>get(JWT_CONTEXT_KEY))
                .block();
    }

    public static Mono<JwtContext> getMonoContext() {
        return Mono.deferContextual(Mono::just)
                .filter(c -> c.hasKey(JWT_CONTEXT_KEY))
                .flatMap(c -> c.<Mono<JwtContext>>get(JWT_CONTEXT_KEY));
    }

    public static Function<Context, Context> clearContext() {
        return context -> context.delete(JWT_CONTEXT_KEY);
    }

    public static Context withJwtContext(Mono<? extends JwtContext> jwtContext) {
        return Context.of(JWT_CONTEXT_KEY, jwtContext);
    }

    public static Mono<Long> getMonoUserId() {
        return getMonoContext().map(JwtContext::getUserId);
    }
}
