package ru.clevertec.controller.servlets.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

@Slf4j
@Aspect
public class LoggingAspect {

    private FileWriter fileWriter;

    public LoggingAspect() {

        try {
            fileWriter = new FileWriter("log.txt", false);
        } catch (IOException e) {
            log.info("Invalid file");
        }
    }

    @AfterReturning(pointcut = "execution(* ru.clevertec.service.impl.AccountServiceImpl.*(..))", returning = "result")
    public void logMethodCall(JoinPoint joinPoint, Object result) {

        String methodName = joinPoint.getSignature().getName();
        String arguments = Arrays.toString(joinPoint.getArgs());
        String logMessage = String.format("Method '%s' called with arguments %s%n", methodName, arguments);
        String resultMessage = String.format("Method '%s' returned result: %s%n", methodName, result);

        log.info(logMessage);
        log.info(resultMessage);

        try {
            fileWriter.write(String.valueOf(new Date()) + " " + logMessage);
            fileWriter.write(String.valueOf(new Date()) + " " + resultMessage);
            fileWriter.flush();
        } catch (IOException e) {
            log.info("Recording failed!");
        }
    }
}
