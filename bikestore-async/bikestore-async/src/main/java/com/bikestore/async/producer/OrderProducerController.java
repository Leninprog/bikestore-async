package com.bikestore.async.producer;

import com.bikestore.async.model.Order;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/orders")
public class OrderProducerController {

    private final RabbitTemplate rabbitTemplate;

    @Value("${bikestore.queue.orders}")
    private String ordersQueue;

    public OrderProducerController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping
    public String createOrder(@RequestBody Order order) {
        if (order.getPaymentStatus() == null || order.getPaymentStatus().isBlank()) {
            order.setPaymentStatus("NEW");
        }
        order.setRetryCount(order.getRetryCount());

        // Enviar a la cola 'orders'
        rabbitTemplate.convertAndSend(ordersQueue, order);

        log(order.getOrderId(), "OrderProducer publicó pedido en cola 'orders'");

        return "Order enviado: " + order.getOrderId() + " a las " + Instant.now();
    }

    private void log(String orderId, String message) {
        String ts = Instant.now().toString();
        String thread = Thread.currentThread().getName();
        System.out.printf("[%s] orderId=%s time=%s -> %s%n",
                thread, orderId, ts, message);
    }
}



//Query para el postman
// {
//  "orderId": "ORDER-1001",
//  "customerEmail": "cliente@example.com",
//  "amount": 99.9
//}