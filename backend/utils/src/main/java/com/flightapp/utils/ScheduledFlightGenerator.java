package com.flightapp.utils;

import com.flightapp.model.Airport;
import com.flightapp.model.Flight;
import com.flightapp.model.Route;
import com.flightapp.repository.IFlightRepository;
import com.flightapp.repository.IRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class ScheduledFlightGenerator {

    private final IFlightRepository flightRepository;
    private final IRouteRepository routeRepository;

    @Scheduled(cron = "0 0 * * * *") // rulează în fiecare oră fixă
    public void checkAndGenerateFlights() {
        System.out.println("checked for generation\n");
        LocalDate today = LocalDate.now();
        System.out.println("today: " + today);

        // Șterge toate zborurile care au dată anterioară zilei de azi
        flightRepository.deleteByDateBefore(today);

        LocalDate lastGeneratedDate = flightRepository.findTopByOrderByDateDesc()
                .map(Flight::getDate)
                .orElse(today.minusDays(1));
        System.out.println("lastGeneratedDate: " + lastGeneratedDate);

        long daysUntilLast = ChronoUnit.DAYS.between(today, lastGeneratedDate);
        System.out.println(daysUntilLast);

        if (daysUntilLast <= 365 - 7) {
            LocalDate startDate = lastGeneratedDate.plusDays(1);
            LocalDate endDate = startDate.plusDays(6); // 7 zile

            for (Route route : routeRepository.findAll()) {
                generateFlights(route.getOriginAirport(), route.getDestinationAirport(), startDate, endDate);
                generateFlights(route.getDestinationAirport(), route.getOriginAirport(), startDate, endDate);
            }
        }
    }

    private void generateFlights(Airport from, Airport to, LocalDate start, LocalDate end) {
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            DayOfWeek dow = date.getDayOfWeek();

            // Ex: doar marți și joi
            if (dow == DayOfWeek.TUESDAY || dow == DayOfWeek.THURSDAY) {
                boolean exists = flightRepository.existsByDepartureAndDestinationAndDate(from, to, date);
                if (!exists) {
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
}
