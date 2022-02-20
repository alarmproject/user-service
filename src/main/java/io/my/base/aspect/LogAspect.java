package io.my.base.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.my.base.log.PayloadLog;
import io.my.base.util.AspectUtil;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Aspect
@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LogAspect {
    private final AspectUtil aspectUtil;
    private final PayloadLog payloadLog;

    @Around(value = "@annotation(io.my.base.annotation.Logger)")
    public Object logger(ProceedingJoinPoint joinPoint) throws Throwable {
        String logId = UUID.randomUUID().toString();

        long startTime = System.currentTimeMillis();

        logRequest(joinPoint, logId);

        Object proceed = joinPoint.proceed(joinPoint.getArgs());

        if (proceed instanceof Mono<?>) {
            return logResponse(proceed, startTime, logId);
        } else {
            return proceed;
        }
    }

    private void logRequest(ProceedingJoinPoint joinPoint, String logId) {
        Map<String, Object> request = new HashMap<>();
        Class<?> clz = aspectUtil.getClass(joinPoint);
        String requestUrl = aspectUtil.getRequestUrl(joinPoint, clz);
        request.put("url", requestUrl);
        request.put("logId", logId);
        request.put("logType", "request");
        request.put("logTime", new Date().getTime());
        request.putAll(aspectUtil.getParams(joinPoint));
        log(request);
    }

    private Object logResponse(Object proceed, long startTime, String logId) {
        Mono<?> mono = (Mono<?>) proceed;
        return mono.map(response -> {
            long endTime = System.currentTimeMillis();

            Map<String, Object> map = new HashMap<>();
            map.put("response", response);
            map.put("logId", logId);
            map.put("logType", "response");
            map.put("excueteTime", endTime - startTime);
            map.put("logTime", new Date().getTime());

            log(map);
            return response;
        });
    }

    private void log(Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String log = objectMapper.writeValueAsString(obj);
            payloadLog.info(log);
        } catch (JsonProcessingException e) { /* do nothing */ }

    }
}
