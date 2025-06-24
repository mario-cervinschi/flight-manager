package com.flightapp.controller;

import com.flightapp.model.Flight;
import com.flightapp.services.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/travel/flights")
@CrossOrigin(origins = "http://localhost:4200")
public class FlightController {
    @Autowired
    private FlightService flightService;

    @GetMapping
    public List<FlightDto> getFlights(
            @RequestParam("destinationCode") String departureCode,
            @RequestParam("departureCode") String destinationCode,
            @RequestParam("year") int year,
            @RequestParam("month") int month
    ) {
        return flightService.getFlights(departureCode, destinationCode, year, month)
                .stream()
                .map(FlightDto::fromEntity)
                .toList();
    }

    public record FlightDto(LocalDate date, LocalTime departureTime, double price) {

        public static FlightDto fromEntity(Flight flight) {
            return new FlightDto(
                    flight.getDate(),
                    flight.getDepartureTime(),
                    flight.getPrice()
            );
        }
    }
}
