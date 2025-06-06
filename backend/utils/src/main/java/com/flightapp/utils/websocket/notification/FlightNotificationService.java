package com.flightapp.utils.websocket.notification;

import com.flightapp.model.Flight;

public interface FlightNotificationService {
    public void flightsUpdated(Flight[] flights);
}

