package travel.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import travel.client.gui.LogInController;
import travel.client.gui.MainController;
import travel.networking.jsonprotocol.TravelServicesJsonProxy;
import travel.networking.rpcprotocol.TravelServicesRpcProxy;
import travel.services.ITravelServices;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class StartClientRpc extends Application {

    private static int defaultPort = 55555;
    private static String defaultServer = "localhost";

    private static Logger logger = LogManager.getLogger(StartClientRpc.class);

    public void start(Stage primaryStage) throws IOException {
        logger.debug("in start");
        Properties clientProps = new Properties();
        try{
            clientProps.load(StartClientRpc.class.getResourceAsStream("/travelclient.properties"));
            logger.info("Client properties set {}" , clientProps);
        } catch (Exception e){
            logger.error("Cannot find travelclient.properties " + e);
            logger.debug("Looking into floder {}", (new File(".")).getAbsolutePath());
            return;
        }
        String serverIp = clientProps.getProperty("travel.server.host", defaultServer);
        int serverPort = defaultPort;

        try{
            serverPort = Integer.parseInt(clientProps.getProperty("travel.server.port"));
        }catch (NumberFormatException e){
            logger.error("Wrong port number " + e.getMessage());
            logger.debug("Using default port {}", defaultPort);
        }
        logger.info("Using server ip {}", serverIp);
        logger.info("Using server port {}", serverPort);

//        ITravelServices server = new TravelServicesRpcProxy(serverIp, serverPort);
        ITravelServices server = new TravelServicesJsonProxy(serverIp, serverPort);

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/login-interface.fxml"));
        Parent root=loader.load();
        LogInController logInController =
                loader.<LogInController>getController();
        logInController.setServer(server);

        primaryStage.setTitle("MPP chat");
        primaryStage.setScene(new Scene(root, 640, 400));
        primaryStage.show();
    }
}
