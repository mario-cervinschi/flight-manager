
package com.flightapp.repository;

import com.flightapp.model.Route;
import com.flightapp.model.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRouteRepository extends JpaRepository<Route, Long> {

    List<Route> findByOriginAirportCode(String originAirportCode);

//    List<Route> findByDestinationAirportCode(String destinationAirportCode);

//    Route findByOriginAirportCodeAndDestinationAirportCode(String originCode, String destinationCode);

    @Query("SELECT r.destinationAirport FROM Route r WHERE r.originAirport.code = :airportCode")
    List<Airport> findDestinationsByOriginAirportCode(@Param("airportCode") String airportCode);

    @Query("SELECT r.originAirport FROM Route r WHERE r.destinationAirport.code = :airportCode")
    List<Airport> findOriginsByDestinationAirportCode(@Param("airportCode") String airportCode);
}
