package com.bikestore.async.worker;

import com.bikestore.async.model.Order;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Random;

@Component
public class PaymentWorker {

    private final RabbitTemplate rabbitTemplate;
    private final Random random = new Random();

    private static final int MAX_RETRIES = 3;

    @Value("${bikestore.queue.orders}")
    private String ordersQueue;

    @Value("${bikestore.queue.emails}")
    private String emailsQueue;

    @Value("${bikestore.queue.orders-dlq}")
    private String dlqQueue;

    public PaymentWorker(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "${bikestore.queue.orders}")
    public void processOrder(Order order) {
        boolean success = random.nextBoolean();

        if (success) {
            order.setPaymentStatus("PAID");
            log(order.getOrderId(), "Pago APROBADO, enviando a cola 'emails'");
            rabbitTemplate.convertAndSend(emailsQueue, order);
        } else {
            int retry = order.getRetryCount() + 1;
            order.setRetryCount(retry);
            log(order.getOrderId(), "Pago FALLÓ. Intento " + retry);

            if (retry < MAX_RETRIES) {
                log(order.getOrderId(), "Reenviando a 'orders' para reintento");
                rabbitTemplate.convertAndSend(ordersQueue, order);
            } else {
                order.setPaymentStatus("FAILED");
                log(order.getOrderId(), "Superó reintentos. Enviando a Dead-Letter Queue 'orders.dlq'");
                rabbitTemplate.convertAndSend(dlqQueue, order);
            }
        }
    }

    private void log(String orderId, String message) {
        String ts = Instant.now().toString();
        String thread = Thread.currentThread().getName();
        System.out.printf("[%s] orderId=%s time=%s -> %s%n",
                thread, orderId, ts, message);
    }
}
