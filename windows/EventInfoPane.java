package windows;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import objects.Event;

public class EventInfoPane extends VBox {
    private Event event;
    private Integer eventID;
    private String eventName;
    private String eventDesc;
    private java.sql.Timestamp eventStart;
    private java.sql.Timestamp eventEnd;
    private String eventLocation;
    private String clubName;
    private Integer clubID;

    public EventInfoPane(Event event) {
        this.event = event;

        this.setAlignment(Pos.CENTER);

        extractData();

        Stage stage = new Stage();
        stage.setTitle(clubName + " Event");

        this.getChildren().addAll(new Label(eventName), new Label(), new Label(eventDesc), new Label(eventLocation), new Label(eventStart.toString() + " - " + eventEnd.toString()));

        // display
        stage.setScene(new Scene(this, 325, 125));
        stage.show();
    }

    // extract params from event object
    private void extractData() {
        eventName = event.getEventName();
        eventDesc = event.getEventDesc();
        eventLocation = event.getEventLocation();
        eventStart = event.getEventStart();
        eventEnd = event.getEventEnd();
        clubName = event.getClubName();
        clubID = event.getClubID();
        eventID = event.getEventID();
    }
}
