package objects;

import java.io.Serializable;

public class Event implements Serializable {
    Integer eventID;
    String eventName;
    String eventDesc;
    java.sql.Timestamp eventStart;
    java.sql.Timestamp eventEnd;
    String eventLocation;
    String clubName;
    Integer clubID;

    public Event(Integer eventID, String eventName, String eventDesc, String eventLocation, java.sql.Timestamp eventStart, java.sql.Timestamp eventEnd, Integer clubID, String clubName) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.eventDesc = eventDesc;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
        this.eventLocation = eventLocation;
        this.clubName = clubName;
        this.clubID = clubID;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public Integer getEventID() {
        return eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public java.sql.Timestamp getEventEnd() {
        return eventEnd;
    }

    public java.sql.Timestamp getEventStart() {
        return eventStart;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public String getClubName() {
        return clubName;
    }

    public Integer getClubID() {
        return clubID;
    }
}
