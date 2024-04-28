package edu.cmu.orderservice.service;

import com.google.gson.Gson;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendToQueue(String queueName, Object message) {
        // Use default exchange, set queue name as the routing key
        Gson gson = new Gson();
        String jsonMessage = gson.toJson(message);
        rabbitTemplate.convertAndSend(queueName, jsonMessage);
        System.out.println("Message sent to queue " + queueName + ": " + jsonMessage);
    }
}
