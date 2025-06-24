package com.flightapp.services;

import com.flightapp.model.Airport;
import com.flightapp.model.Route;
import com.flightapp.repository.IRouteRepository;
import com.flightapp.repository.IAirportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RouteService {

    private final IRouteRepository routeRepository;
    private final IAirportRepository airportRepository;

    @Autowired
    public RouteService(IRouteRepository routeRepository, IAirportRepository airportRepository) {
        this.routeRepository = routeRepository;
        this.airportRepository = airportRepository;
    }

    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

    public List<Route> getRoutesByOriginAirport(String airportCode) {
        return routeRepository.findByOriginAirportCode(airportCode);
    }

    public List<Airport> getDestinationsForAirport(String airportCode) {
        List<Airport> directDestinations = routeRepository.findDestinationsByOriginAirportCode(airportCode);
        List<Airport> reverseDestinations = routeRepository.findOriginsByDestinationAirportCode(airportCode);

        Set<Airport> allDestinations = new HashSet<>();
        allDestinations.addAll(directDestinations);
        allDestinations.addAll(reverseDestinations);

        return new ArrayList<>(allDestinations);
    }


//    public Optional<Route> getDirectRoute(String originCode, String destinationCode) {
//        Route route = routeRepository.findByOriginAirportCodeAndDestinationAirportCode(originCode, destinationCode);
//        return Optional.ofNullable(route);
//    }

//    public Route createRoute(String originCode, String destinationCode, Double distance, Integer duration) {
//        Optional<Airport> originAirport = airportRepository.findById(originCode);
//        Optional<Airport> destinationAirport = airportRepository.findById(destinationCode);
//
//        if (originAirport.isEmpty() || destinationAirport.isEmpty()) {
//            throw new RuntimeException("One or both airports not found");
//        }
//
//        Route route = new Route();
//        route.setOriginAirport(originAirport.get());
//        route.setDestinationAirport(destinationAirport.get());
//        route.setDuration(duration);
//
//        return routeRepository.save(route);
//    }
//
//    public void deleteRoute(Long routeId) {
//        routeRepository.deleteById(routeId);
//    }
}
