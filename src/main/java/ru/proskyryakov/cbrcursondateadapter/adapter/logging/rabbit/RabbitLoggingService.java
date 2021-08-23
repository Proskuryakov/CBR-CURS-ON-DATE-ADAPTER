package ru.proskyryakov.cbrcursondateadapter.adapter.logging.rabbit;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.proskyryakov.cbrcursondateadapter.adapter.logging.LoggingMessage;


@Service
@RequiredArgsConstructor
public class RabbitLoggingService {

    private final AmqpTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.logging}")
    private String exchange;

    @Value("${rabbitmq.routingkey.logging}")
    private String routingkey;

    public void send(LoggingMessage loggingMessage) {
        rabbitTemplate.convertAndSend(exchange, routingkey, loggingMessage);
    }

}