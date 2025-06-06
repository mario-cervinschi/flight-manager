package com.flightapp.utils.notification;

import com.flightapp.model.Flight;

public interface FlightNotificationService {
    public void flightsUpdated(Flight[] flights);
}

