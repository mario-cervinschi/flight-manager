package travel.persistance.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import travel.model.Agency;
import travel.model.Flight;
import travel.model.Ticket;
import travel.persistance.interfaces.IAgencyRepository;
import travel.persistance.interfaces.IFlightRepository;
import travel.persistance.interfaces.ITicketRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class TicketRepositoryDB implements ITicketRepository {

    private JDBCUtils utils;
    private static final Logger logger = LogManager.getLogger();
    private IFlightRepository flightRepository;
    private IAgencyRepository agencyRepository;

    public TicketRepositoryDB(Properties properties, IFlightRepository flightRepo, IAgencyRepository agencyRepo) {
        logger.info("TicketRepositoryDB initialized with proprieties: {}", properties);
        utils = new JDBCUtils(properties);
        flightRepository = flightRepo;
        agencyRepository = agencyRepo;
    }

    @Override
    public Optional<Ticket> findOne(Long id) {
        logger.traceEntry();
        Connection con = utils.getConnection();
        try (PreparedStatement ps = con.prepareStatement("select * from Tickets where id=?")) {
            ps.setLong(1, id);
            try (ResultSet result = ps.executeQuery()) {
                if (result.next()) {
                    Long idTicket = result.getLong("id");
                    Integer nrSeats = result.getInt("nr_seats");

                    Long idFlight = result.getLong("id_flight");
                    Flight flight = flightRepository.findOne(idFlight).orElse(null);

                    Long idAgency = result.getLong("id_agency");
                    Agency agency = agencyRepository.findOne(idAgency).orElse(null);

                    List<String> touristNames = new ArrayList<>();

                    try(PreparedStatement psTourist = con.prepareStatement("select tourist_name from TicketTouristNames where id_ticket = ?")) {
                        psTourist.setLong(1, idTicket);
                        try (ResultSet resultTourist = psTourist.executeQuery()) {
                            while(resultTourist.next()) {
                                String touristName = resultTourist.getString("tourist_name");
                                touristNames.add(touristName);
                            }
                        }
                    }

                    if(flight != null && agency != null) {
                        Ticket ticket = new Ticket(nrSeats, touristNames, flight, agency);
                        ticket.setId(idTicket);
                        logger.traceExit("Ticket found: {}", ticket);
                        return Optional.of(ticket);
                    }
                    else{
                        logger.error("Flight or agency not found");
                    }

                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Ticket> findAll() {
        logger.traceEntry();
        Connection con = utils.getConnection();
        Map<Long, Ticket> entities = new HashMap<>();
        try (PreparedStatement ps = con.prepareStatement("select * from Tickets")) {
            try (ResultSet result = ps.executeQuery()) {
                while (result.next()) {
                    Long idTicket = result.getLong("id");
                    Integer nrSeats = result.getInt("nr_seats");

                    Long idFlight = result.getLong("id_flight");
                    Flight flight = flightRepository.findOne(idFlight).orElse(null);

                    Long idAgency = result.getLong("id_agency");
                    Agency agency = agencyRepository.findOne(idAgency).orElse(null);

                    List<String> touristNames = new ArrayList<>();

                    try(PreparedStatement psTourist = con.prepareStatement("select tourist_name from TicketTouristNames where id_ticket = ?")) {
                        psTourist.setLong(1, idTicket);
                        try (ResultSet resultTourist = psTourist.executeQuery()) {
                            while(resultTourist.next()) {
                                String touristName = resultTourist.getString("tourist_name");
                                touristNames.add(touristName);
                            }
                        }
                    }

                    if(flight != null && agency != null) {
                        Ticket ticket = new Ticket(nrSeats, touristNames, flight, agency);
                        ticket.setId(idTicket);

                        entities.put(ticket.getId(), ticket);
                    }
                    else{
                        logger.error("Flight or agency not found");
                    }
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        logger.traceExit("Found {} tickets", entities.size());
        return entities.values();
    }

    @Override
    public Optional<Ticket> save(Ticket entity) {
        logger.traceEntry("saving task: {}", entity);
        Connection con = utils.getConnection();
        if(entity == null) {
            logger.error("Entity null");
            throw new IllegalArgumentException("Entity cannot be null");
        }

        try (PreparedStatement ps = con.prepareStatement("INSERT INTO Tickets (nr_seats, id_flight, id_agency) values (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)){
            ps.setInt(1, entity.getNoOfSeats());
            ps.setLong(2, entity.getFlight().getId());
            ps.setLong(3, entity.getAgency().getId());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    entity.setId(rs.getLong(1));
                }
            }

            try (PreparedStatement psTourists = con.prepareStatement("INSERT INTO TicketTouristNames(id_ticket, tourist_name) values (?, ?)")){
                for (String t : entity.getTouristNames()) {
                    psTourists.setLong(1, entity.getId());
                    psTourists.setString(2, t);
                    psTourists.executeUpdate();
                }
            }

        }
        catch(SQLException e) {
            logger.error(e);
        }
        logger.traceExit("Ticket saved: {}", entity);
        return Optional.of(entity);
    }

    @Override
    public Optional<Ticket> delete(Long aLong) {

        logger.traceEntry("deleting ticket with {}", aLong);
        Connection con = utils.getConnection();
        Optional<Ticket> ticket = findOne(aLong);
        if (ticket.isPresent()) {

            try (PreparedStatement ps = con.prepareStatement("DELETE FROM Tickets WHERE id = ?")){
                ps.setLong(1, aLong);
                ps.executeUpdate();

                try (PreparedStatement psDelete = con.prepareStatement("DELETE FROM TicketTouristNames WHERE id_ticket = ?")) {
                    psDelete.setLong(1, aLong);
                    psDelete.executeUpdate();
                }
                logger.traceExit("Ticket deleted with id: {}", aLong);
                return ticket;
            }catch (SQLException e) {
                logger.error(e);
            }
        }
        else{
            logger.error("Ticket not found");
        }
        return Optional.empty();
    }

    @Override
    public Optional<Ticket> update(Ticket entity) {
        logger.traceEntry("updating ticket with {}", entity);
        Connection con = utils.getConnection();

        if (entity.getId() == null) {
            logger.error("Entity ID null");
            throw new IllegalArgumentException("Entity cannot be null");
        }

        try (PreparedStatement ps = con.prepareStatement("UPDATE Tickets SET nr_seats = ?, id_flight = ?, id_agency = ? WHERE id = ?")){
            ps.setInt(1, entity.getNoOfSeats());
            ps.setLong(2, entity.getFlight().getId());
            ps.setLong(3, entity.getAgency().getId());
            ps.setLong(4, entity.getId());

            ps.executeUpdate();

            try (PreparedStatement psDelete = con.prepareStatement("DELETE FROM TicketTouristNames WHERE id_ticket = ?")) {
                psDelete.setLong(1, entity.getId());
                psDelete.executeUpdate();
            }

            try (PreparedStatement psTourists = con.prepareStatement("INSERT INTO TicketTouristNames(id_ticket, tourist_name) values (?, ?)")){
                for (String t : entity.getTouristNames()) {
                    psTourists.setLong(1, entity.getId());
                    psTourists.setString(2, t);
                    psTourists.executeUpdate();
                }
            }
            logger.traceExit("Ticket updated with id: {}", entity.getId());
            return Optional.of(entity);
        }
        catch(SQLException e) {
            logger.error(e);
        }
        return Optional.empty();
    }
}
