package client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import objects.Club;
import objects.Event;
import objects.Message;
import windows.ClubInfoPane;
import windows.EventInfoPane;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Client extends Application {
    // server variables
    private Socket socket;
    private String host = "localhost";
    private Integer port = 8000;

    // streams
    private ObjectOutputStream toServer;
    private ObjectInputStream fromServer;

    // login elements
    private TextField usernameField;
    private TextField passwordField;
    private Button loginButton;

    // home screen sidebar labels
    private Button homeButton = new Button("Home");
    private Button clubButton = new Button("Clubs");
    private Button eventButton = new Button("Events");
    private Button logoutButton = new Button("Logout");

    // data
    private ObservableList<Club> clubObservableList;
    private ObservableList<Event> eventObservableList;
    private ObservableList<Club> clubSearchList;
    private ObservableList<Event> eventSearchList;

    // list views
    private ListView<Club> clubListView;
    private ListView<Event> eventListView;

    // screen types
    private enum CurrentWindow {
        HOME,
        CLUB,
        EVENT
    }

    // which screen is user on
    private CurrentWindow currentWindow;
    private Stage currentStage;

    // login window
    @Override
    public void start(Stage stage) {
        currentStage = stage;

        // data declaration
        clubObservableList = FXCollections.observableArrayList();
        eventObservableList = FXCollections.observableArrayList();

        // fields
        usernameField = new TextField("rkiefer7196@floridapoly.edu");
        passwordField = new TextField("abc");

        // buttons
        loginButton = new Button("Login");

        VBox vBox = new VBox(8);
        vBox.getChildren().addAll(new Label("Email"), usernameField, new Label("Password"), passwordField, loginButton);

        currentStage.setScene(new Scene(vBox, 200, 150));
        currentStage.setTitle("Login");
        currentStage.show();

        // if user wishes to connect to server
        loginButton.setOnAction(e -> {
            // if user connects to server
            if(connectToServer()) {
                // request data
                requestUserData();

                // wait for data from server
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

                // go to home screen
                homeScreen();
            } else {
                // error message: failed to connect to server
            }
        });
    }

    // home screen
    private void homeScreen() {
        // set current screen
        currentWindow = CurrentWindow.HOME;

        // button section
        VBox menuOptions = new VBox(10);
        menuOptions.getChildren().addAll(homeButton, clubButton, eventButton);

        // create list view of all user's clubs objects
        clubListView = new ListView<>(clubObservableList);

        // populate list view with club objects
        clubListView.setCellFactory(params -> new ListCell<Club>() {
            @Override
            protected void updateItem(Club club, boolean empty) {
                super.updateItem(club, empty);

                if(empty || club==null || club.getClubName()==null) {
                    setText(null);
                } else {
                    setText(club.getClubName());
                }
            }
        });

        // if user selects a club from list
        clubListView.setOnMouseClicked(e-> {
            ClubInfoPane clubInfoPane = new ClubInfoPane(clubListView.getSelectionModel().getSelectedItem());
        });

        // create list view of all user's clubs objects
        eventListView = new ListView<>(eventObservableList);

        // populate list view with event objects
        eventListView.setCellFactory(params -> new ListCell<Event>() {
            @Override
            protected void updateItem(Event event, boolean empty) {
                super.updateItem(event, empty);

                if(empty || event==null || event.getEventName()==null) {
                    setText(null);
                } else {
                    setText(event.getEventName());
                }
            }
        });

        // if user selects an event from the list
        eventListView.setOnMouseClicked(e-> {
            EventInfoPane eventInfoPane = new EventInfoPane(eventListView.getSelectionModel().getSelectedItem());
        });

        // club section
        VBox clubSummary = new VBox();
        clubSummary.setAlignment(Pos.TOP_CENTER);
        clubSummary.setPrefWidth(200);
        clubSummary.getChildren().addAll(new Label("My Clubs"), clubListView);

        // event section
        VBox eventSummary = new VBox();
        eventSummary.setAlignment(Pos.TOP_CENTER);
        eventSummary.setPrefWidth(200);
        eventSummary.getChildren().addAll(new Label("Upcoming Events"), eventListView);

        // top panel
        HBox hBox = new HBox(5);
        Region fillerA = new Region();
        HBox.setHgrow(fillerA, Priority.ALWAYS);
        Region fillerB = new Region();
        HBox.setHgrow(fillerB, Priority.ALWAYS);
        hBox.getChildren().addAll(new Label(getUsername()), fillerA, logoutButton);

        // add all elements of home screen to pane
        BorderPane borderPane = new BorderPane();
        // borderPane.setTop(hBox);
        borderPane.setLeft(menuOptions);
        borderPane.setCenter(clubSummary);
        borderPane.setRight(eventSummary);

        // set alignments
        Insets insets = new Insets(10);
        BorderPane.setMargin(menuOptions, insets);
        BorderPane.setMargin(clubSummary, insets);
        BorderPane.setMargin(eventSummary, insets);

        // set and display scene
        currentStage.setScene(new Scene(borderPane, 500, 200));
        currentStage.setTitle("Home");
        currentStage.show();

        // go to club search scene
        clubButton.setOnAction(e -> {
            clubSearchScreen();
        });

        // go to event search scene
        eventButton.setOnAction(e -> {
            eventSearchScreen();
        });
    }

    // search for club scene
    private void clubSearchScreen() {
        // set current screen
        currentWindow = CurrentWindow.CLUB;

        // button section
        VBox menuOptions = new VBox(10);
        menuOptions.getChildren().addAll(homeButton, clubButton, eventButton);

        VBox center = new VBox(5);
        VBox right = new VBox(5);

        ComboBox clubTagOption = new ComboBox();
        TextField clubSearchBar = new TextField();
        Button search = new Button("Search");

        // add elements to center pane
        center.getChildren().addAll(new Label("Club Search by Name"), clubSearchBar, new Label("Club Search by Tag"), clubTagOption, search);

        // create list view for search results
        ListView<Club> clubSearchResults = new ListView(clubSearchList);

        // populate list view with event objects
        clubSearchResults.setCellFactory(params -> new ListCell<Club>() {
            @Override
            protected void updateItem(Club club, boolean empty) {
                super.updateItem(club, empty);

                if(empty || club==null || club.getClubName()==null) {
                    setText(null);
                } else {
                    setText(club.getClubName());
                }
            }
        });

        // if user selects an event from the list
        clubSearchResults.setOnMouseClicked(e-> {
            ClubInfoPane clubInfoPane = new ClubInfoPane(clubSearchResults.getSelectionModel().getSelectedItem());
        });

        // add elements to right pane
        right.getChildren().addAll(new Label("Club Search Results"), clubSearchResults);
        right.setAlignment(Pos.TOP_CENTER);

        // add all elements of home screen to pane
        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(menuOptions);
        borderPane.setCenter(center);
        borderPane.setRight(right);

        // set alignments
        Insets insets = new Insets(10);
        BorderPane.setMargin(menuOptions, insets);
        BorderPane.setMargin(center, insets);
        BorderPane.setMargin(right, insets);

        // set and display scene
        currentStage.setScene(new Scene(borderPane, 500, 200));
        currentStage.setTitle("Club Search");
        currentStage.show();

        // go back to home screen
        homeButton.setOnAction(e -> {
            homeScreen();
        });

        // go to event search screen
        eventButton.setOnAction(e -> {
            eventSearchScreen();
        });

        search.setOnAction(e -> {
            requestClubSearchDataByString(clubSearchBar.getText());
        });
    }

    // search for club scene
    private void eventSearchScreen() {
        // set current screen
        currentWindow = CurrentWindow.EVENT;

        // button section
        VBox menuOptions = new VBox(10);
        menuOptions.getChildren().addAll(homeButton, clubButton, eventButton);

        VBox center = new VBox(5);
        VBox right = new VBox(5);

        ComboBox eventTagOption = new ComboBox();
        TextField eventSearchBar = new TextField();
        Button search = new Button("Search");

        // add elements to center pane
        center.getChildren().addAll(new Label("Event Search by Name"), eventSearchBar, new Label("Event Search by Tag"), eventTagOption, search);

        // create list view for search results
        ListView<Event> eventSearchResults = new ListView(eventSearchList);

        // populate list view with event objects
        eventSearchResults.setCellFactory(params -> new ListCell<Event>() {
            @Override
            protected void updateItem(Event event, boolean empty) {
                super.updateItem(event, empty);

                if(empty || event==null || event.getClubName()==null) {
                    setText(null);
                } else {
                    setText(event.getEventName());
                }
            }
        });

        // if user selects an event from the list
        eventSearchResults.setOnMouseClicked(e-> {
            EventInfoPane eventInfoPane = new EventInfoPane(eventSearchResults.getSelectionModel().getSelectedItem());
        });

        // add elements to right pane
        right.getChildren().addAll(new Label("Event Search Results"), eventSearchResults);
        right.setAlignment(Pos.TOP_CENTER);

        // add all elements of home screen to pane
        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(menuOptions);
        borderPane.setCenter(center);
        borderPane.setRight(right);

        // set alignments
        Insets insets = new Insets(10);
        BorderPane.setMargin(menuOptions, insets);
        BorderPane.setMargin(center, insets);
        BorderPane.setMargin(right, insets);

        // set and display scene
        currentStage.setScene(new Scene(borderPane, 500, 200));
        currentStage.setTitle("Event Search");
        currentStage.show();

        // go back to home screen
        homeButton.setOnAction(e -> {
            homeScreen();
        });

        // go to club search screen
        clubButton.setOnAction(e -> {
            clubSearchScreen();
        });

        search.setOnAction(e -> {
            requestEventSearchDataByString(eventSearchBar.getText());
        });
    }

    private void updateScreen() {
        switch(currentWindow) {
            case HOME:
                homeScreen();
                break;
            case CLUB:
                clubSearchScreen();
                break;
            case EVENT:
                eventSearchScreen();
                break;
        }
    }

    private String getUsername() {
        return usernameField.getText();
    }

    private String getPassword() {
        return passwordField.getText();
    }

    // data request
    private void requestUserData() {
        try {
            toServer.writeObject(new Message(getUsername(), getPassword(), null, null, null, null, 0, null));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void requestClubSearchDataByString(String param) {
        try {
            toServer.writeObject(new Message(null, null, null, null, null, null, 1, param));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void requestEventSearchDataByString(String param) {
        try {
            toServer.writeObject(new Message(null, null, null, null, null, null, 2, param));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // connect to server
    private boolean connectToServer() {
        try {
            // create socket
            socket = new Socket(host, port);

            // create I/O streams
            toServer = new ObjectOutputStream(socket.getOutputStream());
            fromServer = new ObjectInputStream(socket.getInputStream());

            // create listener for server data
            serverListener();

            return true;
        } catch (IOException e) {
            System.out.println("Failed to connect to server");
        }
        return false;
    }

    private void serverListener() {
        // create thread to listen for data from server
        new Thread(() -> {
            while (true) {
                try {
                    // get message object from server
                    Message message = (Message) fromServer.readObject();

                    // if user data
                    if(message.getMessageType()==0) {
                        // update client's club data
                        clubObservableList = FXCollections.observableArrayList(message.getClubs());

                        // update client's upcoming events data
                        eventObservableList = FXCollections.observableArrayList(message.getUpcomingEvents());

                        Platform.runLater(() -> {
                            updateScreen();
                        });

                    }
                    // if club search results
                    else if(message.getMessageType()==1) {
                        clubSearchList = FXCollections.observableArrayList(message.getClubSearchList());

                        Platform.runLater(() -> {
                            updateScreen();
                        });
                    }

                    // if event search results
                    else if(message.getMessageType()==2) {
                        eventSearchList = FXCollections.observableArrayList(message.getEventSearchList());

                        Platform.runLater(() -> {
                            updateScreen();
                        });
                    } else {
                        System.out.println("Message type not defined");
                    }

                } catch (IOException | ClassNotFoundException ex) {
                    System.out.println("Could not read data from server");
                }
            }
        }).start();
    }
}
