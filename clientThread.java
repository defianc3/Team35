import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class clientThread extends Thread {

	int socket_num;
	
	
	public clientThread(int i){
		socket_num = i;
	}
	
	public void run(){
	
		Socket tSock = null;
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			tSock = new Socket("", socket_num);
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
