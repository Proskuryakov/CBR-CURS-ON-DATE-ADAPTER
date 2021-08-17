package ru.proskyryakov.cbrcursondateadapter.rabbit;

import org.flywaydb.core.internal.logging.slf4j.Slf4jLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitService {

    private static final Logger log = LoggerFactory.getLogger(RabbitService.class);

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routingkey}")
    private String routingkey;

    public void send(ChangeCursModel changeCursModel) {
        rabbitTemplate.convertAndSend(exchange, routingkey, changeCursModel);
        log.info("send message {}", changeCursModel);
    }

}
