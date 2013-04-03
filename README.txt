Command Line Project 2 Client/Server Documentation

To compile the program for use with the command line, run the script 'commandLine.sh'
	./commandLine.sh

To run in server mode:
	java SocketMainCopy [port] [rows] [columns] [client player(W/B)] [time limit]

	ex: java SocketMainCopy 1024 5 9 W 5000

	Now follow the on screen instructions to select server/client and human/computer


To run in client mode:
	java SocketMainCopy [host] [port]

	ex: java SocketMainCopy 127.0.0.1 1024

	Follow the instructions…

For human players, the format for a move is the same as the format for move communication between the client and server