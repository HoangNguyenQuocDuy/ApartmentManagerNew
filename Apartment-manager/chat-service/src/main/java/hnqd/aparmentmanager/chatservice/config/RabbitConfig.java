package hnqd.aparmentmanager.chatservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Bean
    public FanoutExchange chatFanoutExchange() {
        return new FanoutExchange("chatFanoutExchange");
    }

    @Bean
    public Queue chatServiceQueue() {
        return QueueBuilder
                .durable("chatQueue.chat-service")
                .build();
    }

    @Bean
    public Binding bindChatServiceQueue(Queue chatServiceQueue, FanoutExchange chatFanoutExchange) {
        return BindingBuilder.bind(chatServiceQueue).to(chatFanoutExchange);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter converter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter);
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
