package objects;

import java.io.Serializable;
import java.util.ArrayList;

public class Club implements Serializable {
    private Integer clubID;
    private String clubName;
    private String description;
    private ArrayList<Event> events;
    private ArrayList<Leader> leaders;

    public Club(Integer clubID, String clubName, String description, ArrayList<Event> events, ArrayList<Leader> leaders) {
        this.clubName = clubName;
        this.description = description;
        this.clubID = clubID;
        this.events = events;
        this.leaders = leaders;
    }

    public Integer getClubID() {
        return clubID;
    }

    public String getClubName() {
        return clubName;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public ArrayList<Leader> getLeaders() {
        return leaders;
    }
}
