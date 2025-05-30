package TravelAgency;

import TravelAgency.websockets.FlightWebsocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final ObjectMapper objectMapper;

    // Inject ObjectMapper via constructor (recommended)
    public WebSocketConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @Bean
    public FlightWebsocketHandler createChatWebsocketHandler(){

        return new FlightWebsocketHandler(objectMapper);
    }

    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(createChatWebsocketHandler(), "/chatws").setAllowedOrigins("*");
        System.out.println("Registered WebSocket Handler for chatws");
    }
}
