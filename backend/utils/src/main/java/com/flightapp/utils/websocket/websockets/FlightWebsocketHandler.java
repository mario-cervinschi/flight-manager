package com.flightapp.utils.websockets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightapp.model.Flight;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class FlightWebsocketHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper;

    private static List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    public FlightWebsocketHandler(ObjectMapper objectMapper){
        this.objectMapper = objectMapper; System.out.println("Constructing new FlightsWebsocketHandler");
    }
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        System.out.println("[Websocket] New TextMessage: " + message.getPayload());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session)  {
        System.out.println("New websocket session established"+session.getId());
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status)  {
        System.out.println("Removing websocket session"+session.getId());
        sessions.remove(session);
    }

    public void sendFlightsAll(Flight[] flights) {
        try{
            String result=objectMapper.writeValueAsString(flights);
            System.out.println("Websockets sending data "+result);
            for(WebSocketSession webSocketSession : sessions) {
                try {
                    webSocketSession.sendMessage(new TextMessage(result));
                }catch (IOException e){
                    System.err.println("error sending message to websocket"+e);
                }
            }
        } catch(JsonProcessingException e){
            System.err.println("error writing object"+e);
        }
    }
}
