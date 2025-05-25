package travel.persistance.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import travel.model.Agency;
import travel.persistance.interfaces.IAgencyRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

public class AgencyRepositoryDB implements IAgencyRepository {

    private final JDBCUtils utils;
    private static final Logger logger = LogManager.getLogger();

    public AgencyRepositoryDB(Properties properties) {
        logger.info("AgencyRepositoryDB initialized with proprieties: {}", properties);
        utils = new JDBCUtils(properties);
    }

    @Override
    public Optional<Agency> findByEmail(String email) {
        logger.traceEntry();
        Connection con = utils.getConnection();
        try (PreparedStatement ps = con.prepareStatement("select * from Agencies where email=?")) {
            ps.setString(1, email);
            try (ResultSet result = ps.executeQuery()) {
                if (result.next()) {
                    Long idAgency = result.getLong("id");
                    String agencyName = result.getString("agency_name");
                    String emailResult = result.getString("email");
                    String password = result.getString("password");

                    Agency agency = new Agency(agencyName, emailResult, password);
                    agency.setId(idAgency);

                    logger.traceExit("Found Agency with email: " + email);
                    return Optional.of(agency);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        logger.traceExit("No Agency found with email: " + email);
        return Optional.empty();
    }

    @Override
    public Optional<Agency> findOne(Long id) {
        logger.traceEntry();
        Connection con = utils.getConnection();
        try (PreparedStatement ps = con.prepareStatement("select * from Agencies where id=?")) {
            ps.setLong(1, id);
            try (ResultSet result = ps.executeQuery()) {
                if (result.next()) {
                    Long idAgency = result.getLong("id");
                    String agencyName = result.getString("agency_name");
                    String email = result.getString("email");
                    String password = result.getString("password");

                    Agency agency = new Agency(agencyName, email, password);
                    agency.setId(idAgency);

                    logger.traceExit("Found Agency with id: " + id);
                    return Optional.of(agency);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        logger.traceExit("No Agency found with id: " + id);
        return Optional.empty();
    }

    @Override
    public Iterable<Agency> findAll() {
        logger.traceEntry();
        Map<Long, Agency> agencies = new HashMap<>();
        Connection con = utils.getConnection();
        try (PreparedStatement ps = con.prepareStatement("select * from Agencies")) {
            try (ResultSet result = ps.executeQuery()) {
                while (result.next()) {
                    Long idAgency = result.getLong("id");
                    String agencyName = result.getString("agency_name");
                    String email = result.getString("email");
                    String password = result.getString("password");

                    Agency agency = new Agency(agencyName, email, password);
                    agency.setId(idAgency);

                    agencies.put(agency.getId(), agency);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        logger.traceExit("Found {} Agencies", agencies.size());
        return agencies.values();
    }

    @Override
    public Optional<Agency> save(Agency entity) {
        logger.traceEntry("saving task: {}", entity);
        Connection con = utils.getConnection();
        if(entity == null) {
            logger.error("Entity null");
            throw new IllegalArgumentException("Entity cannot be null");
        }

        try (PreparedStatement ps = con.prepareStatement("INSERT INTO Agencies (agency_name, email, password) values (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)){
            ps.setString(1, entity.getAgencyName());
            ps.setString(2, entity.getEmail());
            ps.setString(3, entity.getPassword());
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
        logger.traceExit("Saved agency: {}", entity);
        return Optional.of(entity);
    }

    @Override
    public Optional<Agency> delete(Long aLong) {
        logger.traceEntry("deleting agency with {}", aLong);
        Connection con = utils.getConnection();
        Optional<Agency> agency = findOne(aLong);
        if (agency.isPresent()) {
            try (PreparedStatement ps = con.prepareStatement("DELETE FROM Agencies WHERE id = ?")){
                ps.setLong(1, aLong);
                ps.executeUpdate();

                logger.traceExit("Deleted agency with id: {}", aLong);
                return agency;
            }catch (SQLException e) {
                logger.error(e);
            }
        }
        logger.traceExit("No Agency found with id: {}", aLong);
        return Optional.empty();
    }

    @Override
    public Optional<Agency> update(Agency entity) {
        logger.traceEntry("updating agency with {}", entity);
        Connection con = utils.getConnection();

        if (entity.getId() == null) {
            logger.error("Entity ID null");
            throw new IllegalArgumentException("Entity cannot be null");
        }

        try (PreparedStatement ps = con.prepareStatement("UPDATE Agencies SET agency_name = ?, email = ?, password = ? WHERE id = ?")){
            ps.setString(1, entity.getAgencyName());
            ps.setString(2, entity.getEmail());
            ps.setString(3, entity.getPassword());
            ps.setLong(4, entity.getId());

            ps.executeUpdate();

            logger.traceExit("Updated agency with id: {}", entity.getId());
            return Optional.of(entity);
        }
        catch(SQLException e) {
            logger.error(e);
        }
        logger.traceExit("No Agency found with id: {}", entity.getId());
        return Optional.empty();
    }

}
