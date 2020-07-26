package server;

import javafx.util.Pair;
import objects.Club;
import objects.Event;
import objects.Message;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class NewClientThread extends Thread {
    // class variables
    private Socket clientSocket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private Integer identification;
    private Server server;

    // user data
    private String username;
    private String password;

    // class constructor
    public NewClientThread(Socket socket, Integer ID, Server server) {
        // set class variables
        clientSocket = socket;
        identification = ID;
        this.server = server;

        // attempt to create streams
        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("> Failed to get streams");
            e.printStackTrace();
        }
    }

    // run the thread to continuously serve client
    public void run() {
        while(true) {
            try {
                // read from client
                Message message = (Message) objectInputStream.readObject();

                Message newMessage;

                if(message.getMessageType()==0) {
                    // get user info
                    username = message.getUser();
                    password = message.getPassword();

                    // get user data from database
                    Pair<ArrayList<Club>, ArrayList<Event>> pair = DBUtil.getUserData(username, password);

                    // construct message
                    newMessage = new Message("server","NA", pair.getKey(), pair.getValue(), null, null, 0, null);
                } else if(message.getMessageType()==1) {
                    // get user info
                    username = message.getUser();
                    password = message.getPassword();

                    // get club search data from database
                    ArrayList<Club> clubs = DBUtil.getClubSearchResults(username, password, message.getParameter());

                    // construct message
                    newMessage = new Message("server","NA", null, null, clubs, null, 1, null);
                } else if(message.getMessageType()==2) {
                    // get event search data from database
                    ArrayList<Event> events = DBUtil.getEventSearchResults(message.getParameter());

                    // construct message
                    newMessage = new Message("server","NA", null, null, null, events, 2, null);
                } else if(message.getMessageType()==3) {
                    // get user data from database
                    Pair<ArrayList<Club>, ArrayList<Event>> pair = DBUtil.unsubscribeFromClub(username, password, message.getClubs().get(0));

                    // construct message
                    newMessage = new Message("server","NA", pair.getKey(), pair.getValue(), null, null, 0, null);
                } else if(message.getMessageType()==4) {
                    // get user data from database
                    Pair<ArrayList<Club>, ArrayList<Event>> pair = DBUtil.subscribeToClub(username, password, message.getClubs().get(0));

                    newMessage = new Message("server","NA", pair.getKey(), pair.getValue(), null, null, 0, null);
                } else {
                    newMessage = null;
                }

                if(writeToClient(newMessage)) {
                    System.out.println("Message sent to client");
                } else {
                    System.out.println("Did not sent message back to client");
                }

            } catch(SocketException | EOFException ex) {
                System.out.println("> Client " + identification + " has disconnected from server at " + new Date());
                break;
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("> Failed to get input stream");
                break;
            }
        }
    }

    // write back to client
    public boolean writeToClient(Message message) {
        // if client isnt connected, message cant be sent
        if(!clientSocket.isConnected()) {
            return false;
        }

        // if msg is empty
        if(message==null) {
            return false;
        }

        // try to write to client
        try {
            objectOutputStream.writeObject(message);
            return true;
        } catch (SocketException ex) {
            System.out.println("Failed to write to client");
        } catch (IOException e) {
            System.out.println("Failed to write to client");
        }

        return false;
    }

    // try to close streams
    public void closeStreams() {
        try {
            if(objectOutputStream!=null)
                objectOutputStream.close();
            if(objectInputStream!=null)
                objectInputStream.close();
            if(clientSocket!=null)
                clientSocket.close();
        } catch (IOException e) {
            System.out.println("Difficulty shutting down streams");
            e.printStackTrace();
        }
    }
}
