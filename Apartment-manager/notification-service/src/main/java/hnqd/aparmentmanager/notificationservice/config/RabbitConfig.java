package hnqd.aparmentmanager.notificationservice.config;

import hnqd.aparmentmanager.notificationservice.listener.Listener;
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
    public Queue alertQueue() {
        return QueueBuilder
                .durable("alertQueue")
                .build();
    }

    @Bean
    public Queue chatQueue() {
        return QueueBuilder
                .durable("chatQueue")
                .build();
    }

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

    @Bean
    public DirectExchange chatExchange() {
        return new DirectExchange("chatExchange");
    }

    @Bean
    public DirectExchange alertExchange() {
        return new DirectExchange("alertExchange");
    }

    // binding for notificationQueue and notificationExchange
    @Bean
    public Binding bindingNotificationQueue(Queue notificationQueue, DirectExchange exchange) {
        return BindingBuilder.bind(notificationQueue)
                .to(exchange)
                .with("rSc7D1FNUS"); // Main binding with routing key "rSc7D1FNUS"
    }

    // binding for chatQueue and chatExchange
    @Bean
    public Binding bindingChatQueue(Queue chatQueue, DirectExchange chatExchange) {
        return BindingBuilder.bind(chatQueue)
                .to(chatExchange)
                .with("G8wMk8fKtQ"); // Main binding with routing key "G8wMk8fKtQ"
    }

    // binding for chatQueue and alertExchange
    @Bean
    public Binding bindingAlertQueue(Queue alertQueue, DirectExchange alertExchange) {
        return BindingBuilder.bind(alertQueue)
                .to(alertExchange)
                .with("LZwwaGgBmN"); // Main binding with routing key "LZwwaGgBmN"
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
    public MessageListenerAdapter notifyListenerAdapter(Listener listener, Jackson2JsonMessageConverter messageConverter) {
        // Notice the name of the method being used ("handleNotification")
        return new MessageListenerAdapter(listener, "handleNotification");
    }

    @Bean
    public MessageListenerAdapter chatListenerAdapter(Listener listener, Jackson2JsonMessageConverter messageConverter) {
        // Notice the name of the method being used ("handleChatMessage")
        return new MessageListenerAdapter(listener, "handleChatMessage");
    }

    @Bean
    public MessageListenerAdapter alertListenerAdapter(Listener listener, Jackson2JsonMessageConverter messageConverter) {
        // Notice the name of the method being used ("handleAlert")
        return new MessageListenerAdapter(listener, "handleAlert");
    }

    @Bean
    public SimpleMessageListenerContainer notifyListenerContainer(ConnectionFactory connectionFactory, MessageListenerAdapter notifyListenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory); // connect to RabbitMQ
        container.setQueueNames("notificationQueue"); // listen message from 'notificationQueue' queue
        container.setMessageListener(notifyListenerAdapter); // Using the MessageListenerAdapter to handle message
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); // manual in confirm message
        return container;
    }

    @Bean
    public SimpleMessageListenerContainer chatListenerContainer(ConnectionFactory connectionFactory, MessageListenerAdapter chatListenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory); // connect to RabbitMQ
        container.setQueueNames("chatQueue"); // listen message from 'chatQueue' queue
        container.setMessageListener(chatListenerAdapter); // Using the MessageListenerAdapter to handle message
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); // manual in confirm message
        return container;
    }

    @Bean
    public SimpleMessageListenerContainer alertListenerContainer(ConnectionFactory connectionFactory, MessageListenerAdapter alertListenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory); // connect to RabbitMQ
        container.setQueueNames("alertQueue"); // listen message from 'alertQueue' queue
        container.setMessageListener(alertListenerAdapter); // Using the MessageListenerAdapter to handle message
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); // manual in confirm message
        return container;
    }
}
