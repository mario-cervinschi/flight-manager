package travel.client.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import travel.client.validator.TicketValidator;
import travel.model.Agency;
import travel.model.Flight;
import travel.model.Ticket;
import travel.services.ITravelServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BuyTicketsController {
    public ComboBox<Integer> ticketCountField;
    public Button confirmButton;
    public Label flightLabel;

    private Flight currentFlight;
    private ITravelServices server;
    private Agency connectedAgency;

    private Parent parent;
    private MainController mainController;

    private final TicketValidator ticketValidator = new TicketValidator();

    private static final Logger logger = LogManager.getLogger();

    public void setServer(ITravelServices server) {
        logger.info("Setting service in BuyTicketsController");
        this.server = server;
    }

    public void setConnectedAgency(Agency connectedAgency) {
        logger.info("Setting connected agency in BuyTicketsController as {}", connectedAgency);
        this.connectedAgency = connectedAgency;
    }

    public void setFlight(Flight flight) {
        logger.info("Setting current flight in BuyTicketsController as {}", flight);
        this.currentFlight = flight;
        flightLabel.setText("Flight to " + flight.getDestination() + " at " + flight.getTimeOfDeparture());
        populateComboBox(flight.getAvailableSeats());
    }

    public void handleOnConfirmButton(ActionEvent actionEvent) {
        logger.info("Confirm number of seats button clicked");
        Integer selectedNoTickets = ticketCountField.getSelectionModel().getSelectedItem();

        if (selectedNoTickets <= currentFlight.getAvailableSeats()) {
            VBox layout = new VBox(10);

            Button goBackButton = new Button("←");
            goBackButton.setOnAction(event -> {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/buy-tickets-interface.fxml"));
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                Stage stage = (Stage) goBackButton.getScene().getWindow();

                BuyTicketsController controller = loader.getController();
                controller.setFlight(currentFlight);
                controller.setServer(server);
                controller.setParent(parent);
                controller.setMainController(mainController);
                controller.setConnectedAgency(connectedAgency);

                stage.setScene(new Scene(root, 300, 200));
                stage.setTitle("Select Number of Tickets");
                stage.show();
                logger.info("Selected a valid number of seats. Switching to name input view.");
            });
            goBackButton.setPrefWidth(30);
            goBackButton.setPrefHeight(30);

            layout.getChildren().add(goBackButton);

            List<TextField> passengerFields = new ArrayList<>();

            for (int i = 0; i < selectedNoTickets; i++) {
                TextField passengerField = new TextField();
                passengerField.setPromptText("Passenger's " + (i + 1) + " Name");
                layout.getChildren().add(passengerField);
                passengerFields.add(passengerField);
            }

            Button buyButton = new Button("Buy Tickets");
            layout.getChildren().add(buyButton);

            buyButton.setOnAction(event -> {
                logger.info("Buy Tickets button clicked");
                boolean allFilled = passengerFields.stream().allMatch(tf -> !tf.getText().trim().isEmpty());

                if (allFilled) {
                    logger.info("All fields are filled");
                    List<String> names = passengerFields.stream().map(TextField::getText).collect(Collectors.toList());
                    try{
                        Ticket ticket = new Ticket(selectedNoTickets, names, currentFlight, connectedAgency);
                        ticketValidator.validate(ticket);
                        Flight updatedFlight = new Flight(currentFlight.getDestination(), currentFlight.getTimeOfDeparture(), currentFlight.getAirportName(), currentFlight.getAvailableSeats() - selectedNoTickets);
                        updatedFlight.setId(currentFlight.getId());

                        server.addTicket(ticket);

                        Stage stage = (Stage) buyButton.getScene().getWindow();
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Successfully bought the tickets");
                        alert.show();
                        logger.info("Successfully bought the tickets");
                        goToMainInterface(stage);
                    }catch (Exception e) {
                        logger.error(e.getMessage());
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText(e.getMessage());
                        alert.showAndWait();
                    }
                } else {
                    logger.warn("Incomplete fields when buying tickets");
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Incomplete Information");
                    alert.setContentText("Please fill in all passenger names before confirming!");
                    alert.showAndWait();
                }
            });

            Stage stage = (Stage) ticketCountField.getScene().getWindow();
            stage.setScene(new Scene(layout, 300, 400));
        } else {
            logger.warn("Bigger number of tickets selected than available seats");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Selected number of tickets is higher than available seats.");
            alert.showAndWait();
        }
    }

    private void populateComboBox(Long seats) {
        logger.info("Populating the \"number of seats ComboBox\" with values");
        ticketCountField.getItems().clear();
        for (int i = 1; i <= seats; i++) {
            ticketCountField.getItems().add(i);
        }
    }

    private void goToMainInterface(Stage stage) throws IOException {
        logger.info("Creating the main interface view");

        if (mainController != null) {
            mainController.setServer(server);  // Set the server for the mainController
            mainController.initApp(connectedAgency);  // Initialize with connectedAgency

            Scene currentScene = stage.getScene();
            currentScene.setRoot(parent);
            stage.setScene(currentScene);
            stage.setTitle("Main Interface");
            stage.show();
        } else {
            logger.error("Main controller is not initialized properly.");
        }

        if (parent == null) {
            logger.error("Parent node is not set. Cannot switch to main interface.");
            return;
        }

        logger.info("Successfully moved to main interface");
    }

    public void onBackButtonCLick(ActionEvent actionEvent) {
        logger.info("Back button clicked");
        try {
            Stage stage = (Stage) confirmButton.getScene().getWindow();
            goToMainInterface(stage);
        } catch (Exception e) {
            logger.error(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An error occurred while switching the scene.");
            alert.setContentText("Unable to load the previous screen.");
            alert.showAndWait();
        }
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setParent(Parent root) {
        this.parent = root;
    }
}
