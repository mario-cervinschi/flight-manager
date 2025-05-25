package travel.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;
import travel.model.Agency;
import travel.model.Flight;
import travel.model.Ticket;
import travel.persistance.interfaces.IAgencyRepository;
import travel.persistance.interfaces.IFlightRepository;
import travel.persistance.interfaces.ITicketRepository;
import travel.services.ITravelObserver;
import travel.services.ITravelServices;
import travel.services.TravelException;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TravelServicesImpl implements ITravelServices {
    private IAgencyRepository agencyRepository;
    private IFlightRepository flightRepository;
    private ITicketRepository ticketRepository;
    private static Logger logger = LogManager.getLogger(TravelServicesImpl.class);

    private Map<Long, ITravelObserver> loggedClients;

    public TravelServicesImpl(IAgencyRepository agencyRepository, IFlightRepository flightRepository, ITicketRepository ticketRepository) {
        this.agencyRepository = agencyRepository;
        this.flightRepository = flightRepository;
        this.ticketRepository = ticketRepository;
        loggedClients = new ConcurrentHashMap<>();
        logger.traceEntry();
    }

    @Override
    public synchronized Agency login(Agency agency, ITravelObserver client) throws TravelException {
        logger.traceEntry();
        Agency agencyR = agencyRepository.findByEmail(agency.getEmail()).orElse(null);
        if(agencyR != null) {
            if(loggedClients.get(agencyR.getId()) != null) {
                throw new TravelException("Already logged in");
            }
            if(!BCrypt.checkpw(agency.getPassword(), agencyR.getPassword())){
                throw new TravelException("Wrong password");
            }
            loggedClients.put(agencyR.getId(), client);
        }
        else{
            throw new TravelException("Email does not exist");
        }
        return agencyR;
    }

    @Override
    public synchronized Agency saveAgency(Agency agency) throws TravelException {
        logger.traceEntry("Saving agency " + agency);
        if(agencyRepository.findByEmail(agency.getEmail()).isEmpty()) {
            return agencyRepository.save(agency).orElse(null);
        }
        else{
            throw new TravelException("Agency already exists");
        }
    }

    @Override
    public synchronized Flight[] getAllFlights() throws TravelException {
        logger.traceEntry();
        Iterable<Flight> flights = flightRepository.findAll();
        Set<Flight> result = new TreeSet<>();
        flights.forEach(result::add);
        return result.toArray(new Flight[result.size()]);
    }

    @Override
    public synchronized Flight[] getAllFlightsDestinationDate(String destination, LocalDate date) throws TravelException {
        logger.traceEntry();
        Iterable<Flight> flights = flightRepository.findAllFlightsDestinationDateAboveZero(destination, date);
        Set<Flight> result = new TreeSet<>();
        flights.forEach(result::add);
        return result.toArray(new Flight[result.size()]);
    }

    @Override
    public synchronized Flight updateFlight(Flight flight) throws TravelException {
        return null;
    }

    @Override
    public synchronized Ticket addTicket(Ticket ticket) throws TravelException {
        logger.traceEntry();
        Flight flight = ticket.getFlight();
        flight.setAvailableSeats(flight.getAvailableSeats() - ticket.getNoOfSeats());
        flight = flightRepository.update(flight).orElseThrow(() -> new TravelException("Could not update flight"));
        ticket.setFlight(flight);
        ticketRepository.save(ticket).orElseThrow(() -> new TravelException("Could not save ticket"));
        for(var entry : loggedClients.entrySet()) {
            try{
//                loggedClients.put(agency.getId(), client);
                entry.getValue().boughtTicket(ticket);
            }catch(TravelException e){
                logger.error(e);
            }
        }
        return ticket;
    }

    @Override
    public synchronized void logout(Agency agency, ITravelObserver client) throws TravelException {
        logger.traceEntry();
        ITravelObserver localClient = loggedClients.remove(agency.getId());
        if(localClient == null) {
            throw new TravelException("Agency " + agency.getEmail() + " not logged in");
        }
    }
}
