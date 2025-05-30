package TravelAgency.notification;

import TravelAgency.websockets.FlightWebsocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import travel.model.Flight;

@Component
public class FlightWebSocketNotification implements FlightNotificationService {

    @Autowired
    private FlightWebsocketHandler webSocketHandler;

    public FlightWebSocketNotification(){
        System.out.println("creating ChatWebSocketNotification");
    }
    @Override
    public void flightsUpdated(Flight[] flights) {
        webSocketHandler.sendFlightsAll(flights);
    }
}
