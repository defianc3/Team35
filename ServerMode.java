import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerMode implements Runnable{
	
	
	public static String getPlayerInput(){
		System.out.print("Enter a move ");
		String playerInput = "";
		Scanner scan2 = new Scanner(System.in);			//The server is white and a human is playing
		playerInput = scan2.nextLine();					//TODO update this code to work with the gamewindow
		return playerInput;
	}
	

	int portNumber;
	int _rows;
	int _columns;
	char clientT;
	int responseTime;
	boolean human;
	
	
	
	ServerMode(int rows, int _cols, int _port, char _clientT, int response, boolean _human){
		portNumber = _port;
		_rows = rows;
		_columns = _cols;
		clientT = _clientT;
		responseTime = response;
		human = _human;
	}
	
	
	public void run(){
		
		long startTime = 0;
		long endTime = 0;

		Fanorona game;
		Piece.Type clientPlayer;
		ServerSocket sock = null;

		try{
			sock = new ServerSocket(portNumber);
		}
		catch(Exception e){
			System.out.println("Port not available");
		}
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
}