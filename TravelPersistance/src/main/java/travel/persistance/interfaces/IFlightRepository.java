package travel.persistance.interfaces;

import org.springframework.stereotype.Repository;
import travel.persistance.IRepository;
import travel.model.Flight;

import java.time.LocalDate;

public interface IFlightRepository extends IRepository<Long, Flight> {
    Iterable<Flight> findAllFlightsDestinationDateAboveZero(String destination, LocalDate date);
}
