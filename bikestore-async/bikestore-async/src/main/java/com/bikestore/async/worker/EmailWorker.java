package com.bikestore.async.worker;

import com.bikestore.async.model.Order;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class EmailWorker {

    @RabbitListener(queues = "${bikestore.queue.emails}")
    public void sendEmail(Order order) {
        if ("PAID".equals(order.getPaymentStatus())) {
            log(order.getOrderId(),
                    "Enviando email de confirmación a " + order.getCustomerEmail());
            // Aquí podrías integrar un servicio real de correo
        } else {
            log(order.getOrderId(),
                    "Recibido en 'emails' con estado no PAID: " + order.getPaymentStatus());
        }
    }

    private void log(String orderId, String message) {
        String ts = Instant.now().toString();
        String thread = Thread.currentThread().getName();
        System.out.printf("[%s] orderId=%s time=%s -> %s%n",
                thread, orderId, ts, message);
    }
}
