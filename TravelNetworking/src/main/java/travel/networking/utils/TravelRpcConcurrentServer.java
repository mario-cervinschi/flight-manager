package travel.networking.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import travel.networking.jsonprotocol.TravelClientJsonWorker;
import travel.networking.rpcprotocol.TravelClientRpcWorker;
import travel.services.ITravelServices;

import java.net.Socket;

public class TravelRpcConcurrentServer extends AbsConcurrentServer {
    private ITravelServices travelServices;
    private static Logger logger = LogManager.getLogger(TravelRpcConcurrentServer.class);

    public TravelRpcConcurrentServer(int port, ITravelServices travelServices) {
        super(port);
        this.travelServices = travelServices;
        logger.info("Travel - TravelRpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
//        TravelClientRpcWorker worker = new TravelClientRpcWorker(travelServices, client);
        TravelClientJsonWorker worker = new TravelClientJsonWorker(client, travelServices);

        Thread thread = new Thread(worker);
        return thread;
    }

    @Override
    public void stop() throws ServerException {
        logger.info("Stopping services ...");
    }
}
