package com.bikestore.async.config;

import org.springframework.amqp.core.Queue;
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
        // durable = true
        return new Queue(ordersQueueName, true);
    }

    @Bean
    public Queue emailsQueue() {
        return new Queue(emailsQueueName, true);
    }

    @Bean
    public Queue dlqQueue() {
        return new Queue(dlqQueueName, true);
    }
}
