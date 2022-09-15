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
 * ClientHandler.java
 *
 * <p>This class handles communication between the client
 * and the server.  It runs in a separate thread but has a
 * link to a common list of sockets to handle broadcast.
 *
 */
public class ClientHandler implements Runnable {
  private Client client;
  private ArrayList<Client> clientList;

  ClientHandler(Client c, ArrayList<Client> clientList) {
    this.client = c;
    this.clientList = clientList;  // Keep reference to master list
  }

  /**
   * received input from a client.
   * sends it to other clients.
   */
  public void run() {
    try {
      System.out.println("Connection made with socket " + client.connectionSock);
      BufferedReader clientInput = new BufferedReader(
          new InputStreamReader(client.connectionSock.getInputStream()));
      boolean firstMessage = true;
      boolean goodbyeMessage = false;
      boolean invalidUsername = false;
      boolean whoMessage = false;
      boolean scoresMessage = false;
      boolean winnerMessage = false;
      String winnerUsername = "";

      while (true) {
        // Get data sent from a client
        String clientText = clientInput.readLine();
        if (clientText != null) {
          if (clientText.equals("Who?")) {
            whoMessage = true;
          } else {
            whoMessage = false;
          }
          // set scoreMessage
          if (clientText.equals("SCORES") && client.username.equals("host")) {
            scoresMessage = true;
          } else {
            scoresMessage = false;
          }
          // add point to the winner
          if (clientText.contains("WINNER") && client.username.equals("host")) {
            String [] msgWords = clientText.trim().split("\\s+");
            if (msgWords[0].equals("WINNER")) {
              winnerMessage = true;
              winnerUsername = msgWords[1];
            } else {
              winnerMessage = false;
            }
          } else {
            winnerMessage = false;
          }
          // first message is the username and add to the client list if not used
          if (firstMessage) {
            invalidUsername = false;
            for (Client s : clientList) {
              if (clientText.toLowerCase().equals(s.username.toLowerCase())) {
                invalidUsername = true;
                break;
              }
            }
            // if username is used ask client to try different username
            DataOutputStream clientOutput;
            clientOutput = new DataOutputStream(client.connectionSock.getOutputStream());
            if (invalidUsername) {
              clientOutput.writeBytes(clientText + " is used, please try other usernames\n");
            } else {
              clientOutput.writeBytes(clientText + " is accepted\n");
              client.username = clientText;
              System.out.println(client.username + " has connected");
            }
          }

          // if clientText is Goodbye then set goodbyeMessage to true
          if (clientText.equals("QUIT")) {
            goodbyeMessage = true;
          } else if (!firstMessage && !goodbyeMessage && !whoMessage && !scoresMessage
                     && !winnerMessage) {
            System.out.println("FROM " + client.username + ": " + clientText);
          }
          if (whoMessage) {
            // for (int i = 0; i < clientList.size(); i++) {
            // System.out.println(clientList.get(i) + "\n");
            // }
            DataOutputStream clientOutput;
            clientOutput = new DataOutputStream(client.connectionSock.getOutputStream());
            String clientNames = "";
            for (Client s : clientList) {
              if (s.connectionSock != client.connectionSock) {
                clientNames = clientNames + " " + s.username;
              }
            }
            clientOutput.writeBytes(clientNames + "\n");
          } else if (scoresMessage) {
            String clientScores = "";
            // ***** CODE TO BE ADDED TO SEND SCORES TO ALL CLIENTS *****
            for (Client s : clientList) {
              if (s.connectionSock != client.connectionSock) {
                clientScores = clientScores + s.username + "'s Score: " + s.score + "\n";
              }
            }
            for (Client s : clientList) {
              if (s.connectionSock != client.connectionSock) {
                DataOutputStream clientOutput;
                clientOutput = new DataOutputStream(s.connectionSock.getOutputStream());
                clientOutput.writeBytes(clientScores);
              }
            }

          } else if (winnerMessage) {
            String [] msgWords = clientText.trim().split("\\s+");
            for (Client s : clientList) {
              if (s.connectionSock != client.connectionSock) {
                if (s.username.toLowerCase().equals(winnerUsername.toLowerCase())) {
                  s.score += 1;
                  DataOutputStream clientOutput;
                  clientOutput = new DataOutputStream(s.connectionSock.getOutputStream());
                  clientOutput.writeBytes("Your score is: " + s.score + "\n");
                  break;
                }
              }
            }
          } else {
            // Turn around and output this data
            // to all other clients except the one
            // that sent us this information
            for (Client s : clientList) {
              if (s.connectionSock != client.connectionSock) {
                DataOutputStream clientOutput;
                clientOutput = new DataOutputStream(s.connectionSock.getOutputStream());
                // if first message and valid username prints username has joined the chat
                // if goodbye message prints username has left the chat and remove from clientList
                if (firstMessage) {
                  if (!invalidUsername) {
                    clientOutput.writeBytes(client.username + " has joined the chat\n");
                  }
                } else if (goodbyeMessage) {
                  clientOutput.writeBytes(client.username + " has left the chat\n");
                  //clientList.remove(client);
                  //client.connectionSock.close();
                  //System.exit(0);
                } else if (!goodbyeMessage) {
                  clientOutput.writeBytes("FROM " + client.username + ": " + clientText + "\n");
                }
              }
            }
            if (!invalidUsername) {
              firstMessage = false;
            }
            if (goodbyeMessage) {
              clientList.remove(client);
            }
          }
        } else {
          // Connection was lost
          System.out.println("Closing connection for socket " + client.connectionSock);
          // Remove from arraylist
          clientList.remove(client);
          client.connectionSock.close();
          break;
        }
      }
    } catch (Exception e) {
      System.out.println("Error: " + e.toString());
      // Remove from arraylist
      clientList.remove(client.connectionSock);
    }
  }
} // ClientHandler for MtServer.java
