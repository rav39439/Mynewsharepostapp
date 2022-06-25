package com.mynewapplication.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker

public class Wsconfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // topic = publish-subscribe(one-to-many)
        // queue = point-to-point (one-to-one)
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
        // config.setApplicationDestinationPrefixes("/app");

    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket")
                // registry.addEndpoint("/our-websocket")

                // .setHandshakeHandler(new HttpHandshakeInterceptor())
                .withSockJS();
    }
}
