package TravelAgency.restclient;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import travel.model.Flight;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class FlightRestClientTest {

    private static final String BASE_URL = "http://localhost:8080/travel/flights";

    public static void main(String[] args) {
        RestClient client = RestClient.builder()
                .baseUrl(BASE_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        // 1. GET /greeting
        String greeting = client.get()
                .uri("/greeting?name=RestClientUser")
                .retrieve()
                .body(String.class);
        System.out.println("Greeting: " + greeting);

        // 2. GET all flights
        Flight[] flights = client.get()
                .uri("")
                .retrieve()
                .body(Flight[].class);
        System.out.println("All Flights:");
        if (flights != null) {
            for (Flight flight : flights) {
                System.out.println(flight);
            }
        }

        // 3. POST create flight
        Flight newFlight = new Flight("Madrid", LocalDateTime.now().plusDays(1), "Barajas", 150L);
        Flight createdFlight = client.post()
                .uri("")
                .body(newFlight)
                .retrieve()
                .body(Flight.class);
        System.out.println("Created Flight: " + createdFlight);

        if (createdFlight == null || createdFlight.getId() == null) {
            System.err.println("Flight creation failed!");
            return;
        }

        Long createdId = createdFlight.getId();

        // 4. GET by ID
        Flight flightById = client.get()
                .uri("/{id}", createdId)
                .retrieve()
                .body(Flight.class);
        System.out.println("Fetched by ID: " + flightById);

        // 5. PUT update
        flightById.setAvailableSeats(99L);
        Flight updatedFlight = client.put()
                .uri("/{id}", createdId)
                .body(flightById)
                .retrieve()
                .body(Flight.class);
        System.out.println("Updated Flight: " + updatedFlight);

        // 6. DELETE
        client.delete()
                .uri("/{id}", createdId)
                .retrieve()
                .toBodilessEntity();
        System.out.println("Deleted Flight with ID: " + createdId);
    }
}
