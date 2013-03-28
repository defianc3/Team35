import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class clientThread extends Thread {

	int port;
	String host;
	
	
	public clientThread(String h, int p){
		port = p;
		host = h;
	}
	
	public void run(){
		
		Fanorona game;
		Piece.Type clientPlayer;
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
				
				int index = response.indexOf(' ');
				String command = response.substring(0,index);
				
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
					responseTime = timeRestriction;
					out.println("READY");
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
				if(response.equals("quit")){
					out.println("Client is quitting");
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
