package ru.proskyryakov.cbrcursondateadapter.adapter.logging;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.proskyryakov.cbrcursondateadapter.adapter.logging.rabbit.RabbitLoggingService;

import java.util.Date;

@Component
@Aspect
@RequiredArgsConstructor
public class RabbitLoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(RabbitLoggingAspect.class);

    private final RabbitLoggingService rabbitLoggingService;

    @Before("@annotation(RabbitLogging)")
    public void logExecutionTime(JoinPoint jp) {
        LoggingMessage loggingMessage = new LoggingMessage(jp.getSignature().getName(), new Date());

        rabbitLoggingService.send(loggingMessage);

        log.info("rabbit send {}", loggingMessage);
    }
}
