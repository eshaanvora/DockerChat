//DOCKERCHAT
//Eshaan Vora
//EshaanVora@gmail.com

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

/**
 * Client.java
 *
 * <p> This class is the contains the client socket
 * and the username to be added to the clientList
 * and the score to be kept track of
 */

public class Client {

  public Socket connectionSock = null;
  public String username = "";
  public int score = 0;

  Client(Socket sock,  String username) {
    this.connectionSock = sock;
    this.username = username;
    this.score = 0;
  }
}
