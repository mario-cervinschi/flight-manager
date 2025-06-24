package com.flightapp.utils;

import com.flightapp.model.Airport;
import com.flightapp.model.Flight;
import com.flightapp.model.Route;
import com.flightapp.repository.IFlightRepository;
import com.flightapp.repository.IRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class InitialFlightSeeder implements CommandLineRunner {

    private final IFlightRepository flightRepository;
    private final IRouteRepository routeRepository;

    @Override
    public void run(String... args) {
        if (flightRepository.count() == 0) {
            LocalDate today = LocalDate.now();
            LocalDate oneYearLater = today.plusYears(1);

            for (Route route : routeRepository.findAll()) {
                generateFlights(route.getOriginAirport(), route.getDestinationAirport(), today, oneYearLater);
                generateFlights(route.getDestinationAirport(), route.getOriginAirport(), today, oneYearLater);
            }
        }
    }

    private void generateFlights(Airport from, Airport to, LocalDate start, LocalDate end) {
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            if (date.getDayOfWeek() == DayOfWeek.TUESDAY || date.getDayOfWeek() == DayOfWeek.THURSDAY) {
                Flight flight = new Flight();
                flight.setDeparture(from);
                flight.setDestination(to);
                flight.setDate(date);
                flight.setDepartureTime(LocalTime.of(9, 30));
                flight.setPrice((double) (100 + new Random().nextInt(50)));
                flightRepository.save(flight);
            }
        }
    }
}
