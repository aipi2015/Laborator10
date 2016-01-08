package ro.pub.cs.aipi.lab10.graphicuserinterface;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;
import ro.pub.cs.aipi.lab10.general.Constants;

public class Authentication {

    private Stage applicationStage;
    private Scene applicationScene;

    @FXML
    private TextField userNameTextField;
    @FXML
    private Button okButton, cancelButton;
    @FXML
    private Label errorLabel;

    public void start() {
        applicationStage = new Stage();
        try {
            applicationScene = new Scene((Parent) FXMLLoader.load(getClass().getResource(Constants.AUTHENTICATION_FXML_FILE)));
        } catch (Exception exception) {
            System.out.println("An exception has occurred: " + exception.getMessage());
            if (Constants.DEBUG) {
                exception.printStackTrace();
            }
        }
        applicationStage.setTitle(Constants.APPLICATION_TITLE);
        applicationStage.getIcons().add(new Image(getClass().getResource(Constants.ICON_FILE_NAME).toExternalForm()));
        okButton = (Button) applicationScene.lookup("#okButton");
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                userNameTextField = (TextField) applicationScene.lookup("#userNameTextField");
                String userName = userNameTextField.getText();
                if (userName.isEmpty()) {
                    errorLabel = (Label) applicationScene.lookup("#errorLabel");
                    errorLabel.setText(Constants.NO_USERNAME_ERROR);
                    FadeTransition fadeTransition = new FadeTransition(Duration.millis(Constants.MESSAGE_ERROR_TIMEOUT), errorLabel);
                    fadeTransition.setFromValue(1.0);
                    fadeTransition.setToValue(0.0);
                    fadeTransition.setAutoReverse(false);
                    fadeTransition.play();
                } else {
                    applicationStage.hide();
                    new ContactsList(userName).start();
                }
            }
        });
        cancelButton = (Button) applicationScene.lookup("#cancelButton");
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Platform.exit();
            }
        });
        applicationStage.setScene(applicationScene);
        applicationStage.show();
    }

    public void show() {
        applicationStage.show();
    }

    public void hide() {
        applicationStage.hide();
    }

}
