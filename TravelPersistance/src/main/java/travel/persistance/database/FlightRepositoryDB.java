package travel.persistance.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import travel.model.Flight;
import travel.persistance.interfaces.IFlightRepository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

@Repository
public class FlightRepositoryDB implements IFlightRepository {

    private final JDBCUtils utils;
    private static final Logger logger = LogManager.getLogger(FlightRepositoryDB.class);

    @Autowired
    public FlightRepositoryDB(Properties properties) {
        logger.info("FlightRepositoryDB initialized with proprieties: {}", properties);
        utils = new JDBCUtils(properties);
    }

    @Override
    public Optional<Flight> findOne(Long id) {
        logger.traceEntry();
        Connection con = utils.getConnection();
        try (PreparedStatement ps = con.prepareStatement("select * from Flights where id=?")) {
            ps.setLong(1, id);
            try (ResultSet result = ps.executeQuery()) {
                if (result.next()) {
                    Long idFlight = result.getLong("id");
                    String destination = result.getString("destination");
                    Timestamp timeOfDeparture = result.getTimestamp("time_of_departure");
                    String airportName = result.getString("airport_name");
                    Long availableSeats = result.getLong("available_seats");

                    LocalDateTime date = timeOfDeparture != null ? timeOfDeparture.toLocalDateTime() : null;

                    Flight flight = new Flight(destination, date, airportName, availableSeats);
                    flight.setId(idFlight);
                    logger.traceExit("Flight found with id: {}", idFlight);
                    return Optional.of(flight);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Flight> findAll() {
        logger.traceEntry();
        Map<Long, Flight> entities = new HashMap<>();
        Connection con = utils.getConnection();
        try (PreparedStatement ps = con.prepareStatement("select * from Flights")) {
            try (ResultSet result = ps.executeQuery()) {
                while (result.next()) {
                    Long idFlight = result.getLong("id");
                    String destination = result.getString("destination");
                    Timestamp timeOfDeparture = result.getTimestamp("time_of_departure");
                    String airportName = result.getString("airport_name");
                    Long availableSeats = result.getLong("available_seats");

                    LocalDateTime date = timeOfDeparture != null ? timeOfDeparture.toLocalDateTime() : null;

                    Flight flight = new Flight(destination, date, airportName, availableSeats);
                    flight.setId(idFlight);

                    entities.put(flight.getId(), flight);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        logger.traceExit("Found {} flights", entities.size());
        return entities.values();
    }

    @Override
    public Optional<Flight> save(Flight entity) {
        logger.traceEntry("saving task: {}", entity);
        Connection con = utils.getConnection();
        if(entity == null) {
            logger.error("Entity null");
            throw new IllegalArgumentException("Entity cannot be null");
        }

        try (PreparedStatement ps = con.prepareStatement("INSERT INTO Flights (destination, time_of_departure, airport_name, available_seats) values (?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)){
            ps.setString(1, entity.getDestination());
            ps.setTimestamp(2, Timestamp.valueOf(entity.getTimeOfDeparture()));
            ps.setString(3, entity.getAirportName());
            ps.setLong(4, entity.getAvailableSeats());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    entity.setId(rs.getLong(1));
                }
            }
        }
        catch(SQLException e) {
            logger.error(e);
        }
        logger.traceExit("Flight saved with id: {}", entity.getId());
        return Optional.of(entity);
    }

    @Override
    public Optional<Flight> delete(Long aLong) {
        logger.traceEntry("deleting flight with {}", aLong);
        Connection con = utils.getConnection();
        Optional<Flight> flight = findOne(aLong);
        if (flight.isPresent()) {
            try (PreparedStatement ps = con.prepareStatement("DELETE FROM Flights WHERE id = ?")){
                ps.setLong(1, aLong);
                ps.executeUpdate();
                logger.traceExit("Deleted flight with id: {}", aLong);
                return flight;
            }catch (SQLException e) {
                logger.error(e);
            }
        }
        logger.traceExit("No flight found with id: {}", aLong);
        return Optional.empty();
    }

    @Override
    public Optional<Flight> update(Flight entity) {
        logger.traceEntry("updating flight with {}", entity);
        Connection con = utils.getConnection();

        if (entity.getId() == null) {
            logger.error("Entity ID null");
            throw new IllegalArgumentException("Entity cannot be null");
        }

        try (PreparedStatement ps = con.prepareStatement("UPDATE Flights SET destination = ?, time_of_departure = ?, airport_name = ?, available_seats = ? WHERE id = ?")){
            ps.setString(1, entity.getDestination());

            long timestampMilliseconds = entity.getTimeOfDeparture()
                    .atZone(ZoneId.systemDefault())  // Convert to system default time zone
                    .toInstant()                      // Convert to Instant
                    .toEpochMilli();

            ps.setLong(2, timestampMilliseconds);
            ps.setString(3, entity.getAirportName());
            ps.setLong(4, entity.getAvailableSeats());
            ps.setLong(5, entity.getId());

            ps.executeUpdate();
            logger.traceExit("Updated flight with id: {}", entity.getId());
            return Optional.of(entity);
        }
        catch(SQLException e) {
            logger.error(e);
        }
        logger.traceExit("No flight found with id: {}", entity.getId());
        return Optional.empty();
    }

    @Override
    public Iterable<Flight> findAllFlightsDestinationDateAboveZero(String destination, LocalDate date) {
        logger.traceEntry();
        Connection con = utils.getConnection();
        Map<Long, Flight> entities = new HashMap<>();

        LocalDateTime startOfDay = date.atStartOfDay(); // Midnight (00:00)
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX); //

        try (PreparedStatement ps = con.prepareStatement("select * from Flights where available_seats > 0 AND destination = ? and time_of_departure BETWEEN ? AND ?")) {
            ps.setString(1, destination);
            ps.setTimestamp(2, Timestamp.valueOf(startOfDay));  // Start of day
            ps.setTimestamp(3, Timestamp.valueOf(endOfDay));    // End of day
            try (ResultSet result = ps.executeQuery()) {
                if (result.next()) {
                    Long idFlight = result.getLong("id");
                    Timestamp timeOfDeparture = result.getTimestamp("time_of_departure");
                    String airportName = result.getString("airport_name");
                    Long availableSeats = result.getLong("available_seats");

                    LocalDateTime dateResulted = timeOfDeparture != null ? timeOfDeparture.toLocalDateTime() : null;

                    Flight flight = new Flight(destination, dateResulted, airportName, availableSeats);
                    flight.setId(idFlight);

                    entities.put(flight.getId(), flight);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        logger.traceExit("Found {} flights", entities.size());
        return entities.values();
    }
}
