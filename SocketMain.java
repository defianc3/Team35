import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class SocketMain{
	
	public static ServerSocket create(){
		
		for(int i = 1024; i < 2048; i++){
			try{
				return new ServerSocket(i);
			}
			catch(IOException e){
				continue;
			}
		}
		return null;
	}
	
	
	public static void main(String args[]){
		
		System.out.print("(1) Server or (2) Client?");
		Scanner firstScan = new Scanner(System.in);
		String response1 = firstScan.nextLine();				//TODO update this to take input from the gameWindow
		System.out.println("(1) Human or (2) Computer?");				//Also ip/host and port# (only applicable to the client)
		String response2 = firstScan.nextLine();
		
		boolean human = false;
		if(response2.equals("1")){
			human = true;
		}
		else if(response2.equals("2")){
			human = false;
		}
		else{
			System.out.println("Error on human/computer selection");
			System.exit(3);
		}
		
		if(response1.equals("1")){		//this is the server side
			serverMode(human);
		}
		else if(response1.equals("2")){
			String host = "";
			int port = 0;
			try{
				host = args[0];						//TODO get hostname / port# from game window instead of command line
				port = Integer.parseInt(args[1]);
			}
			catch(Exception e){
				System.out.println("Command line arguments error");
				System.exit(2);
			}
			
			clientMode(human,host,port);
		}
		else{
			
			System.out.println("Server/client selection error");			//User failed to specify client or server
			
		}
	}
	
	public static String getPlayerInput(){
		System.out.print("Enter a move ");
		String playerInput = "";
		Scanner scan2 = new Scanner(System.in);			//The server is white and a human is playing
		playerInput = scan2.nextLine();					//TODO update this code to work with the gamewindow
		return playerInput;
	}
	
	public static void serverMode(boolean human){
		
		long startTime = 0;
		long endTime = 0;
		
		Fanorona game;
		Piece.Type clientPlayer;
		ServerSocket sock = create();
		System.out.println("Listening on "+sock.getLocalPort());
		
		Socket client = null;
		PrintWriter out = null;
		BufferedReader in = null;
		try{
			client = sock.accept();
			out = new PrintWriter(client.getOutputStream(), true);		//socket related stuff
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		}
		catch(IOException e){
			System.out.println("Accept failed");
			System.exit(1);
		}
		
		out.println("WELCOME");			//server sends the client a welcome
		int _rows = 5;
		int _columns = 9;
		char clientT = 'B';				//hardcoded board characteristics
		int responseTime = 5000;
		out.println("INFO "+_columns+" "+_rows+" "+clientT+" "+responseTime);   //send the info command
		
		game = new Fanorona(_columns,_rows);
		
		Piece.Type serverPlayer;
		if(clientT == 'B'){
			clientPlayer = Piece.Type.BLACK;
			serverPlayer = Piece.Type.WHITE;			//initialize game and determine which side controls which pieces
		}
		else{
			clientPlayer = Piece.Type.WHITE;
			serverPlayer = Piece.Type.BLACK;
		}

		String playerInput = "";
		boolean cont = true;
		
		try {
			while((playerInput = in.readLine()) != null && cont){			//loops until it reads a null or cont == false
																				//cont is set to false when an illegal move occurs
				if(playerInput.equals("READY")){			//client sends ready, so begin game
					out.println("BEGIN");
					
					if(serverPlayer == Piece.Type.WHITE && !human){

						String move;					//if the server is white and the AI is playing...
						TimedMoveGet tmg = new TimedMoveGet(game.copyGame(), 0, 0, serverPlayer);
						Thread t = new Thread(tmg);
						t.run();
						try {
							t.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						move = tmg.bestMove;			//tmg.bestMove is updated anytime a better move is found
						game.move(move);
						out.println(move);				//do the move and send it to the client

					}
					else if(serverPlayer == Piece.Type.WHITE && human){
						
						String input = getPlayerInput();
						input = game.convertToInternalMove(input);
						
						game.move(input);
						out.println(input);
						
					}
					continue;		//go back to the beginning of the while loop and wait for more input
				}
				if(playerInput.equals("OK")){
					startTime = System.currentTimeMillis();			//client has acknowledged getting a move, start timer
					continue;
				}
				if(playerInput.equals("TIME")){
					System.out.println("Time exceeded");		//Client reports that the server took too long to respond
					out.println("WINNER");
					break;
				}
				if(playerInput.equals("ILLEGAL")){
					System.out.println("Illegal move");			//client reports that the server attempted an illegal move
					out.println("WINNER");
					break;
				}
				
				System.out.println("input = " + playerInput);	
				int index = playerInput.indexOf(' ');
				String command = "";
				try{
					command = playerInput.substring(0,index);		//Gets the command from the player input
				}
				catch(Exception e){
					command = playerInput;
				}
				
				if(command.equals("A") || command.equals("W") || command.equals("S") || command.equals("P")){	//A move command
					endTime = System.currentTimeMillis();
					
					if(endTime-startTime > responseTime && responseTime != 0 && game.numberOfTurns != 0){
						out.println("TIME");
						out.println("LOSER");			//Client exceeded time limit on a move that wasnt the first		
						break;
					}
					
					
					out.println("OK");					//Server responds ok, it received a move
					long time3 = System.currentTimeMillis();
					if(game.capturingMoveAvailable() && !game.isPossibleCapturingMove(playerInput)){
						out.println("ILLEGAL");
						out.println("LOSER");		//If the move is illegal, notify the client
						System.out.println("WINNER");
						break;
					}
					else if(!game.capturingMoveAvailable() && !game.isPossibleNonCapturingMove(playerInput)){
						out.println("ILLEGAL");
						out.println("LOSER");
						System.out.println("WINNER");
						break;
					}
					
					game.move(playerInput);
					game.prettyprint();
					
					int val = game.checkEndGame();		//returns an int which represents the possible game states
					if(val == 1){
																			//white win
						if(clientPlayer == Piece.Type.WHITE){
							out.println("WINNER");
							System.out.println("LOSER");
							break;
						}
						else{
							out.println("LOSER");
							System.out.println("WINNER");
							break;
						}
					}
					else if(val == -1){
																			//black win
						if(clientPlayer == Piece.Type.WHITE){
							out.println("LOSER");
							System.out.println("WINNER");
							break;
						}
						else{
							out.println("WINNER");
							System.out.println("LOSER");
							break;
						}
					}
					else if(val == 2){
																			//max turns
						out.println("TIE");
						System.out.println("TIE");
						break;
					}
					else{											//The game is not over
						if(!human){											

							String move;
							TimedMoveGet tmg = new TimedMoveGet(game.copyGame(), time3, responseTime, serverPlayer);
							Thread t = new Thread(tmg);
							t.run();										//Get a move from the AI with a time limit of responseTime
							try {											//time3 is the start time
								t.join();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							move = tmg.bestMove;
							game.move(move);
							out.println(move);

						}
						else{
							String input = getPlayerInput();
							input = game.convertToInternalMove(input);
							
							game.move(input);
							out.println(input);
						}
					}
					
					val = game.checkEndGame();					//perform an endgame check again
					if(val == 1){
						//white win
						if(clientPlayer == Piece.Type.WHITE){
							out.println("WINNER");
							System.out.println("LOSER");
							break;
						}
						else{
							out.println("LOSER");
							System.out.println("WINNER");
							break;
						}
					}
					else if(val == -1){
						//black win
						if(clientPlayer == Piece.Type.WHITE){
							out.println("LOSER");
							System.out.println("WINNER");
							break;
						}
						else{
							out.println("WINNER");
							System.out.println("LOSER");
							break;
						}
					}
					else if(val == 2){
						//max turns
						out.println("TIE");
						System.out.println("TIE");
						break;
					}
					//Approach move
				}
				else{									//The command was not recognized
					out.println("ILLEGAL");
					cont = false;
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			sock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void clientMode(boolean human,String host, int port){
		
		//Running as a client
		
		long startTime = 0;
		long endTime = 0;
		
		Fanorona game = null;
		Piece.Type clientPlayer = null;
		Piece.Type serverPlayer = null;
		
		int responseTime = 0;
		
		Socket tSock = null;
		BufferedReader in = null;
		PrintWriter out = null;
		
		try {
			tSock = new Socket(host, port);					//Socket related
			out = new PrintWriter(tSock.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(tSock.getInputStream()));
		}
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("client - got connection");
		
		String response = "";
		try {
			while((response = in.readLine()) != null){
				System.out.println("from server: "+response+".");
				if(response.equals("WELCOME")){						//server sends a welcome, nothing to do here
					continue;
				}
				if(response.equals("OK")){
					startTime = System.currentTimeMillis();				//server received the move, start timer
					continue;
				}
					if(response.equals("ILLEGAL")){
						break;											//server reports that an illegal move was attempted
				}
				if(response.equals("LOSER")){						
					System.out.println("Lost");
					break;
				}													//Server reporting win/loss condition
				if(response.equals("WINNER")){
					System.out.println("won");
					break;
				}
				if(response.equals("TIE")){							//TODO display end game conditions on the gamewindow
					System.out.println("Tie");
					break;
				}
				if(response.equals("TIME")){
					System.out.println("Time limit exceeded");		//Server reporting time limit exceeded, continue. A LOSER command is coming next.
					continue;
				}
				
				int index = response.indexOf(' ');
				String command = response;
				if(index == -1){
					command = response;								//Retrieve the command from the player input
				}
				else{
					command = response.substring(0,index);
				}
				System.out.println("Command: "+command);
				
				if(command.equals("INFO")){
				
					String cmd = response;
					cmd = cmd.substring(index+1);
					index = cmd.indexOf(' ');
					int columns = Integer.parseInt(cmd.substring(0,index));
					cmd = cmd.substring(index+1);
					index = cmd.indexOf(' ');
					int rows = Integer.parseInt(cmd.substring(0,index));			//Get the various parameters passed by the server
					cmd = cmd.substring(index+1);
					index = cmd.indexOf(' ');
					char startType = cmd.charAt(index-1);
					cmd = cmd.substring(index+1);
					int timeRestriction = Integer.parseInt(cmd);
					System.out.println("rows: "+rows+" columns: "+columns);
					
					game = new Fanorona(columns,rows);
					if(startType == 'W'){
						clientPlayer = Piece.Type.WHITE;
					}																	//Determine which side the client plays on
					else if(startType == 'B'){
						clientPlayer = Piece.Type.BLACK;
					}
					else{
						System.out.println("Type error");
						System.exit(1);
					}
					if(clientPlayer == Piece.Type.WHITE){
						serverPlayer = Piece.Type.BLACK;
					}
					else{
						serverPlayer = Piece.Type.WHITE;
					}
						responseTime = timeRestriction;
						out.println("READY");												//Let server know the client is done setting up the game
					}
				else if(command.equals("BEGIN")){
													//Start game
					game.prettyprint();
					if(clientPlayer == Piece.Type.WHITE && !human){
						String move;
						TimedMoveGet tmg = new TimedMoveGet(game.copyGame(), 0, 0, clientPlayer);		//get an AI move with no time restriction
						Thread t = new Thread(tmg);
						t.run();
						try {
							t.join();
						}
						catch (InterruptedException e) {
							e.printStackTrace();
						}
						move = tmg.bestMove;
						game.move(move);
						out.println(move);
					}
					else if(clientPlayer == Piece.Type.WHITE && human){
					
						String input = getPlayerInput();
						input = game.convertToInternalMove(input);
						
						game.move(input);
						out.println(input);
						
					}
					continue;
				}
				else if(command.equals("A") || command.equals("W") || command.equals("S") || command.equals("P")){
				
					endTime = System.currentTimeMillis();
					if(endTime - startTime > responseTime && responseTime != 0 && game.numberOfTurns != 0){
						out.println("TIME");						//report time limit exceeded
						continue;
					}
					
					out.println("OK");								//acknowledge move received
					startTime = System.currentTimeMillis();
					
					if(game.capturingMoveAvailable() && !game.isPossibleCapturingMove(response)){
						out.println("ILLEGAL");
						continue;
					}
					else if(!game.capturingMoveAvailable() && !game.isPossibleNonCapturingMove(response)){
						out.println("ILLEGAL");
						continue;
					}
					game.move(response);
					game.prettyprint();
					if(!human){
						String move;
						long tempTime = (long) responseTime;
						TimedMoveGet tmg = new TimedMoveGet(game.copyGame(), startTime, tempTime, clientPlayer);		//Get AI move move with time limit
						Thread t = new Thread(tmg);
						t.run();
						
						try {
							t.join();
						}
						catch (InterruptedException e) {
							e.printStackTrace();
						}
						move = tmg.bestMove;
						System.out.println("Move: "+move);
						game.move(move);
						game.prettyprint();
						out.println(move);
						}
					else{
						String input = getPlayerInput();
						input = game.convertToInternalMove(input);
					
						game.move(input);
						out.println(input);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();			//the end of a giant try/catch which started right before the while loop
		}
		
		out.close();
		try {
			in.close();					//clean up the buffers and sockets
			tSock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
