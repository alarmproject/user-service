package io.my.base.context;

import com.sun.istack.NotNull;
import io.my.base.exception.ErrorTypeEnum;
import io.my.base.payload.BaseResponse;
import io.my.base.properties.security.UnSecurityProperties;
import io.my.base.util.ExceptionUtil;
import io.my.base.util.JsonUtil;
import io.my.base.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    private final ExceptionUtil exceptionUtil;
    private final UnSecurityProperties unSecurityProperties;

    @NotNull
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        JwtContext context = new JwtContext(exchange);
        String requestUri = exchange.getRequest().getMethodValue() + " " + exchange.getRequest().getPath();

        for (String uri : unSecurityProperties.getList()) {
            if (uri.equals(requestUri))
                return chain.filter(exchange)
                    .contextWrite(JwtContextHolder.withJwtContext(Mono.just(context)));
        }

        String jwt = context.getJwt();

        if (context.getJwt() == null || !jwtUtil.verifyAccessToken(jwt)) {
            exceptionUtil.setExceptionHeaders(exchange, HttpStatus.UNAUTHORIZED);
            DefaultDataBuffer buffer = exceptionUtil.createExceptionBody(ErrorTypeEnum.JWT_EXCEPTION);
            return exchange.getResponse().writeWith(Mono.just(buffer));
        }

        context.setUserId(jwtUtil.getUserIdByAccessToken(jwt));

        return chain.filter(exchange)
                .contextWrite(JwtContextHolder.withJwtContext(Mono.just(context)));
    }

}
