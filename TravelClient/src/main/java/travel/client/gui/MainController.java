package travel.client.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import travel.model.Ticket;
import travel.model.Agency;
import travel.model.Flight;
import travel.services.ITravelObserver;
import travel.services.ITravelServices;
import travel.services.TravelException;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements ITravelObserver {
    public TableColumn<Flight, String> flightsDestinationColumn;
    public TableColumn<Flight, String> flightsTakeOffColumn;
    public TableColumn<Flight, String> flightsAirportColumn;
    public TableColumn<Flight, String> flightsSeatsAvailableColumn;
    public TableView<Flight> flightsTable;
    public Label connectedAsLabel;
    public TextField destinationField;
    public DatePicker dateField;
    public TableView<Flight> searchCriteriaTable;
    public TableColumn<Flight, String> searchFlightsDestinationColumn;
    public TableColumn<Flight, String> searchFlightsTakeOffColumn;
    public TableColumn<Flight, String> searchFlightsSeatsAvailableColumn;
    public TableColumn<Flight, Void> searchFlightsBuyColumn;

    private final ObservableList<Flight> flights = FXCollections.observableArrayList();
    private final ObservableList<Flight> filteredFlights = FXCollections.observableArrayList();

    private ITravelServices server;
    private Agency connectedAgency;

    private static final Logger logger = LogManager.getLogger();

    private void initFlightsTable(){
        logger.info("Initializing flights table");
        flightsTable.getItems().clear();
        Flight[] fligts = server.getAllFlights();
        for(Flight flight : fligts){
            System.out.println(flight);
        }
        flights.addAll(fligts);
    }

    private void clearFilterFlights(){
        logger.info("Clearing filtered flights table");
        searchCriteriaTable.getItems().clear();
    }

    private void setConnectedAgency(){
        logger.info("Setting connected agency as {}", connectedAgency);
        connectedAsLabel.setText("Connected as " + connectedAgency.getAgencyName());
    }

    public void initApp(Agency connectedAgency){
        logger.traceEntry();
        flightsDestinationColumn.setCellValueFactory(new PropertyValueFactory<>("destination"));
        flightsTakeOffColumn.setCellValueFactory(new PropertyValueFactory<>("timeOfDeparture"));
        flightsAirportColumn.setCellValueFactory(new PropertyValueFactory<>("airportName"));
        flightsSeatsAvailableColumn.setCellValueFactory(new PropertyValueFactory<>("availableSeats"));

        flightsTable.setItems(flights);

        searchFlightsDestinationColumn.setCellValueFactory(new PropertyValueFactory<>("destination"));
        searchFlightsTakeOffColumn.setCellValueFactory(new PropertyValueFactory<>("timeOfDeparture"));
        searchFlightsSeatsAvailableColumn.setCellValueFactory(new PropertyValueFactory<>("availableSeats"));

        searchFlightsBuyColumn.setCellFactory(column -> new TableCell<Flight, Void>() {
            private final Button buyButton = new Button("Cumpara");
            {
                buyButton.setOnAction(event -> {
                    Flight flight = getTableView().getItems().get(getIndex());
                    try {
                        handleBuyButtonClick(flight, event);
                    } catch (IOException e) {
                        logger.error(e);
                        throw new RuntimeException(e);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    HBox container = new HBox(10, buyButton);
                    container.setAlignment(Pos.CENTER);
                    setGraphic(container);
                }
            }
        });

        searchCriteriaTable.setItems(filteredFlights);

        this.connectedAgency = connectedAgency;

        initFlightsTable();
        clearFilterFlights();
        setConnectedAgency();
        logger.traceExit("Successfully initialized the Main view");
    }

    private void handleBuyButtonClick(Flight flight, ActionEvent event) throws IOException {
        try {
            logger.info("Buy button clicked");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/buy-tickets-interface.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = stage.getScene();

            BuyTicketsController controller = loader.getController();
            controller.setFlight(flight);
            controller.setServer(server);
            controller.setMainController(this);
            controller.setParent(scene.getRoot());
            controller.setConnectedAgency(connectedAgency);

            scene.setRoot(root);
            stage.setTitle("Select Number of Tickets");
            logger.info("Successfully switched to buy interface");
        } catch (IOException e) {
            logger.error(e);
            e.printStackTrace();
        }
    }

    public void setServer(ITravelServices travelService) {
        logger.info("Setting service to {}", travelService);
        this.server = travelService;
    }


    public void onFindFlightsButtonClick(ActionEvent actionEvent) {
        logger.info("Find flights button clicked");
        var destination = destinationField.getText();
        var dateOfDeparture = dateField.getValue();

        if(!destination.isEmpty() && dateOfDeparture != null){
            clearFilterFlights();

            filteredFlights.addAll(server.getAllFlightsDestinationDate(destination, dateOfDeparture));
            if(filteredFlights.isEmpty()){
                logger.info("No flights found");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("No flights found");
                alert.showAndWait();
            }
        }
        else{
            logger.error("Empty fields for finding flights based on destination and date");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Empty fields");
            alert.showAndWait();
        }
    }

    public void onResetFlightButton(ActionEvent actionEvent) {
        logger.info("Reset flights button clicked");
        clearFilterFlights();
        destinationField.setText("");
        dateField.setValue(null);
    }

    public void onLogoutButtonClick(ActionEvent actionEvent) {
        logger.info("Logout button clicked");
        try {
            logout();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login-interface.fxml"));
            Parent loginRoot = loader.load();

            LogInController loginController = loader.getController();
            loginController.setServer(server);

            Stage currentStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            currentStage.setTitle("Login");
            currentStage.setScene(new Scene(loginRoot));

            currentStage.show();

            logger.info("Successfully logged out");
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
            logger.error(e.getMessage());
        }
    }

    void logout(){
        try{
            server.logout(connectedAgency, this);
        }catch(TravelException e){
            logger.error(e);
        }
    }

    @Override
    public void boughtTicket(Ticket ticket) {
        Platform.runLater(() -> {
            initFlightsTable();
            var destination = destinationField.getText();
            var dateOfDeparture = dateField.getValue();

            if(!destination.isEmpty() && dateOfDeparture != null) {
                clearFilterFlights();

                filteredFlights.addAll(server.getAllFlightsDestinationDate(destination, dateOfDeparture));
            }
        });
        logger.info("refreshed the available flights");
    }
}
