package objects;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {
    String user;
    String password;
    ArrayList<Club> myClubs;
    ArrayList<Event> myUpcomingEvents;
    ArrayList<Club> clubSearchList;
    ArrayList<Event> eventSearchList;
    User userInfo;
    Integer messageType;
    String parameter;

    /*
    Message types
    0 - user data request
    1 - club search request
    2 - event search request
    3 - unsubscribe from club request
    4 - subscribe to club request
     */

    public Message(String user, String password, ArrayList<Club> myClubs, ArrayList<Event> myUpcomingEvents, ArrayList<Club> clubSearchList, ArrayList<Event> eventSearchList, Integer messageType, String parameter) {
        this.user = user;
        this.password = password;
        this.myClubs = myClubs;
        this.myUpcomingEvents = myUpcomingEvents;
        // this.userInfo = userInfo;
        this.messageType = messageType;
        this.parameter = parameter;
        this.eventSearchList = eventSearchList;
        this.clubSearchList = clubSearchList;
    }

    public ArrayList<Club> getClubs() {
        return myClubs;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<Event> getUpcomingEvents() {
        return myUpcomingEvents;
    }

    /*
    public User getUserInfo() {
        return userInfo;
    }
     */

    public Integer getMessageType() {
        return messageType;
    }

    public String getParameter() {
        return parameter;
    }

    public ArrayList<Club> getClubSearchList() {
        return clubSearchList;
    }

    public ArrayList<Event> getEventSearchList() {
        return eventSearchList;
    }
}
