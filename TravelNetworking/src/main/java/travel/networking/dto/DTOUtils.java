package travel.networking.dto;

import travel.model.Agency;
import travel.model.Flight;

import java.time.LocalDate;

public class DTOUtils {

    public static Flight getFromDTO(SearchFlightDTO searchFlightDTO) {
        String destination = searchFlightDTO.getDestination();
        LocalDate departureDate = searchFlightDTO.getDepartureDate();

        Flight flight = new Flight(destination, departureDate);

        return flight;
    }

    public static SearchFlightDTO getDTO(Flight flight) {
        String destination = flight.getDestination();
        LocalDate date = flight.getTimeOfDeparture().toLocalDate();
        SearchFlightDTO searchFlightDTO = new SearchFlightDTO(destination, date);
        return searchFlightDTO;
    }
}
