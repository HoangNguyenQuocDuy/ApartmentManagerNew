package hnqd.aparmentmanager.notificationservice.config;

import hnqd.aparmentmanager.notificationservice.service.impl.NotificationService;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {

    @Bean
    public Queue notificationQueue() {
        return QueueBuilder
                .durable("notificationQueue")
                .deadLetterExchange("dlx-exchange")
                .deadLetterRoutingKey("dlx")
                .build();
    }

    // queue for Dead Letter
    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable("dlq")
                .ttl(10000)  // TTL (time-to-live) for DLQ messages
                .build();
    }

    // exchange for DLX
    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange("dlx-exchange");
    }

    // exchange for main queue
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange("notificationExchange");
    }

    // binding for notificationQueue and notificationExchange
    @Bean
    public Binding bindingNotificationQueue(Queue notificationQueue, DirectExchange exchange) {
        return BindingBuilder.bind(notificationQueue)
                .to(exchange)
                .with("rSc7D1FNUS"); // Main binding with routing key "rSc7D1FNUS"
    }

    // binding for deadLetterQueue and dlxExchange
    @Bean
    public Binding bindingDeadLetterQueue(Queue deadLetterQueue, DirectExchange dlxExchange) {
        return BindingBuilder.bind(deadLetterQueue)
                .to(dlxExchange)
                .with("dlx"); // Binding for DLQ with routing key "dlx"
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // Single MessageListenerAdapter Bean
    @Bean
    public MessageListenerAdapter messageListenerAdapter(NotificationService notificationService, Jackson2JsonMessageConverter messageConverter) {
        // Notice the name of the method being used ("handleNotification")
        return new MessageListenerAdapter(notificationService, "handleNotification");
    }

    @Bean
    public SimpleMessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory, MessageListenerAdapter messageListenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory); // connect to RabbitMQ
        container.setQueueNames("notificationQueue"); // listen message from 'notificationQueue' queue
        container.setMessageListener(messageListenerAdapter); // Using the MessageListenerAdapter to handle message
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); // manual in confirm message
        return container;
    }
}
