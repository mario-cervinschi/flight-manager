package travel.client.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import travel.client.validator.AgencyValidator;
import travel.client.validator.ValidatorException;
import travel.model.Agency;
import travel.services.ITravelServices;
import travel.services.TravelException;

import java.io.IOException;

public class LogInController {
    public TextField email;
    public PasswordField password;
    public Button btnLogIn;
    public Button btnSignUp;
    public TextField agencyName;
    public TextField signUpEmail;
    public TextField signUpPassword;
    public TextField confirmPassword;

    private final AgencyValidator agencyValidator = new AgencyValidator();
    private ITravelServices server;
    private Agency loggedAgency;

    private static final Logger logger = LogManager.getLogger();

    public void setServer(ITravelServices service) {
        logger.info("Setting Login service");
        this.server = service;
    }

    public void onLogInButtonClick(ActionEvent actionEvent) {
        //mainParent root

        String email = this.email.getText();
        String password = this.password.getText();
        loggedAgency = new Agency(email, password);

        try{
            logger.info("Logged in successfully");

            FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/main-interface.fxml"));
            Parent mainRoot = mainLoader.load();

            MainController mainController = mainLoader.getController();
            loggedAgency = server.login(loggedAgency, mainController);
            mainController.setServer(server);

            Stage stage = (Stage) btnLogIn.getScene().getWindow();
            stage.setScene(new Scene(mainRoot));
            stage.setTitle("Main Interface");

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    mainController.logout();
                    logger.info("Logged out successfully");
                    System.exit(0);
                }
            });

            stage.show();
            mainController.initApp(loggedAgency);


        }catch(TravelException e){
            logger.error(e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onSignUpButtonClick(ActionEvent actionEvent) throws IOException {
        logger.info("Sign up button clicked");
        initSignUpView(actionEvent);
    }

    private void initSignUpView(ActionEvent actionEvent) throws IOException {
        logger.info("Creating Sign Up View");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/signup-interface.fxml"));

        Parent newView = fxmlLoader.load();

        LogInController signUpController = fxmlLoader.getController();
        signUpController.setServer(server);

        Stage currentStage = (Stage) btnLogIn.getScene().getWindow();
        currentStage.getScene().setRoot(newView);
        logger.info("Switched to Sign Up View");
    }

    private void goToLogInInterface(ActionEvent actionEvent) throws IOException {
        logger.info("Creating Log In Interface");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/login-interface.fxml"));

        Parent newView = fxmlLoader.load();

        LogInController logInController = fxmlLoader.getController();
        logInController.setServer(server);

        Stage currentStage = (Stage) btnSignUp.getScene().getWindow();

        currentStage.getScene().setRoot(newView);
        logger.info("Switched to Log In Interface");
    }

    public void onBackButtonCLick(ActionEvent actionEvent) {
        try {
            goToLogInInterface(actionEvent);
        }catch (IOException e){
            logger.error(e.getMessage());
        }
    }

    public void onSignUpButtonCLick(ActionEvent actionEvent) {
        try{
            logger.info("Sign Up button clicked");
            String name = agencyName.getText();
            String email = signUpEmail.getText();
            String password = signUpPassword.getText();

            agencyValidator.validate(name, email, password);

            Agency agency = new Agency(agencyName.getText(), signUpEmail.getText(), signUpPassword.getText());

            server.saveAgency(agency);

            goToLogInInterface(actionEvent);
        }catch (IOException e){
            logger.error(e.getMessage());
        }catch(TravelException | ValidatorException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(e.getMessage());
            logger.error(e.getMessage());
            alert.showAndWait();
        }
    }
}
