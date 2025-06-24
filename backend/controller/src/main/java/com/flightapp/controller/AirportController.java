package com.flightapp.controller;

import com.flightapp.model.Airport;
import com.flightapp.model.Flight;
import com.flightapp.services.AirportService;
import com.flightapp.services.RouteService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/travel/airports")
@CrossOrigin(origins = "http://localhost:4200")
public class AirportController {
    @Autowired
    private AirportService airportService;

    @Autowired
    private RouteService routeService;

    @GetMapping()
    public ResponseEntity<List<Airport>> getAllAirports(){
        try {
            List<Airport> airports = new ArrayList<>(airportService.getAllAirports());
            if (airports.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(airports, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/destinations/{id}")
    public ResponseEntity<List<Airport>> getAllDestinationsForAirportId(@PathVariable("id") String id) {
        try {
            List<Airport> destinations = routeService.getDestinationsForAirport(id);
            if (destinations.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(destinations, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
