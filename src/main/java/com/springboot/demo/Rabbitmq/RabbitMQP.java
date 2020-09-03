package com.springboot.demo.Rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class RabbitMQP {

    private final static String QUEUE_NAME = "Hello";
    private final static boolean durable = true; // 队列持久化，保证再Rabbitmq重启时，队列不丢失

    public static void main(String[] args) {
        String message = "hello_10";
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.1.198");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()){
            channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        } catch (Exception e) {

        }
    }
}
