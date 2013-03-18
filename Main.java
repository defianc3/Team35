import java.util.Scanner;

class Main{
	public static void main(String args[]){

		System.out.println("\nX=black  0=white, N='null'(no piece)\n");

		Fanorona game = new Fanorona(5,9);

		game.prettyprint();

		int turn = 0;
		while(true){
			if(game.capturingMoveAvailable()) System.out.println("Capturing move required");
			System.out.print("Enter a move: ");

			String move = "";
			int pieceInt1 = -1;
			int pieceInt2 = -1;

			String pieceStr1 = "";
			String pieceStr2 = "";

			String playerInput = "";
			Scanner scan2 = new Scanner(System.in);
			playerInput = scan2.nextLine();
			Scanner scan = new Scanner(playerInput);

			if(playerInput.equals("quit")){
				System.out.println("\nExiting\n\n");
				scan.close();
				scan2.close();
				System.exit(0);
			}
			else if(playerInput.equals("moves")){
				System.out.println("\nwhite: "+game.board.whiteMoves);
				System.out.println("\nblack: "+game.board.blackMoves);
			}
			else{
				try {
		        	pieceInt1 = scan.nextInt();
		        	pieceInt2 = scan.nextInt();

		        	pieceStr1 = Integer.toString(pieceInt1);
		        	pieceStr2 = Integer.toString(pieceInt2);

		        	if(pieceInt1 < 10){
		        		pieceStr1 = "0"+pieceStr1;
		        	}
		        	if(pieceStr1.length() != 2){
		        		pieceStr1 = pieceStr1+="0";
		        	}

		        	if(pieceInt2 < 10){
		        		pieceStr2 = "0"+pieceStr2;
		        	}
		        	if(pieceStr2.length() != 2){
		        		pieceStr2 = pieceStr2+="0";
		        	}

		        	playerInput = scan.nextLine();


			      	move = pieceStr1 + " " + pieceStr2 + playerInput;

			      	int row1 = move.charAt(0)-48;
			      	int col1 = move.charAt(1)-48;
			      	int row2 = move.charAt(3)-48;
			      	int col2 = move.charAt(4)-48;
			      	char moveType = move.charAt(6);

			      	boolean valid = true;
			      	// if(!Character.isDigit(move.charAt(0)) || !Character.isDigit(move.charAt(1)) || !Character.isDigit(move.charAt(3)) || !Character.isDigit(move.charAt(4))){
			      	// 	valid=false;
			      	// }

			      	if(row1 > 4 || row1 < 0 || row2 > 4 || row2 < 0 || col1 < 0 || col1 > 8 || col2 < 0 || col2 > 8 || !(moveType == 'a' || moveType == 'w' || moveType == 'f')){
			      		valid = false;
			      	}
			      	if(!valid){
			      		System.out.println("not valid");
			      	}
			      	else{
			      		if(game.capturingMoveAvailable()){
				      		if(game.isPossibleCapturingMove(row1, col1, row2, col2, moveType)){
				      			System.out.println("inside here");
				      			System.out.println("is a capturing move");
				      			boolean successiveMove = game.move(row1, col1, row2, col2, moveType);
				      			if(successiveMove){

				      			}
				      		}
				      		else{
				      			System.out.println("A capturing move must be entered\n");
				      		}
				      	}
				      	else{
				      		game.move(row1,col1,row2,col2,moveType);
				      	}
			      	}
			   	}
				catch(Exception e){
	        		System.out.println("Error: " + e.getMessage());
	      		}
      		}
		  	game.prettyprint();
		  	/* TODO Is this the correct spot for this to break? */
		  	turn++;
		  	if (turn == 25) {
		  		break;
		  	}
		}
	}
}