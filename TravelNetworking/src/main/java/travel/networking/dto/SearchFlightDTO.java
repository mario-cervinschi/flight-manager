package travel.networking.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class SearchFlightDTO implements Serializable {
    private String destination;
    private LocalDate departureDate;

    public SearchFlightDTO(String destination, LocalDate departureDate) {
        this.destination = destination;
        this.departureDate = departureDate;
    }

    public String getDestination() {
        return destination;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }
}
