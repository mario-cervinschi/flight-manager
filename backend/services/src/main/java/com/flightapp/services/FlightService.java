package com.flightapp.services;

import com.flightapp.model.Airport;
import com.flightapp.model.Flight;
import com.flightapp.model.Route;
import com.flightapp.repository.IAirportRepository;
import com.flightapp.repository.IFlightRepository;
import com.flightapp.repository.IRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class FlightService {
    private final IFlightRepository flightRepository;

    public List<Flight> getFlights(String departureCode, String destinationCode, int year, int month) {
        return flightRepository.findFlightsByRouteAndMonth(departureCode, destinationCode, year, month);
    }
}