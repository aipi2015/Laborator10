package ro.pub.cs.aipi.lab10.graphicuserinterface;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.GregorianCalendar;
import java.util.HashMap;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.jms.JMSException;

import ro.pub.cs.aipi.lab10.communicator.PublishSubscribe;
import ro.pub.cs.aipi.lab10.general.Constants;

public class ContactsList {

    private Stage applicationStage;
    private Scene applicationScene;

    private ContactsList contactsList;

    private PublishSubscribe communicator;

    private String currentUserName;
    private HashMap<String, MessageExchange> currentMessagingWindows;
    private TreeItem<String> currentContactsListItem, connectedContactsListItem, recentContactsListItem;
    private About aboutWindow;

    @FXML
    private MenuBar mainMenuBar;
    @FXML
    private Label currentUserNameLabel;
    @FXML
    private TreeView<String> currentContactsListTreeView;
    @FXML
    private Button communicateButton, closeButton;

    public ContactsList(String userName) {
        contactsList = this;
        this.currentUserName = userName;
        currentMessagingWindows = new HashMap<String, MessageExchange>();
    }

    @SuppressWarnings("unchecked")
    public void start() {
        communicator = new PublishSubscribe(currentUserName, this);
        applicationStage = new Stage();
        try {
            applicationScene = new Scene((Parent) FXMLLoader.load(getClass().getResource(Constants.CONTACTS_LIST_FXML_FILE)));
        } catch (Exception exception) {
            System.out.println("An exception has occurred: " + exception.getMessage());
            if (Constants.DEBUG) {
                exception.printStackTrace();
            }
        }
        applicationStage.setTitle(Constants.APPLICATION_TITLE);
        applicationStage.getIcons().add(new Image(getClass().getResource(Constants.ICON_FILE_NAME).toExternalForm()));
        currentUserNameLabel = (Label) applicationScene.lookup("#currentUserNameLabel");
        currentUserNameLabel.setText(currentUserName);
        ScrollPane currentContactsListScrollPane = (ScrollPane) applicationScene.lookup("#currentContactsListScrollPane");
        currentContactsListTreeView = (TreeView<String>) currentContactsListScrollPane.getContent();
        currentContactsListTreeView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        Node currentContactListIcon = new ImageView(new Image(getClass().getResource(Constants.CONTACTS_LIST_ICON).toExternalForm()));
        currentContactsListItem = new TreeItem<String>(Constants.CONTACTS_LIST, currentContactListIcon);
        currentContactsListItem.setExpanded(true);
        Node connectedContactListIcon = new ImageView(new Image(getClass().getResource(Constants.CONNECTED_CONTACTS_LIST_ICON).toExternalForm()));
        connectedContactsListItem = new TreeItem<String>(Constants.CONNECTED_CONTACTS_LIST, connectedContactListIcon);
        currentContactsListItem.getChildren().add(connectedContactsListItem);
        Node recentContactListIcon = new ImageView(new Image(getClass().getResource(Constants.RECENT_CONTACTS_LIST_ICON).toExternalForm()));
        recentContactsListItem = new TreeItem<String>(Constants.RECENT_CONTACTS_LIST, recentContactListIcon);
        currentContactsListItem.getChildren().add(recentContactsListItem);
        readFromFile();
        currentContactsListTreeView.setRoot(currentContactsListItem);

        // TODO (exercise 7a): send login messages to known users
        ObservableList<TreeItem<String>> recentContactsList = recentContactsListItem.getChildren();
        for (TreeItem<String> recentContact : recentContactsList) {
            String contactUserName = recentContact.getValue();
            try {
                communicator.publish(contactUserName, Constants.LOGIN_MESSAGE);
            } catch (JMSException | IOException exception) {
                System.out.println("An exception has occurred: " + exception.getMessage());
                if (Constants.DEBUG) {
                    exception.printStackTrace();
                }
            }
        }

        mainMenuBar = (MenuBar) applicationScene.lookup("#mainMenuBar");
        ObservableList<Menu> mainMenuBarList = mainMenuBar.getMenus();
        Menu operationsMenu = mainMenuBarList.get(Constants.OPERATIONS_MENU_INDEX);
        Menu helpMenu = mainMenuBarList.get(Constants.HELP_MENU_INDEX);
        ObservableList<MenuItem> operationsMenuList = operationsMenu.getItems();
        MenuItem communicateMenuItem = operationsMenuList.get(Constants.COMMUNICATE_MENU_INDEX);
        EventHandler<ActionEvent> communicateEventHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                communicate();
            }
        };
        communicateMenuItem.setOnAction(communicateEventHandler);
        EventHandler<ActionEvent> closeEventHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                close();
            }
        };
        MenuItem closeMenuItem = operationsMenuList.get(Constants.CLOSE_MENU_INDEX);
        closeMenuItem.setOnAction(closeEventHandler);
        ObservableList<MenuItem> aboutMenuList = helpMenu.getItems();
        MenuItem aboutMenuItem = aboutMenuList.get(Constants.ABOUT_MENU_INDEX);
        aboutMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                about();
            }
        });
        communicateButton = (Button) applicationScene.lookup("#communicateButton");
        communicateButton.setOnAction(communicateEventHandler);
        closeButton = (Button) applicationScene.lookup("#closeButton");
        closeButton.setOnAction(closeEventHandler);
        applicationStage.setScene(applicationScene);
        applicationStage.show();
    }

    public void show() {
        applicationStage.show();
    }

    public void hide() {
        applicationStage.hide();
    }

    public void handleCurrentMessagingWindows(String interlocutorUserName,
            MessageExchange messageExchange) {
        if (!currentMessagingWindows.containsKey(interlocutorUserName)) {
            currentMessagingWindows.put(interlocutorUserName, messageExchange);
        }
        ObservableList<TreeItem<String>> recentContactsList = recentContactsListItem.getChildren();
        boolean found = false;
        if (recentContactsList != null) {
            for (TreeItem<String> recentContact : recentContactsList) {
                String contactUserName = recentContact.getValue();
                if (contactUserName.equals(interlocutorUserName)) {
                    found = true;
                }
            }
        }
        if (!found) {
            recentContactsListItem.getChildren().add(new TreeItem<String>(interlocutorUserName));
            writeToFile();
        }
    }

    // TODO (exercise 9): include the date and time of the message
    // as an argument of the method
    public void handleMessage(String interlocutorUserName, String messageContent, GregorianCalendar messageDateTime) {
        MessageExchange messageExchange;

        // TODO (exercise 7b): analyze login messages
        // and update connectedContactsListItem
        if (messageContent != null && messageContent.equals(Constants.LOGIN_MESSAGE)) {
            ObservableList<TreeItem<String>> connectedContactsList = connectedContactsListItem.getChildren();
            boolean found = false;
            if (connectedContactsList != null) {
                for (TreeItem<String> connectedContact : connectedContactsList) {
                    String contactUserName = connectedContact.getValue();
                    if (contactUserName.equals(interlocutorUserName)) {
                        found = true;
                    }
                }
            }
            if (!found) {
                connectedContactsListItem.getChildren().add(new TreeItem<String>(interlocutorUserName));
            }
            return;
        }

        // TODO (exercise 7d): analyze logout messages
        // and update connectedContactsListItem
        if (messageContent != null && messageContent.equals(Constants.LOGOUT_MESSAGE)) {
            ObservableList<TreeItem<String>> connectedContactsList = connectedContactsListItem.getChildren();
            int position = -1;
            if (connectedContactsList != null) {
                int currentPosition = 0;
                for (TreeItem<String> connectedContact : connectedContactsList) {
                    String contactUserName = connectedContact.getValue();
                    if (contactUserName.equals(interlocutorUserName)) {
                        position = currentPosition;
                    }
                    currentPosition++;
                }
            }
            if (position != -1) {
                connectedContactsListItem.getChildren().remove(position);
            }
            return;
        }

        if (interlocutorUserName == null) {
            messageExchange = new MessageExchange(currentUserName, communicator, contactsList);
            messageExchange.start();
        } else if (!currentMessagingWindows.containsKey(interlocutorUserName)) {
            messageExchange = new MessageExchange(currentUserName, interlocutorUserName, communicator, contactsList);
            messageExchange.start();
            handleCurrentMessagingWindows(interlocutorUserName, messageExchange);
        } else {
            messageExchange = currentMessagingWindows.get(interlocutorUserName);
            messageExchange.show();
        }
        if (messageContent != null) {
            messageExchange.handleConversationHistory(messageContent, messageDateTime);
        }
    }

    private void communicate() {
        TreeItem<String> currentSelectionTreeItem = currentContactsListTreeView.getSelectionModel().getSelectedItem();
        String currentSelection = null;
        if (currentSelectionTreeItem != null) {
            currentSelection = currentSelectionTreeItem.getValue();
        }
        if (currentSelection != null && isUserName(currentSelection)) {
            handleMessage(currentSelection, null, null);
        } else {
            handleMessage(null, null, null);
        }
    }

    private void close() {

        // TODO (exercise 7c): send logout messages to known users
        ObservableList<TreeItem<String>> recentContactsList = recentContactsListItem.getChildren();
        for (TreeItem<String> recentContact : recentContactsList) {
            String contactUserName = recentContact.getValue();
            try {
                communicator.publish(contactUserName, Constants.LOGOUT_MESSAGE);
            } catch (JMSException | IOException exception) {
                System.out.println("An exception has occurred: " + exception.getMessage());
                if (Constants.DEBUG) {
                    exception.printStackTrace();
                }
            }
        }
        communicator.close();
        Platform.exit();
    }

    private void about() {
        if (aboutWindow == null) {
            aboutWindow = new About();
            aboutWindow.start();
        } else {
            aboutWindow.show();
        }
    }

    private void readFromFile() {
        createFile();
        try (RandomAccessFile file = new RandomAccessFile(Constants.TEMPORARY_DIRECTORY + currentUserName, "rw")) {
            String contactUserName;
            while ((contactUserName = file.readLine()) != null) {
                recentContactsListItem.getChildren().add(new TreeItem<String>(contactUserName));
            }
            file.close();
        } catch (IOException exception) {
            System.out.println("An exception has occurred: " + exception.getMessage());
            if (Constants.DEBUG) {
                exception.printStackTrace();
            }
        }
    }

    private void writeToFile() {
        deleteFile();
        try (RandomAccessFile file = new RandomAccessFile(Constants.TEMPORARY_DIRECTORY + currentUserName, "rw")) {
            ObservableList<TreeItem<String>> recentContactsList = recentContactsListItem.getChildren();
            for (TreeItem<String> recentContact : recentContactsList) {
                String contactUserName = recentContact.getValue();
                file.writeBytes(contactUserName + Constants.NEW_LINE);
            }
            file.close();
        } catch (IOException exception) {
            System.out.println("An exception has occurred: " + exception.getMessage());
            if (Constants.DEBUG) {
                exception.printStackTrace();
            }
        }
    }

    private void createFile() {
        File file = new File(Constants.TEMPORARY_DIRECTORY + currentUserName);
        if (file == null || !file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException exception) {
                System.out.println("An exception has occurred: " + exception.getMessage());
                if (Constants.DEBUG) {
                    exception.printStackTrace();
                }
            }
        }
    }

    private void deleteFile() {
        File file = new File(Constants.TEMPORARY_DIRECTORY + currentUserName);
        if (file != null && file.exists()) {
            file.delete();
        }
    }

    private boolean isUserName(String currentSelection) {
        if (currentSelection.equals(currentContactsListTreeView.getRoot().getValue())) {
            return false;
        }
        for (TreeItem<String> currentContactsListTreeItem : currentContactsListTreeView.getRoot().getChildren()) {
            if (currentSelection.equals(currentContactsListTreeItem.getValue())) {
                return false;
            }
        }
        return true;
    }
}
