package io.my.base.context;

import com.sun.istack.NotNull;
import io.my.base.exception.object.JwtException;
import io.my.base.properties.security.UnSecurityProperties;
import io.my.base.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtContextWebFilter implements WebFilter {
    private final JwtUtil jwtUtil;
    private final UnSecurityProperties unSecurityProperties;

    @NotNull
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        JwtContext context = new JwtContext(exchange);
        String requestUri = exchange.getRequest().getMethodValue() + " " + exchange.getRequest().getPath();

        setContext(context, requestUri);

        return chain.filter(exchange)
                .contextWrite(JwtContextHolder.withJwtContext(Mono.just(context)));
    }

    private void setContext(JwtContext context, String requestUri) {
        for (String uri : unSecurityProperties.getList()) {
            if (uri.equals(requestUri)) return;
        }

        String jwt = context.getJwt();
        if (context.getJwt() == null || !jwtUtil.verifyAccessToken(jwt)) throw new JwtException();

        context.setUserId(jwtUtil.getUserIdByAccessToken(jwt));

        // TODO Database에서 User 정보를 가져와서 Context에 User 세팅
    }
}
