package com.flightapp.utils.notification;

import com.flightapp.model.Flight;
import com.flightapp.utils.websockets.FlightWebsocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FlightWebSocketNotification implements FlightNotificationService {

    @Autowired
    private FlightWebsocketHandler webSocketHandler;

    public FlightWebSocketNotification(){
        System.out.println("creating FlightsWebsocketNotification");
    }
    @Override
    public void flightsUpdated(Flight[] flights) {
        webSocketHandler.sendFlightsAll(flights);
    }
}
