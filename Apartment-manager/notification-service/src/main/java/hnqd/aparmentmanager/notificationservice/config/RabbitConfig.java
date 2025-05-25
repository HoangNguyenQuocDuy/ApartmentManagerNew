package hnqd.aparmentmanager.notificationservice.config;

import hnqd.aparmentmanager.notificationservice.listener.Listener;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
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
    public FanoutExchange chatFanoutExchange() {
        return new FanoutExchange("chatFanoutExchange");
    }

    @Bean
    public Queue chatQueue() {
        return QueueBuilder
                .durable("chatQueue.notify-service")
                .build();
    }

    @Bean
    public Queue notificationQueue() {
        return QueueBuilder
                .durable("notificationQueue")
//                .deadLetterExchange("dlx-exchange")
//                .deadLetterRoutingKey("dlx")
                .build();
    }
    @Bean
    public Queue commonNotifyQueue() {
        return QueueBuilder
                .durable("commonNotifyQueue")
                .build();
    }

    // queue for Dead Letter
//    @Bean
//    public Queue deadLetterQueue() {
//        return QueueBuilder.durable("dlq")
//                .ttl(10000)  // TTL (time-to-live) for DLQ messages
//                .build();
//    }

    // exchange for DLX
//    @Bean
//    public DirectExchange dlxExchange() {
//        return new DirectExchange("dlx-exchange");
//    }

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
    public Binding bindChatQueue(Queue chatQueue, FanoutExchange chatFanoutExchange) {
        return BindingBuilder.bind(chatQueue).to(chatFanoutExchange);
    }

    @Bean
    public DirectExchange alertExchange() {
        return new DirectExchange("alertExchange");
    }

    @Bean
    public DirectExchange commonNotifyExchange() {
        return new DirectExchange("commonNotifyExchange");
    }

    // binding for notificationQueue and notificationExchange
    @Bean
    public Binding bindingNotificationQueue(Queue notificationQueue, DirectExchange exchange) {
        return BindingBuilder.bind(notificationQueue)
                .to(exchange)
                .with("rSc7D1FNUS"); // Main binding with routing key "rSc7D1FNUS"
    }

    // binding for chatQueue and chatExchange
//    @Bean
//    public Binding bindingChatQueue(Queue chatQueue, DirectExchange chatExchange) {
//        return BindingBuilder.bind(chatQueue)
//                .to(chatExchange)
//                .with("G8wMk8fKtQ"); // Main binding with routing key "G8wMk8fKtQ"
//    }

    // binding for chatQueue and alertExchange
    @Bean
    public Binding bindingAlertQueue(Queue alertQueue, DirectExchange alertExchange) {
        return BindingBuilder.bind(alertQueue)
                .to(alertExchange)
                .with("LZwwaGgBmN"); // Main binding with routing key "LZwwaGgBmN"
    }

    // binding for deadLetterQueue and dlxExchange
//    @Bean
//    public Binding bindingDeadLetterQueue(Queue deadLetterQueue, DirectExchange dlxExchange) {
//        return BindingBuilder.bind(deadLetterQueue)
//                .to(dlxExchange)
//                .with("dlx"); // Binding for DLQ with routing key "dlx"
//    }

    @Bean
    public Binding bindingCommonNotifyQueue(Queue commonNotifyQueue, DirectExchange commonNotifyExchange) {
        return BindingBuilder.bind(commonNotifyQueue)
                .to(commonNotifyExchange)
                .with("H9wMk8fKtP"); // Main binding with routing key "H9wMk8fKtP"
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter messageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        return factory;
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
}
