# Abalone Multiplayer
A computer multiplayer version of the board game Abalone. The version comes with a server application which supports multiple games played at the same time and a client application which allows users to connect to the server.

# Server side
  - Multiple game modes for 2 players, 3 players and 4 players.
  - Queuing system
  - Multithreading for multiple games played at the same time
  - Textual user interface
  - All games are handled by the server in order to keep the integrity of the games

# Client side
  - Textual user interface
  - Computer player (for 2 players games) implemented using the min-max algorithm with alpha-beta pruning and iterative deepening

Both sides communicate via the protocol defined in the Protocol class.

# Installation instructions 
It can be done in 2 ways. 
  
  ### Using the jars from the Jar folder
   Simply run the Server.jar application in a terminal via the command `java Server.jar` and follow the instructions on the screen to set up the server. In a separate terminal/s run the Client.jar application using the command `java Client.jar` and follow the instructions on the screen in order to connect to the server.
     
  ### Running the code in an IDE
   Clone the project and open the `src` folder as a project in an IDE. Then simply run the main function of the `GameServer.class` in the `abalone/Server` folder to set up the server and the main function of the `GameClient.class` from the `abalone/Client` folder to start the client.
  
	
In order to play a game offline, at least 2 instances of the client application must be openned. If both the server and the client applications run on the same machine, the ip can be set to ```localhost```. The port number can be any number allowed as a port number (for example 3333) as long as it is not used for something else. If the port number is used for something else, odd behaviour might be encountered. 
     
  
