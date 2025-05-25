package travel.persistance.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCUtils {

    private final Properties jdbcProps;
    private static final Logger logger = LogManager.getLogger(JDBCUtils.class);
    private Connection instance = null;

    public JDBCUtils(Properties props) {
        this.jdbcProps = props;
        logger.info("Initialized JDBCUtils with properties: {}", props);
    }

    private Connection getNewConnection() {
        logger.info("trying to connect to database ... {}", jdbcProps.getProperty("jdbc.url"));
        logger.info("user: {}", jdbcProps.getProperty("jdbc.username"));
        logger.info("pass: {}", jdbcProps.getProperty("jdbc.password"));

        Connection con = null;
        try {
            if (jdbcProps.getProperty("jdbc.driver") != null) {
                Class.forName(jdbcProps.getProperty("jdbc.driver"));
            }

            String url = jdbcProps.getProperty("jdbc.url");
            if (url == null || url.isEmpty()) {
                throw new SQLException("The JDBC URL is null or empty");
            }

            String user = jdbcProps.getProperty("jdbc.username", "");
            String pass = jdbcProps.getProperty("jdbc.password", "");

            if (user == null || user.isEmpty()) {
                con = DriverManager.getConnection(url);
            } else {
                con = DriverManager.getConnection(url, user, pass);
            }

            logger.info("Successfully connected to database: {}", url);
        } catch (ClassNotFoundException e) {
            logger.error("Error loading database driver: {}", e.getMessage());
            logger.error(e);
        } catch (SQLException e) {
            logger.error(e);
        }
        return con;
    }

    public Connection getConnection() {
        try {
            if (instance == null || instance.isClosed()) {
                instance = getNewConnection();
            }
        } catch (SQLException e) {
            logger.error("Error getting connection: {}", e.getMessage());
            logger.error(e);
        }

        if (instance == null) {
            logger.error("Error getting connection: Connection is null");
        }

        return instance;
    }
}