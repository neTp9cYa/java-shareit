package ru.practicum.shareit.common.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class EndpointAspect {

    @Before("@annotation(ru.practicum.shareit.common.LogInputOutputAnnotaion)")
    public void before(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();
        Object[] parameterValues = joinPoint.getArgs();

        ObjectMapper mapper = new ObjectMapper();
        StringBuilder logParameters = new StringBuilder();
        for (int i = 0; i < parameterNames.length; i++) {
            logParameters.append(parameterNames[i]);
            logParameters.append(":");
            logParameters.append(parameterValues[i]);
            logParameters.append(";");
        }

        if (logParameters.length() == 0) {
            log.info("Start to process method {} from class {} without parameters",
                signature.getName(),
                signature.getDeclaringType().getName());
        } else {
            log.info("Start to process method {} from class {} with parameters: {}",
                signature.getName(),
                signature.getDeclaringType().getName(),
                logParameters.substring(0, logParameters.length() - 1));
        }
    }


    @AfterReturning(
        pointcut = "@annotation(ru.practicum.shareit.common.LogInputOutputAnnotaion)",
        returning = "returningValue")
    public void afterReturning(JoinPoint joinPoint, Object returningValue) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        if (signature.getReturnType().equals(Void.TYPE)) {
            log.info("End to process method {} from class {} without returning value",
                signature.getName(),
                signature.getDeclaringType().getName());
        } else {
            log.info("End to process method {} from class {} with returning value: {}",
                signature.getName(),
                signature.getDeclaringType().getName(),
                returningValue);
        }
    }
}
