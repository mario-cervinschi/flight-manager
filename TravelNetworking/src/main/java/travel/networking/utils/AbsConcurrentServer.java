package travel.networking.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.Socket;

public abstract class AbsConcurrentServer extends AbstractServer {
    private static Logger logger = LogManager.getLogger(AbsConcurrentServer.class);

    public AbsConcurrentServer(int port) {
        super(port);
        logger.debug("AbsConcurrentServer started");
    }

    @Override
    protected void processRequest(Socket client) {
        logger.info("Processing request from " + client.getRemoteSocketAddress());
        Thread thread = createWorker(client);
        thread.start();
    }

    protected abstract Thread createWorker(Socket client);
}
