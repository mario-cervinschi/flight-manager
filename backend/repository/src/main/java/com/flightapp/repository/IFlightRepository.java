package com.flightapp.repository;

import com.flightapp.model.Airport;
import com.flightapp.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IFlightRepository extends JpaRepository<Flight, Long> {

    @Query("""
        SELECT f FROM Flight f
        WHERE f.departure.code = :departureCode
          AND f.destination.code = :destinationCode
          AND YEAR(f.date) = :year
          AND MONTH(f.date) = :month
    """)
    List<Flight> findFlightsByRouteAndMonth(
            @Param("departureCode") String departureCode,
            @Param("destinationCode") String destinationCode,
            @Param("year") int year,
            @Param("month") int month
    );

    boolean existsByDepartureAndDestinationAndDate(Airport departure, Airport destination, LocalDate date);

    Optional<Flight> findTopByOrderByDateDesc();

    void deleteByDateBefore(LocalDate date);
}
