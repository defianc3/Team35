import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class clientThread extends Thread {

	int port;
	String host;
	
	
	public clientThread(String h, int p){
		port = p;
		host = h;
	}
	
	public void run(){
		
		Fanorona game = null;
		Piece.Type clientPlayer = null;
		Piece.Type serverPlayer = null;
		
		int responseTime;
	
		Socket tSock = null;
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			tSock = new Socket(host, port);
			out = new PrintWriter(tSock.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(tSock.getInputStream()));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("client - got connection");
		
		String response = "";
		try {
			while((response = in.readLine()) != null){
				System.out.println("from server: "+response+".");
				if(response.equals("WELCOME")){
					continue;
				}
				if(response.equals("OK")){
					continue;
				}
				if(response.equals("ILLEGAL")){
					break;
				}
				
				int index = response.indexOf(' ');
				String command = response;
				if(index == -1){
					command = response;
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
					if(clientPlayer == Piece.Type.WHITE){
						serverPlayer = Piece.Type.BLACK;
					}
					else{
						serverPlayer = Piece.Type.WHITE;
					}
					responseTime = timeRestriction;
					out.println("READY");
				}
				else if(command.equals("BEGIN")){
					//Start game
					System.out.println("In here");
					if(clientPlayer == Piece.Type.WHITE){
						String move = game.getAIMove(clientPlayer);
						game.move(move);
						out.println(move);
					}
					continue;
				}
				else if(command.equals("A")){
					
					
					out.println("OK");
					game.move(response);
					System.out.println("check1");
					game.prettyprint();
//					System.out.print("Enter a move ");
//					String playerInput = "";
//					Scanner scan2 = new Scanner(System.in);
//					playerInput = scan2.nextLine();
//					try{
//						playerInput = game.convertToInternalMove(playerInput);
//					}
//					catch(Exception e){
//						
//					}
//					out.println(playerInput);
					System.out.println("Active player: "+game.activePlayer());
					String move = game.getAIMove(clientPlayer);
					System.out.println("check1");
					game.move(move);
					game.prettyprint();
					out.println(move);
					
					//Approach move
				}
				else if(command.equals("W")){
					out.println("OK");
					game.move(response);
					game.prettyprint();
//					System.out.print("Enter a move ");
//					String playerInput = "";
//					Scanner scan2 = new Scanner(System.in);
//					playerInput = scan2.nextLine();
//					playerInput = game.convertToInternalMove(playerInput);
//					game.move(playerInput);
//					out.println(playerInput);
//					

					String move = game.getAIMove(clientPlayer);
					game.move(move);
					game.prettyprint();
					out.println(move);
					//withdrawal
				}
				else if(command.equals("P")){
					out.println("OK");
					game.move(response);
					game.prettyprint();
//					System.out.print("Enter a move ");
//					String playerInput = "";
//					Scanner scan2 = new Scanner(System.in);
//					playerInput = scan2.nextLine();
//					playerInput = game.convertToInternalMove(playerInput);
//					game.move(playerInput);
//					out.println(playerInput);
					

					String move = game.getAIMove(clientPlayer);
					game.move(move);
					game.prettyprint();
					out.println(move);
					//piaka
				}
				else if(command.equals("S")){
					out.println("OK");
					game.move(response);
					game.prettyprint();
					System.out.print("Enter a move ");
					String playerInput = "";
					Scanner scan2 = new Scanner(System.in);
					playerInput = scan2.nextLine();
					playerInput = game.convertToInternalMove(playerInput);
					game.move(playerInput);
					out.println(playerInput);
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		out.close();
		try {
			in.close();
			tSock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
