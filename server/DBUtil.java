package server;

import javafx.util.Pair;
import objects.Club;
import objects.Event;
import objects.Leader;
import objects.User;

import java.sql.*;
import java.util.ArrayList;

public class DBUtil {
    private static String db = "clubDB";
    private static String server = "jdbc:mysql://localhost:3306" + "/" + db;
    private static Connection connection = null;
    private static String dbUser = "root";
    private static String dbPassword = "";

    // connect database
    protected static void ConnectDB() {
        try {
            // set up sql driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // pass in credentials
            connection = DriverManager.getConnection(server,dbUser,dbPassword);
        } catch (ClassNotFoundException e) {
            System.out.println("No Driver Detected");
        } catch (SQLException e) {
            System.out.println("Invalid database credentials");
        }

        System.out.println("Database Connected");
    }

    // disconnect database
    protected static void DisconnectDB() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Cannot disconnect from database");
        }
    }

    protected static Pair<ArrayList<Club>, ArrayList<Event>> getUserData(String username, String password) {
        // create connection
        ConnectDB();

        // get user's clubs
        ArrayList<Club> clubList = getClubInfo(username, password);

        // get user's upcoming events
        ArrayList<Event> eventList = getUpcomingEvents(username, password);

        // get user data
        // User user = getUserInfo(username, password);

        // disconnect from db
        DisconnectDB();

        return new Pair<ArrayList<Club>, ArrayList<Event>>(clubList, eventList);
    }

    protected static Pair<ArrayList<Club>, ArrayList<Event>> unsubscribeFromClub(String username, String password, Club club) {
        // create connection
        ConnectDB();

        Statement statement = null;

        try {
            statement = connection.createStatement();

            String query = "delete from USER_CLUB where userID=ANY(select userID from USERS where userEmail='" + username + "' and password='" + password + "') and clubID=" + club.getClubID();

            statement.executeUpdate(query);

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Query error: failed to unsubscribe");
        }

        // get user's clubs
        ArrayList<Club> clubList = getClubInfo(username, password);

        // get user's upcoming events
        ArrayList<Event> eventList = getUpcomingEvents(username, password);

        // disconnect from db
        DisconnectDB();

        return new Pair<ArrayList<Club>, ArrayList<Event>>(clubList, eventList);
    }

    protected static Pair<ArrayList<Club>, ArrayList<Event>> subscribeToClub(String username, String password, Club club) {
        // create connection
        ConnectDB();

        Statement statement = null;

        try {
            statement = connection.createStatement();

            String query = "insert into USER_CLUB (userID, clubID, role) values ((select userID from USERS where userEmail='" + username + "' and password='" + password + "')," + club.getClubID() + ",'Member')";

            statement.executeUpdate(query);

        } catch (SQLException e) {
            System.out.println("Query error: failed to subscribe");
        }

        // get user's clubs
        ArrayList<Club> clubList = getClubInfo(username, password);

        // get user's upcoming events
        ArrayList<Event> eventList = getUpcomingEvents(username, password);

        // disconnect from db
        DisconnectDB();

        return new Pair<ArrayList<Club>, ArrayList<Event>>(clubList, eventList);
    }

    protected static ArrayList<Club> getClubSearchResults(String username, String password, String param) {
        // create connection
        ConnectDB();

        // return list
        ArrayList<Club> clubs = new ArrayList<>();

        Statement statement = null;

        try {
            statement = connection.createStatement();

            // select c.clubID, c.clubName, c.clubDesc from CLUB c where c.clubName LIKE '%club%' and clubID NOT IN(select clubID from USER_CLUB where userID=ANY(select userID from USERS where userEmail='" + username + "' and password='" + password + "'));
            String query = "select c.clubID, c.clubName, c.clubDesc from CLUB c where c.clubName LIKE '%club%' and clubID NOT IN(select clubID from USER_CLUB where userID=ANY(select userID from USERS where userEmail='" + username + "' and password='" + password + "'))";

            ResultSet resultSet = statement.executeQuery(query);

            // go through results
            while (resultSet.next() == true) {
                // get club details
                String name = resultSet.getString("c.clubName");
                Integer ID = resultSet.getInt("c.clubID");
                String desc = resultSet.getString("c.clubDesc");

                // get club events
                ArrayList<Event> events = getEventInfo(ID);

                // get club leaders
                ArrayList<Leader> leaders = getLeaderInfo(ID);

                // add to list
                clubs.add(new Club(ID, name, desc, events, leaders));
            }
        } catch (SQLException e) {
            System.out.println("Query error: club info");
        }

        // disconnect from db
        DisconnectDB();

        return clubs;
    }

    protected static ArrayList<Event> getEventSearchResults(String param) {
        // create connection
        ConnectDB();

        // return list
        ArrayList<Event> events = new ArrayList<>();

        Statement statement = null;

        try {
            statement = connection.createStatement();

            String query = "select e.eventID, e.eventName, e.eventDesc, e.eventLocation, e.eventStartDate, e.eventEndDate, c.clubName, c.clubID " +
                    "from CLUB c, EVENT e " +
                    "where c.clubID=e.clubID and e.eventName LIKE '%" + param + "%'";

            ResultSet resultSet = statement.executeQuery(query);

            // go through results
            while (resultSet.next() == true) {
                // get event details
                String name = resultSet.getString("e.eventName");
                Integer ID = resultSet.getInt("e.eventID");
                String description = resultSet.getString("e.eventDesc");
                String location = resultSet.getString("e.eventLocation");
                java.sql.Timestamp start = resultSet.getTimestamp("e.eventStartDate");
                java.sql.Timestamp end = resultSet.getTimestamp("e.eventEndDate");
                String clubName = resultSet.getString("c.clubName");
                Integer clubID = resultSet.getInt("c.clubID");

                events.add(new Event(ID, name, description, location, start, end, clubID, clubName));
            }
        } catch (SQLException e) {
            System.out.println("Query error: club event info");
            e.printStackTrace();
        }

        // disconnect from db
        DisconnectDB();

        return events;
    }

    // get user info
    private static User getUserInfo(String username, String password) {
        // return list
        User userInfo = null;

        Statement statement = null;

        try {
            statement = connection.createStatement();

            String query = "select u.userFirstName, u.userLastName, u.userEmail, u.universityID, uni.universityName " +
                            "from USERS u, UNIVERSITY uni " +
                            "where u.universityID=uni.universityID";

            ResultSet resultSet = statement.executeQuery(query);

            // go through results
            while (resultSet.next() == true) {
                // get event details
                String first = resultSet.getString("u.userFirstName");
                String last = resultSet.getString("u.userFirstName");
                String email = resultSet.getString("u.userEmail");
                Integer universityID = resultSet.getInt("u.universityID");
                String universityName = resultSet.getString("uni.universityID");

                userInfo  = new User(first, last, email, universityName, universityID);
            }
        } catch (SQLException e) {
            System.out.println("Query error: user info");
        }

        return userInfo;
    }

    // get 10 upcoming events for user
    private static ArrayList<Event> getUpcomingEvents(String username, String password) {
        // return list
        ArrayList<Event> events = new ArrayList<>();

        Statement statement = null;

        try {
            statement = connection.createStatement();

            String query = "select e.eventID, e.eventName, e.eventDesc, e.eventLocation, e.eventStartDate, e.eventEndDate, c.clubName, e.clubID " +
                    "from CLUB c, USER_CLUB uc, USERS u, EVENT e " +
                    "where u.userEmail='" + username + "' and u.password='" + password + "' and u.userID=uc.userID and uc.clubID=c.clubID and e.clubID=c.clubID " +
                    "order by e.eventStartDate " +
                    "limit 10";

            ResultSet resultSet = statement.executeQuery(query);

            // go through results
            while (resultSet.next() == true) {
                // get event details
                String name = resultSet.getString("e.eventName");
                Integer ID = resultSet.getInt("e.eventID");
                String description = resultSet.getString("e.eventDesc");
                String location = resultSet.getString("e.eventLocation");
                java.sql.Timestamp start = resultSet.getTimestamp("e.eventStartDate");
                java.sql.Timestamp end = resultSet.getTimestamp("e.eventEndDate");
                Integer clubID = resultSet.getInt("e.clubID");
                String clubName = resultSet.getString("c.clubName");

                events.add(new Event(ID, name, description, location, start, end, clubID, clubName));
            }
        } catch (SQLException e) {
            System.out.println("Query error: club event info");
        }

        return events;
    }

    protected static ArrayList<Club> getClubInfo(String username, String password) {
        // return list
        ArrayList<Club> clubs = new ArrayList<>();

        Statement statement = null;

        try {
            statement = connection.createStatement();

            String query = "select c.clubID, c.clubName, c.clubDesc from CLUB c, USER_CLUB uc, USERS u where u.userEmail='" + username + "' and u.password='" + password + "' and u.userID=uc.userID and uc.clubID=c.clubID";

            ResultSet resultSet = statement.executeQuery(query);

            // go through results
            while (resultSet.next() == true) {
                // get club details
                String name = resultSet.getString("c.clubName");
                Integer ID = resultSet.getInt("c.clubID");
                String desc = resultSet.getString("c.clubDesc");

                // get club events
                ArrayList<Event> events = getEventInfo(ID);

                // get club leaders
                ArrayList<Leader> leaders = getLeaderInfo(ID);

                // add to list
                clubs.add(new Club(ID, name, desc, events, leaders));
            }
        } catch (SQLException e) {
            System.out.println("Query error: club info");
        }

        return clubs;
    }

    // helper class to get club leader information
    protected static ArrayList<Leader> getLeaderInfo(Integer clubID) {
        // return list
        ArrayList<Leader> leaders = new ArrayList<>();

        Statement statement = null;

        try {
            statement = connection.createStatement();

            String query = "select concat(u.userFirstName, ' ', u.userLastName), uc.role, u.userEmail " +
                            "from USERS u, CLUB c, USER_CLUB uc " +
                            "where c.clubID=uc.clubID and uc.userID=u.userID and c.clubID=" + clubID + " and uc.role NOT IN ('Member', 'Other')";

            ResultSet resultSet = statement.executeQuery(query);

            // go through results
            while (resultSet.next() == true) {
                // get leader details
                String name = resultSet.getString(1);
                String role = resultSet.getString(2);
                String email = resultSet.getString(3);

                leaders.add(new Leader(name, role, email));
            }
        } catch (SQLException e) {
            System.out.println("Query error: club leader info");
            e.printStackTrace();
        }

        return leaders;
    }

    // helper class to get event information for club
    protected static ArrayList<Event> getEventInfo(Integer clubID) {
        // return list
        ArrayList<Event> events = new ArrayList<>();

        Statement statement = null;

        try {
            statement = connection.createStatement();

            String query = "select e.eventID, e.eventName, e.eventDesc, e.eventLocation, e.eventStartDate, e.eventEndDate, c.clubName " +
                            "from CLUB c, EVENT e " +
                            "where e.clubID=c.clubID and c.clubID=" + clubID;

            ResultSet resultSet = statement.executeQuery(query);

            // go through results
            while (resultSet.next() == true) {
                // get event details
                String name = resultSet.getString("e.eventName");
                Integer ID = resultSet.getInt("e.eventID");
                String description = resultSet.getString("e.eventDesc");
                String location = resultSet.getString("e.eventLocation");
                java.sql.Timestamp start = resultSet.getTimestamp("e.eventStartDate");
                java.sql.Timestamp end = resultSet.getTimestamp("e.eventEndDate");
                String clubName = resultSet.getString("c.clubName");

                events.add(new Event(ID, name, description, location, start, end, clubID, clubName));
            }
        } catch (SQLException e) {
            System.out.println("Query error: club event info");
        }

        return events;
    }
}
