package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;

public class Server {
    // db variables
    private String url = "jdbc:mysql://localhost:3306/clubDB";
    private Connection connection = null;

    // server variables
    private Integer port;
    private ArrayList<NewClientThread> activeClientList;
    private Integer clientCount = 0;

    // constructor initializes several variables
    public Server(Integer port) {
        activeClientList = new ArrayList<>();
        this.port = port;
        startServer();
    }

    // start the server
    public void startServer() {
        // thread to handle requests
        new Thread(() -> {
            // try to create a server socket
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("> Server started at " + new Date());

                // continue serving clients
                while (true) {
                    // listen for connection request
                    Socket socket = serverSocket.accept();

                    // increment total clients connected
                    clientCount++;

                    // create new client thread
                    NewClientThread newClient = new NewClientThread(socket, clientCount, this);

                    // add client to active client list
                    activeClientList.add(newClient);

                    // start thread
                    newClient.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                shutdownServer();
            }
        }).start();
    }

    // shut server down
    private void shutdownServer() {
        Integer size = activeClientList.size();

        // go through all active clients and shut down streams
        for(int i=0;i<size;i++) {
            NewClientThread client = activeClientList.get(i);
            client.closeStreams();
        }

        // empty list
        activeClientList.clear();
    }

    public static void main(String[] args) {
        // chat port 8000
        Server server = new Server(8000);
    }
}
