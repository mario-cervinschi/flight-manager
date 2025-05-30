package TravelAgency.notification;

import travel.model.Flight;

public interface FlightNotificationService {
    public void flightsUpdated(Flight[] flights);
}
