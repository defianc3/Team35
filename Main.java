import java.io.*;
import java.util.Scanner;

class Main{
	public static void main(String args[]){

		System.out.println("\nX=black  0=white, N='null'(no piece)\n");

		Fanorona game = new Fanorona(5,9);

		/*

		game.prettyprint();

		System.out.println(game.capturingMoveAvailable());

		System.out.println("Possible capturing move: "+game.board.isPossibleCapturingMove(3,4,"24"));

		game.move(3,4,2,4,'a');

		System.out.println(game.capturingMoveAvailable());

		game.prettyprint();

		game.move(1,5,0,4,'w');

		game.prettyprint();

		game.move(2,4,1,5,'a');

		game.prettyprint();

		*/
		game.prettyprint();

		boolean endGame = false;
		while(!endGame){

			System.out.println("Active player: "+game.activePlayer());
			if(game.capturingMoveAvailable()) System.out.println("Capturing move required");
			System.out.print("Enter a move: ");

			String move = "";
			int piece1 = -1;
			int piece2 = -1;

			String spiece1 = "";
			String spiece2 = "";

			String c = "";
			Scanner scan = new Scanner(System.in);


			try {
	        	piece1 = scan.nextInt();
	        	piece2 = scan.nextInt();

	        	spiece1 = Integer.toString(piece1);
	        	spiece2 = Integer.toString(piece2);

	        	if(piece1 < 10){
	        		spiece1 = "0"+spiece1;
	        	}
	        	if(spiece1.length() != 2){
	        		spiece1 = spiece1+="0";
	        	}

	        	if(piece2 < 10){
	        		spiece2 = "0"+spiece2;
	        	}
	        	if(spiece2.length() != 2){
	        		spiece2 = spiece2+="0";
	        	}

	        	c = scan.nextLine();

	      	}
	      	catch(Exception e){
	        	System.out.println("IO error");
	        	System.exit(1);
	      	}

	      	move = spiece1 + " " + spiece2 + c;

	      	int row1 = move.charAt(0)-48;
	      	int col1 = move.charAt(1)-48;
	      	int row2 = move.charAt(3)-48;
	      	int col2 = move.charAt(4)-48;
	      	char c2 = move.charAt(6);

	      	boolean valid = true;
	      	// if(!Character.isDigit(move.charAt(0)) || !Character.isDigit(move.charAt(1)) || !Character.isDigit(move.charAt(3)) || !Character.isDigit(move.charAt(4))){
	      	// 	valid=false;
	      	// }

	      	if(row1 > 4 || row1 < 0 || row2 > 4 || row2 < 0 || col1 < 0 || col1 > 8 || col2 < 0 || col2 > 8 || !(c2 == 'a' || c2 == 'w' || c2 == 'f')){
	      		valid = false;
	      	}
	      	if(!valid){
	      		System.out.println("not valid");
	      	}
	      	else{
	      		if(game.capturingMoveAvailable()){
		      		if(game.isPossibleCapturingMove(row1, col1, row2, col2, c2)){
		      			System.out.println("inside here");
		      			System.out.println("is a capturing move");
		      			game.move(row1, col1, row2, col2, c2);
		      		}
		      		else{
		      			System.out.println("A capturing move must be entered\n");
		      		}
		      	}
		      	else{
		      		game.move(row1,col1,row2,col2,c2);
		      	}
	      	}

	      	game.prettyprint();

		}
	}
}