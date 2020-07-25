package windows;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import objects.Club;
import objects.Event;
import objects.Leader;

public class ClubInfoPane extends BorderPane {
    // table variables
    private TableView eBoardTableView;
    private TableColumn nameColumn;
    private TableColumn positionColumn;
    private TableColumn emailColumn;

    // observables
    private ObservableList<ObservableList> tableData;

    private Club club;

    public ClubInfoPane(Club club) {
        this.club = club;

        Stage stage = new Stage();
        stage.setTitle(club.getClubName());

        Label clubName = new Label(club.getClubName());
        Text clubDescription = new Text(club.getDescription());

        Label eBoardLabel = new Label("E-Board");
        Label eventsLabel = new Label("Club Events");

        // set column names for table
        eBoardTableView = new TableView();
        tableData = FXCollections.observableArrayList();
        nameColumn = new TableColumn("Full Name");
        positionColumn = new TableColumn("Position");
        emailColumn = new TableColumn("Email");

        // set up leader table
        setTableCellFactory();
        addData();

        // create list view of all club's event objects
        ListView<Event> eventListView = new ListView<>(FXCollections.observableArrayList(club.getEvents()));

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

        // add elements to vbox
        VBox left = new VBox();
        left.setAlignment(Pos.CENTER);
        VBox right = new VBox();
        right.setAlignment(Pos.CENTER);
        left.getChildren().addAll(clubDescription, new Label(), eBoardLabel, eBoardTableView);
        right.getChildren().addAll(eventsLabel, eventListView);

        // add to border pane
        this.setLeft(left);
        this.setRight(right);

        // set insets
        BorderPane.setMargin(left, new Insets(10));
        BorderPane.setMargin(right, new Insets(10));

        // display
        stage.setScene(new Scene(this, 550, 250));
        stage.show();
    }

    // add data to the table
    private void addData() {
        ObservableList<Leader> leaders = FXCollections.observableArrayList(club.getLeaders());

        // add data to row
        for(int i=0;i<leaders.size();i++) {
            ObservableList<String> row = FXCollections.observableArrayList();
            row.addAll(leaders.get(i).getLeaderName(), leaders.get(i).getLeaderRole(), leaders.get(i).getLeaderEmail());
            tableData.add(row);
        }

        // add data to table
        eBoardTableView.setItems(tableData);
    }

    // set table to pull data from certain position of input
    private void setTableCellFactory() {
        nameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                return new SimpleStringProperty(param.getValue().get(0).toString());
            }
        });

        positionColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                return new SimpleStringProperty(param.getValue().get(1).toString());
            }
        });

        emailColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                return new SimpleStringProperty(param.getValue().get(2).toString());
            }
        });

        eBoardTableView.getColumns().addAll(nameColumn, positionColumn, emailColumn);
    }
}
