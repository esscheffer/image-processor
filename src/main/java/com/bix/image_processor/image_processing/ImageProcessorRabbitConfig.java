package com.bix.image_processor.image_processing;

import lombok.Getter;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Getter
public class ImageProcessorRabbitConfig {

    @Value("${processor.rabbitmq.resize_queue:resize_queue}")
    private String resizeQueue;

    @Value("${processor.rabbitmq.grayscale_queue:grayscale_queue}")
    private String grayscaleQueue;

    @Value("${processor.rabbitmq.resize_exchange:resize_exchange}")
    private String resizeExchange;

    @Value("${processor.rabbitmq.grayscale_exchange:grayscale_exchange}")
    private String grayscaleExchange;

    @Value("${processor.rabbitmq.routing-key:resize_routing_key}")
    private String resizeRoutingKey;

    @Value("${processor.rabbitmq.routing-key:grayscale_routing_key}")
    private String grayscaleRoutingKey;

    @Bean
    public Queue resizeQueue() {
        return new Queue(resizeQueue);
    }

    @Bean
    public Queue grayscaleQueue() {
        return new Queue(grayscaleQueue);
    }

    @Bean
    public Exchange resizeExchange() {
        return ExchangeBuilder.directExchange(resizeExchange)
                .durable(true)
                .build();
    }

    @Bean
    public Exchange grayscaleExchange() {
        return ExchangeBuilder.directExchange(grayscaleExchange)
                .durable(true)
                .build();
    }

    @Bean
    public Binding bindingToResizeQueue() {
        return BindingBuilder.bind(resizeQueue())
                .to(resizeExchange())
                .with(resizeRoutingKey)
                .noargs();
    }

    @Bean
    public Binding bindingToGrayscaleQueue() {
        return BindingBuilder.bind(grayscaleQueue())
                .to(grayscaleExchange())
                .with(grayscaleRoutingKey)
                .noargs();
    }

    @Bean
    public SimpleMessageConverter converter() {
        SimpleMessageConverter converter = new SimpleMessageConverter();
        converter.setAllowedListPatterns(List.of("*"));
        return converter;
    }
}
