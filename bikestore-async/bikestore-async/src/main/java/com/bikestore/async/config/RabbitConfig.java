package com.bikestore.async.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${bikestore.queue.orders}")
    private String ordersQueueName;

    @Value("${bikestore.queue.emails}")
    private String emailsQueueName;

    @Value("${bikestore.queue.orders-dlq}")
    private String dlqQueueName;
    @Bean
    public Queue ordersQueue() {
        return QueueBuilder.durable(ordersQueueName)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", dlqQueueName)
                .build();
    }
    @Bean
    public Queue emailsQueue() {
        return QueueBuilder.durable(emailsQueueName).build();
    }
    @Bean
    public Queue dlqQueue() {
        return QueueBuilder.durable(dlqQueueName).build();
    }
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter(new ObjectMapper());
    }
}
