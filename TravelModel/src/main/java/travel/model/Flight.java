package travel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@jakarta.persistence.Entity
@Table(name = "Flights")
public class Flight extends Entity<Long> implements Comparable<Flight>, Serializable {
    private String destination;
    private LocalDateTime timeOfDeparture;
    private String airportName;
    private Long availableSeats;



    public Flight(String destination, LocalDateTime timeOfDeparture, String airportName, Long availableSeats) {
        this.destination = destination;
        this.timeOfDeparture = timeOfDeparture;
        this.airportName = airportName;
        this.availableSeats = availableSeats;
    }

    public Flight() {

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Flight flight = (Flight) o;
        return Objects.equals(destination, flight.destination) && Objects.equals(timeOfDeparture, flight.timeOfDeparture) && Objects.equals(airportName, flight.airportName) && Objects.equals(availableSeats, flight.availableSeats);
    }

    @Override
    public int compareTo(Flight o) {
        return this.getId().compareTo(o.getId());
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setTimeOfDeparture(LocalDateTime timeOfDeparture) {
        this.timeOfDeparture = timeOfDeparture;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), destination, timeOfDeparture, airportName, availableSeats);
    }

    public void setAvailableSeats(Long availableSeats) {
        this.availableSeats = availableSeats;
    }

    @Column(name = "destination")
    public String getDestination() {
        return destination;
    }

    @Column(name = "time_of_departure")
    public LocalDateTime getTimeOfDeparture() {
        return timeOfDeparture;
    }

    @Column(name = "airport_name")
    public String getAirportName() {
        return airportName;
    }

    @Column(name = "available_seats")
    public Long getAvailableSeats() {
        return availableSeats;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "destination='" + destination + '\'' +
                ", timeOfDeparture=" + timeOfDeparture +
                ", airportName='" + airportName + '\'' +
                ", availableSeats=" + availableSeats +
                '}';
    }
}
