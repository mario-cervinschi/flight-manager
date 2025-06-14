package com.flightapp.services;

import com.flightapp.model.Flight;
import com.flightapp.repository.IFlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FlightService {

    private final IFlightRepository flightRepository;

    @Autowired
    public FlightService(IFlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public Flight saveFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    public Optional<Flight> getFlightById(Long id) {
        return flightRepository.findById(id);
    }

    public Flight updateFlight(Flight updatedFlight) {
        return flightRepository.findById(updatedFlight.getId())
                .map(existingFlight -> {
                    existingFlight.setDestination(updatedFlight.getDestination());
                    existingFlight.setTimeOfDeparture(updatedFlight.getTimeOfDeparture());
                    existingFlight.setAirportName(updatedFlight.getAirportName());
                    existingFlight.setAvailableSeats(updatedFlight.getAvailableSeats());
                    return flightRepository.save(existingFlight);
                })
                .orElse(null);
    }

    public void deleteFlight(Long id) {
        flightRepository.deleteById(id);
    }
}
