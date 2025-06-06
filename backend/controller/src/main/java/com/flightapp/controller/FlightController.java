package com.flightapp.controller;

import com.flightapp.model.Flight;
import com.flightapp.services.FlightService;
import com.flightapp.utils.notification.FlightNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/travel/flights")
@CrossOrigin(origins = "http://localhost:4200")
public class FlightController {
    @Autowired
    private FlightService flightService;

    @Autowired
    private FlightNotificationService flightNotificationService;


    public FlightController(FlightService flightService,
                            FlightNotificationService flightNotificationService) {
        this.flightService = flightService;
        this.flightNotificationService = flightNotificationService;
    }

    @GetMapping("/greeting")
    public String greeting(@RequestParam(value="name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

    @GetMapping
    public ResponseEntity<List<Flight>> getAllFlights() {
        try {
            List<Flight> flights = new ArrayList<>();
            flightService.getAllFlights().forEach(flight -> flights.add(flight));
            if (flights.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(flights, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Flight> getFlightById(@PathVariable("id") Long id) {
        try {
            Flight flight = flightService.getFlightById(id).orElse(null);
            if (flight != null) {
                return new ResponseEntity<>(flight, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Flight> createFlight(@RequestBody Flight flight) {
        try {
            Flight savedFlight = flightService.saveFlight(flight);
            notifyChanges();
            return new ResponseEntity<>(savedFlight, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Flight> updateFlight(@PathVariable("id") Long id, @RequestBody Flight flight) {
        try {
            Flight existingFlight = flightService.getFlightById(id).orElse(null);
            if (existingFlight != null) {
                flight.setId(id);
                Flight updatedFlight = flightService.updateFlight(flight);
                notifyChanges();
                return new ResponseEntity<>(updatedFlight, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteFlight(@PathVariable("id") Long id) {
        try {
            Flight existingFlight = flightService.getFlightById(id).orElse(null);
            if (existingFlight != null) {
                flightService.deleteFlight(id);
                notifyChanges();
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void notifyChanges(){
        List<Flight> flightList = new ArrayList<>(flightService.getAllFlights());

        Flight[] flights = flightList.toArray(new Flight[0]);

        flightNotificationService.flightsUpdated(flights);
    }
}
