# Dockerchat

This repo contains programs to implement a multi-threaded TCP chat server and client game

* MtClient.java handles keyboard input from the user.
* ClientListener.java receives responses from the server and displays them
* MtServer.java listens for client connections and creates a ClientHandler for each new client
* ClientHandler.java receives messages from a client and relays it to the other clients.
* Client.java is the client annd contains socket and username.

A Jenkins job was configured to automatically run linters and unit test this project from my public GitHub repo. The Jenkins server was configured on the icd.chapman.edu server

## Identifying Information

* Name: Eshaan Vora
* Email: EshaanVora@gmail.com

## Program Functionality:

*                Check username and ask user to input another name, if username is taken
*                Check and assign who is the host
*                Manual sending of game questions to all clients
*                Manual adding of scores to the winning user
*                Removes username after receiving Goodbye message from client
*                If host client types "SCORES", each client receives the scores of all clients 
*                If the client types "QUIT" their session and program is gracefully exited 
*                Introductory welcome connection message is sent to other clients when a new client connects
*                If client types "Who?" the program will list all the client usernames who are connected
*                Server must by shut down manually by typing ^C

*                This program passes the CheckStyle linter auditing

## Game Rules
* Host manually sends the game questions
* Clients respond with answers
* Host decides who is the winner
* Host types "WINNER username" and the program will add the score to the winning user
 
## Source Files

* MtClient.java
* ClientListener.java
* MtServer.java
* ClientHandler.java
* Client.java

## Known Errors

* None

## Build Insructions

* Compile: 
1. javac MtServer.java
2. javac MtClient.java

* Run:
1. java MtServer
2. java MtClient

* client: 
  docker image build -t client .  
* server:
  docker image build -t server .  

## Execution Instructions

* client: "red" : below is the container name that should be different for every client
  docker container run --rm -it --name red client
* server:
  docker container run --rm -it --name server -p 7654:7654 server
  
