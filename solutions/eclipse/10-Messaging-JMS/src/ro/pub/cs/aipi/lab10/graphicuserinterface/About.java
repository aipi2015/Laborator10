package ro.pub.cs.aipi.lab10.graphicuserinterface;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ro.pub.cs.aipi.lab10.general.Constants;

public class About {
	
	private Stage applicationStage;
	private Scene applicationScene;

	@FXML
	private Label aboutLabel;
	@FXML
	private Button okButton;

	public void start() {
		applicationStage = new Stage();
		try {
			applicationScene = new Scene((Parent) FXMLLoader.load(getClass().getResource(Constants.ABOUT_FXML_FILE)));
		} catch (Exception exception) {
			System.out.println("An exception has occurred: " + exception.getMessage());
			if (Constants.DEBUG) {
				exception.printStackTrace();
			}
		}
		applicationStage.setTitle(Constants.APPLICATION_TITLE);
		applicationStage.getIcons().add(new Image(getClass().getResource(Constants.ICON_FILE_NAME).toExternalForm()));
		aboutLabel = (Label)applicationScene.lookup("#aboutLabel");
		aboutLabel.setText(Constants.ABOUT_TEXT);
		okButton = (Button)applicationScene.lookup("#okButton");
		okButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				applicationStage.hide();
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