package com.flightapp.utils.websocket.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightapp.utils.websocket.websockets.FlightWebsocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final ObjectMapper objectMapper;

    public WebSocketConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @Bean
    public FlightWebsocketHandler createFlightsWebsocketHandler(){
        return new FlightWebsocketHandler(objectMapper);
    }

    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(createFlightsWebsocketHandler(), "/flightws").setAllowedOrigins("*");
        System.out.println("Registered WebSocket Handler for flightws");
    }
}