//DOCKERCHAT
//Eshaan Vora
//EshaanVora@gmail.com

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * MTServer.java
 *
 * <p>This program implements a simple multithreaded chat server.  Every client that
 * connects to the server can broadcast data to all other clients.
 * The server stores an ArrayList of clients to perform the broadcast.
 *
 * <p>The MTServer uses a ClientHandler whose code is in a separate file.
 * When a client connects, the MTServer starts a ClientHandler in a separate thread
 * to receive messages from the client.
 *
 * <p>To test, start the server first, then start multiple clients and type messages
 * in the client windows.
 *
 */

public class MtServer {
  // Maintain list of all client sockets for broadcast
  private ArrayList<Client> clientList;

  public MtServer() {
    clientList = new ArrayList<Client>();
  }

  private void getConnection() {
    // Wait for a connection from the client
    try {
      System.out.println("Waiting for client connections on port 9010.");
      ServerSocket serverSock = new ServerSocket(9010);
      // This is an infinite loop, the user will have to shut it down
      // using control-c
      while (true) {
        Socket connectionSock = serverSock.accept();
        Client client = new Client(connectionSock, "");
        this.clientList.add(client);
        // Send to ClientHandler the socket and arraylist of all clients
        ClientHandler handler = new ClientHandler(client, this.clientList);
        Thread theThread = new Thread(handler);
        theThread.start();
      }
      // Will never get here, but if the above loop is given
      // an exit condition then we'll go ahead and close the socket
      //serverSock.close();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }

  public static void main(String[] args) {
    MtServer server = new MtServer();
    server.getConnection();
  }
}
