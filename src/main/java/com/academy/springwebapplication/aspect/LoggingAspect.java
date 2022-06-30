package com.academy.springwebapplication.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("within(@org.springframework.stereotype.Repository *)" +
            " || within(@org.springframework.stereotype.Service *)" +
            " || within(@org.springframework.stereotype.Controller *)")
    public void beanPointcut() {
    }

    @Pointcut("execution(* com.academy.springwebapplication.controller.*.*(..))")
    public void controllerPointcut() {
    }

    @Pointcut("execution(* com.academy.springwebapplication.service.*.*(..))")
    public void servicePointcut() {
    }

    @AfterThrowing(pointcut = "beanPointcut()", throwing = "throwable")
    public void loggingAfterThrowing(JoinPoint joinPoint, Throwable throwable) {

        logger.error("Exception in {}.{}() with cause = {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), throwable.getMessage());

    }

    @Around("controllerPointcut()")
    public Object controllerLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        if (logger.isDebugEnabled()) {
            logger.debug("ENTRY ---> Controller: {}. Method: {}() with argument[s] = {}. User: '{}'.",
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(),
                    Arrays.toString(joinPoint.getArgs()), username);
        }
        try {
            Object result = joinPoint.proceed();

            if (logger.isDebugEnabled()) {
                logger.debug("EXIT <--- Controller: {}.Method: {}() with result = {}. User: '{}'.",
                        joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), result, username);
            }

            return result;
        } catch (IllegalArgumentException exception) {
            logger.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());

            throw exception;
        }
    }

    @Around("servicePointcut()")
    public Object serviceLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        if (logger.isDebugEnabled()) {
            logger.debug("ENTRY ---> Service: {}. Method: {}() with argument[s] = {}",
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(),
                    Arrays.toString(joinPoint.getArgs()));
        }
        try {
            Object result = joinPoint.proceed();

            if (logger.isDebugEnabled()) {
                logger.debug("EXIT <--- Service: {}.Method: {}() with result = {}",
                        joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), result);
            }

            return result;
        } catch (IllegalArgumentException exception) {
            logger.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());

            throw exception;
        }
    }
}
