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
		
//		Socket client = null;
//		try{
//			client = sock.accept();
//		}
//		catch(IOException e){
//			System.out.println("Accept failed");
//			System.exit(1);
//		}

		while(true){
			System.out.print("Enter a command: ");
			
			String playerInput = "";
			Scanner scan2 = new Scanner(System.in);
			playerInput = scan2.nextLine();
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
				
				//Approach move
			}
			else if(command.equals("W")){
				//withdrawal
			}
			else if(command.equals("P")){
				//piaka
			}
			else if(command.equals("S")){
				break;
			}
		}
		//sock.close();
	}
}
