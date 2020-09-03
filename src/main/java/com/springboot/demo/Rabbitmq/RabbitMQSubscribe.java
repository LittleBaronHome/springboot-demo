package com.springboot.demo.Rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

public class RabbitMQSubscribe {

    private final static String EXCHANGE_NAME = "Hello";
    private final static boolean auto_ack = false; // 需要手动发送回执，以确保当某个worker异常关闭时可及时重新分配。
    private final static boolean durable = true; // 队列持久化，保证再Rabbitmq重启时，队列不丢失

    public static void main(String[] args) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("192.168.1.198");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.basicQos(1); // accept only one unack-ed message at a time (see below)

            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            String queue_name = channel.queueDeclare().getQueue();
//            String queue_name = "test1";
            channel.queueDeclare(queue_name, false, false, true, null); // 自定义队列名 参数 queue, durable, exclusive, autoDelete, arguments
            channel.queueBind(queue_name, EXCHANGE_NAME, "");
            System.out.println(" [*] Waiting for message. To exit press CTRL + C");

            DeliverCallback deliverCallback = (customerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println(" [x] Received '" + message + "'");
                // 手动发送回执
                // 当Rabbitmq发现某个worker没有发送回执，但链接或通道关闭了，就判断该worker异常关闭，重新分配该worker的message
                // rabbitmqctl list_queues name messages_ready messages_unacknowledged
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            };
            channel.basicConsume(queue_name, auto_ack, deliverCallback, customerTag -> {});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
