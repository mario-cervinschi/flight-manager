package TravelAgency.rest;

import TravelAgency.notification.FlightNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import travel.model.Flight;
import travel.persistance.interfaces.IFlightRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/travel/flights")
@CrossOrigin(origins = "http://localhost:4200")
public class FlightController {
    @Autowired
    private IFlightRepository flightRepository;

    @Autowired
    private FlightNotificationService flightNotificationService;

    @GetMapping("/greeting")
    public String greeting(@RequestParam(value="name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

    @GetMapping
    public ResponseEntity<List<Flight>> getAllFlights() {
        try {
            List<Flight> flights = new ArrayList<>();
            flightRepository.findAll().forEach(flight -> flights.add(flight));
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
            Flight flight = flightRepository.findOne(id).orElse(null);
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
            Flight savedFlight = flightRepository.save(flight).orElse(null);
            notifyChanges();
            return new ResponseEntity<>(savedFlight, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Flight> updateFlight(@PathVariable("id") Long id, @RequestBody Flight flight) {
        try {
            Flight existingFlight = flightRepository.findOne(id).orElse(null);
            if (existingFlight != null) {
                // Setează ID-ul pentru a ne asigura că actualizăm înregistrarea corectă
                flight.setId(id);
                Flight updatedFlight = flightRepository.update(flight).orElse(null);
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
            Flight existingFlight = flightRepository.findOne(id).orElse(null);
            if (existingFlight != null) {
                flightRepository.delete(id);
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
        List<Flight> flightList = new ArrayList<>();
        flightRepository.findAll().forEach(flightList::add);

        // Conversie în array
        Flight[] flights = flightList.toArray(new Flight[0]);

        flightNotificationService.flightsUpdated(flights);
    }

}
