import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import travel.networking.utils.AbstractServer;
import travel.networking.utils.ServerException;
import travel.networking.utils.TravelRpcConcurrentServer;
import travel.persistance.database.AgencyRepositoryDB;
import travel.persistance.database.FlightRepositoryDB;
import travel.persistance.database.TicketRepositoryDB;
import travel.persistance.hibernate.AgencyDBRepoHibernate;
import travel.persistance.hibernate.FlightDBRepoHibernate;
import travel.persistance.interfaces.IAgencyRepository;
import travel.persistance.interfaces.IFlightRepository;
import travel.persistance.interfaces.ITicketRepository;
import travel.server.TravelServicesImpl;
import travel.services.ITravelServices;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class StartRpcServer {
    private static int defaultPort=55555;
    private static Logger logger = LogManager.getLogger(StartRpcServer.class);
    public static void main(String[] args) {
        Properties serverProps=new Properties();
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/travelserver.properties"));
            logger.info("Server properties set {}",serverProps);
        } catch (IOException e) {
            logger.error("Cannot find travelserver.properties "+e);
            logger.debug("Looking for file in "+(new File(".")).getAbsolutePath());
            return;
        }
//        IAgencyRepository agencyRepository = new AgencyRepositoryDB(serverProps);
        IAgencyRepository agencyRepository = new AgencyDBRepoHibernate();
//        IFlightRepository flightRepository = new FlightRepositoryDB(serverProps);
        IFlightRepository flightRepository = new FlightDBRepoHibernate();
        ITicketRepository ticketRepository = new TicketRepositoryDB(serverProps, flightRepository, agencyRepository);

        ITravelServices travelServerImpl = new TravelServicesImpl(agencyRepository, flightRepository, ticketRepository);
        int travelServerPort=defaultPort;
        try {
            travelServerPort = Integer.parseInt(serverProps.getProperty("travel.server.port"));
        }catch (NumberFormatException nef){
            logger.error("Wrong  Port Number"+nef.getMessage());
            logger.debug("Using default port "+defaultPort);
        }
        logger.debug("Starting server on port: "+ travelServerPort);
        AbstractServer server = new TravelRpcConcurrentServer(travelServerPort, travelServerImpl);
        try {
            server.start();
        } catch (ServerException e) {
            logger.error("Error starting the server" + e.getMessage());
        }finally {
            try {
                server.stop();
            }catch(ServerException e){
                logger.error("Error stopping server "+e.getMessage());
            }
        }
    }
}