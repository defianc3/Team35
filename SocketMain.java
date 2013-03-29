import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;
import java.util.Date;


import java.io.*;
import java.net.*;

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
		
		Fanorona game;
		Piece.Type clientPlayer;
		int responseTime;
		ServerSocket sock = create();
		System.out.println("Listening on "+sock.getLocalPort());
		
		clientThread cT = new clientThread("",sock.getLocalPort());
		cT.start();
		
		Socket client = null;
		PrintWriter out = null;
		BufferedReader in = null;
		try{
			client = sock.accept();
			out = new PrintWriter(client.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		}
		catch(IOException e){
			System.out.println("Accept failed");
			System.exit(1);
		}
		
		String inputLine, outputLine;
		out.println("WELCOME");
		int _rows = 5;
		int _columns = 9;
		char clientT = 'B';
		out.println("INFO "+_columns+" "+_rows+" "+clientT+" "+"5000");
		
		game = new Fanorona(9,5);
		Piece.Type serverPlayer;
		if(clientT == 'B'){
			clientPlayer = Piece.Type.BLACK;
			serverPlayer = Piece.Type.WHITE;
		}
		else{
			clientPlayer = Piece.Type.WHITE;
			serverPlayer = Piece.Type.BLACK;
		}

		String playerInput = "";
		try {
			while((playerInput = in.readLine()) != null){
	//			System.out.print("Enter a command: ");
				System.out.println("From client: "+playerInput);
					
	
	//			Scanner scan2 = new Scanner(System.in);
				if(playerInput.equals("READY")){
					out.println("BEGIN");
					
					if(serverPlayer == Piece.Type.WHITE){
						String move = game.getAIMove(serverPlayer);
						game.move(move);
						out.println(move);
					}
					
					continue;
				}
				if(playerInput.equals("OK")){
					out.println("OK");
					continue;
				}
//			playerInput = scan2.nextLine();
				System.out.println("input = " + playerInput);
				int index = playerInput.indexOf(' ');
				String command = playerInput.substring(0,index);
				
				
				
				if(command.equals("INFO")){
					String cmd = playerInput;
					cmd = cmd.substring(index+1);
					index = cmd.indexOf(' ');
					int columns = Integer.parseInt(cmd.substring(0,index));
					cmd = cmd.substring(index+1);
					index = cmd.indexOf(' ');
					int rows = Integer.parseInt(cmd.substring(0,index));
					cmd = cmd.substring(index+1);
					index = cmd.indexOf(' ');
					char startType = cmd.charAt(index-1);
					cmd = cmd.substring(index+1);
					int timeRestriction = Integer.parseInt(cmd);
					game = new Fanorona(columns,rows);
					if(startType == 'W'){
						clientPlayer = Piece.Type.WHITE;
					}
					else if(startType == 'B'){
						clientPlayer = Piece.Type.BLACK;
					}
					else{
						System.out.println("Type error");
						System.exit(1);
					}
					responseTime = timeRestriction;
					System.out.println("READY");
				}
				else if(command.equals("BEGIN")){
					//Start game
				}
				else if(command.equals("A")){
					out.println("OK");
					
					if(!game.isPossibleCapturingMove(playerInput)){
						out.println("ILLEGAL");
						out.println("LOSER");
					}
					game.move(playerInput);
					game.prettyprint();
					int val = game.checkEndGame();
					if(val == 1){
						//white win
						if(clientPlayer == Piece.Type.WHITE){
							out.println("WINNER");
						}
						else{
							out.println("LOSER");
						}
					}
					else if(val == -1){
						//black win
						if(clientPlayer == Piece.Type.WHITE){
							out.println("LOSER");
						}
						else{
							out.println("WINNER");
						}
					}
					else if(val == 2){
						//max turns
						out.println("TIE");
					}
					else{
						out.println(game.getAIMove(serverPlayer));
					}
					//Approach move
				}
				else if(command.equals("W")){
					
					out.println("OK");
					
					game.move(playerInput);
					game.prettyprint();
					int val = game.checkEndGame();
					if(val == 1){
						//white win
						if(clientPlayer == Piece.Type.WHITE){
							out.println("WINNER");
						}
						else{
							out.println("LOSER");
						}
					}
					else if(val == -1){
						//black win
						if(clientPlayer == Piece.Type.WHITE){
							out.println("LOSER");
						}
						else{
							out.println("WINNER");
						}
					}
					else if(val == 2){
						//max turns
						out.println("TIE");
					}
					else{
						out.println(game.getAIMove(serverPlayer));
					}
					
					//withdrawal
				}
				else if(command.equals("P")){
					
					out.println("OK");
					game.move(playerInput);
					game.prettyprint();
					int val = game.checkEndGame();
					if(val == 1){
						//white win
						if(clientPlayer == Piece.Type.WHITE){
							out.println("WINNER");
						}
						else{
							out.println("LOSER");
						}
					}
					else if(val == -1){
						//black win
						if(clientPlayer == Piece.Type.WHITE){
							out.println("LOSER");
						}
						else{
							out.println("WINNER");
						}
					}
					else if(val == 2){
						//max turns
						out.println("TIE");
					}
					else{
						out.println(game.getAIMove(serverPlayer));
					}
					//piaka
				}
				else if(command.equals("S")){
					
					out.println("OK");
					game.move(playerInput);
					game.prettyprint();
					int val = game.checkEndGame();
					if(val == 1){
						//white win
						if(clientPlayer == Piece.Type.WHITE){
							out.println("WINNER");
						}
						else{
							out.println("LOSER");
						}
					}
					else if(val == -1){
						//black win
						if(clientPlayer == Piece.Type.WHITE){
							out.println("LOSER");
						}
						else{
							out.println("WINNER");
						}
					}
					else if(val == 2){
						//max turns
						out.println("TIE");
					}
					else{
						out.println(game.getAIMove(serverPlayer));
					}
					break;
				}
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			sock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
