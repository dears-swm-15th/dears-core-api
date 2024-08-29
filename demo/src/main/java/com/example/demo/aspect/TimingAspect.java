package com.example.demo.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TimingAspect {

    @Around("execution(* com.example.demo.chat.service.MessageService.*(..))")
    public Object timeMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long timeTaken = System.currentTimeMillis() - startTime;

        System.out.println("--------------------------------------------------------------------------");
        System.out.println("\n Time taken by " + joinPoint.getSignature() + " is " + timeTaken + " ms\n");
        System.out.println("--------------------------------------------------------------------------");

        return result;
    }
}