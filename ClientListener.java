//DOCKERCHAT
//Eshaan Vora
//EshaanVora@gmail.com

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * ClientListener.java
 *
 * <p>This class runs on the client end and just
 * displays any text received from the server.
 *
 */

public class ClientListener implements Runnable {
  private Socket connectionSock = null;

  ClientListener(Socket sock) {
    this.connectionSock = sock;
  }

  /**
   * Gets message from server and dsiplays it to the user.
   */
  public void run() {
    try {
      BufferedReader serverInput = new BufferedReader(
          new InputStreamReader(connectionSock.getInputStream()));
      while (true) {
        // Get data sent from the server
        String serverText = serverInput.readLine();
        if (serverInput != null) {
          System.out.println(serverText);
        } else {
          // Connection was lost
          System.out.println("Closing connection for socket " + connectionSock);
          connectionSock.close();
          break;
        }
      }
    } catch (Exception e) {
      System.out.println("Error: " + e.toString());
    }
  }
} // ClientListener for MtClient
