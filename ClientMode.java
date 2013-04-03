import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class ClientMode implements Runnable {
	
	
	public static String getPlayerInput(){
		System.out.print("Enter a move ");
		String playerInput = "";
		Scanner scan2 = new Scanner(System.in);			//The server is white and a human is playing
		playerInput = scan2.nextLine();					//TODO update this code to work with the gamewindow
		return playerInput;
	}

	
	String host;
	int port;
	boolean human;
	
	ClientMode(String _host, int _port, boolean _human){
		host = _host;
		port = _port;
		human = _human;
	}
	
	
	public void run(){
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
		
		boolean cont = true;
		while(cont){
			try {
				tSock = new Socket(host, port);					//Socket related
				out = new PrintWriter(tSock.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(tSock.getInputStream()));
				cont = false;
			}
			catch (UnknownHostException e) {
			}
			catch (IOException e) {
			}
			catch (Exception e){
	
			}
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
