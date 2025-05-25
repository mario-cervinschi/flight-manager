package travel.services;

import travel.model.Agency;
import travel.model.Flight;
import travel.model.Ticket;

import java.time.LocalDate;

public interface ITravelServices {

    Agency login(Agency agency, ITravelObserver client) throws TravelException;

    Agency saveAgency(Agency agency) throws TravelException;

    Flight[] getAllFlights() throws TravelException;

    Flight[] getAllFlightsDestinationDate(String destination, LocalDate date) throws TravelException;

    Flight updateFlight(Flight flight) throws TravelException;

    Ticket addTicket(Ticket ticket) throws TravelException;

    void logout(Agency agency, ITravelObserver client) throws TravelException;
}
