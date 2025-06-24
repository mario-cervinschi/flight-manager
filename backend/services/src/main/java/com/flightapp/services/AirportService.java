

package com.flightapp.services;

import com.flightapp.model.Airport;
import com.flightapp.repository.IAirportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirportService {

    private final IAirportRepository airportRepository;

    @Autowired
    AirportService(IAirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    public List<Airport> getAllAirports() {
        return airportRepository.findAll();
    }
}