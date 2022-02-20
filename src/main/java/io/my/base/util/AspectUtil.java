package io.my.base.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Component
public class AspectUtil {
    public Class<?> getClass(JoinPoint joinPoint) {
        return joinPoint.getTarget().getClass();
    }

    public String getRequestUrl(JoinPoint joinPoint, Class<?> clazz) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
        String baseUrl = requestMapping == null ? "" : requestMapping.value()[0];

        return Stream.of( GetMapping.class, PutMapping.class, PostMapping.class,
                PatchMapping.class, DeleteMapping.class, RequestMapping.class)
                .filter(method::isAnnotationPresent)
                .map(mappingClass -> getUrl(method, mappingClass, baseUrl))
                .findFirst().orElse(null);
    }


    private String getUrl(Method method, Class<? extends Annotation> annotationClass, String baseUrl) {
        Annotation annotation = method.getAnnotation(annotationClass);
        String[] value;

        String httpMethod = null;

        try {
            value = (String[]) annotationClass.getMethod("value").invoke(annotation);
            httpMethod = (annotationClass.getSimpleName().replace("Mapping", "")).toUpperCase();
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            return null;
        }

        return String.format("%s %s%s", httpMethod, baseUrl, value.length > 0 ? value[0] : "");
    }

    public Map<String, Object> getParams(ProceedingJoinPoint joinPoint) {
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        String[] parameterNames = codeSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        Map<String, Object> params = new HashMap<>();
        for (int index=0; index<parameterNames.length; index++) {
            params.put(parameterNames[index], args[index]);
        }
        return params;
    }

    public boolean isAnnotation(JoinPoint joinPoint, Class<? extends Annotation> annotationClass) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        return method.getAnnotation(annotationClass) != null;
    }
}
