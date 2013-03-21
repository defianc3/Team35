/*

	Fanorona game

	Constructor takes two arguments, the number of rows and columns



	Possible input format:
		[row][column] [row2][column2] [w/a/f]

		Where [row][column] is the piece to move, [row2][column2] is the place to move it, and
		[w/a/f] indicates whether this move is to be a withdrawal capture, approach capture, or capture-free move

	If a capturing move is possible, a capturing move must be taken

*/



class Fanorona implements Evaluatable{

	public Board board;

	Fanorona(int row, int col){
		board = new Board(row, col,Piece.Type.WHITE);
	}

	void prettyprint(){
		board.prettyprint();
	}

	void printScore(){
		board.printScore(Piece.Type.WHITE);
		board.printScore(Piece.Type.BLACK);
	}

	boolean isPossibleCapturingMove(int row1, int col1, int row2, int col2, char type){
		return board.isPossibleCapturingMove(row1, col1, ""+row2+col2, type);
	}

	boolean capturingMoveAvailable(){

		return board.capturingMoveAvailable();
	}

	//returns true if a successive capture is possible, false otherwise
	public boolean move(int row1, int col1, int row2, int col2, char type){
	
		String s = ""+row2+col2;

		boolean valid = board.isPossibleMove(row1,col1,s, type);

		//check to see if move is valid
		if(valid){
			System.out.println("valid");
			Piece.Type temp = board.activePlayer;
			board.movePiece(row1,col1,row2,col2,type);
			board.activePlayer = temp;

			if(board.activePlayer == Piece.Type.WHITE){
				board.whiteMoves += ""+row1+col1+">"+row2+col2;
			}
			else{
				board.blackMoves += ""+row1+col1+">"+row2+col2;
			}

			if(board.capturingMoveAvailable(board.array[row2][col2])){
				System.out.println("Successive capture available");
				return true;
			}

			if(board.activePlayer == Piece.Type.WHITE){
				board.whiteMoves += "\n";
			}
			else{
				board.blackMoves += "\n";
			}

			if(temp == Piece.Type.WHITE){
				board.activePlayer = Piece.Type.BLACK;
			}
			else{
				board.activePlayer = Piece.Type.WHITE;
			}

			return false;
		}
		else{
			System.out.println("Invalid move entered");
		}
		return false;
	}

	public String getRandomMove(){
		int row = 0;
		int col = 0;
		boolean successive = false;
		/* NOTE: eventually this code should be moved to isPossibleCapturingMove */
		if(board.blackMoves.endsWith("\n") || board.blackMoves.length() == 0){
			//this is the first move of black's turn, any piece can be moved
		}
		else{
			//this is a successive move
			row = board.blackMoves.charAt(board.blackMoves.length()-2)-48;
			col = board.blackMoves.charAt(board.blackMoves.length()-1)-48;
			successive = true;
		}
		String ret = "";
		for(int i = 0; i < board.rows; i++){
			for(int j = 0; j < board.columns; j++){
				String move = board.PossibleCapturingMoves(board.array[i][j]);
				//System.out.println("Fanorona move: "+move);
				Piece p = board.array[i][j];
				//System.out.println(move.length());
				if(successive && (i != row || j != col)){

				}
				else{
					if(move.length() > 0){
						String movetest = ""+p.row+p.column+" "+(move.charAt(1))+""+(move.charAt(2));
						//System.out.println("movetest: "+movetest);
						if(isPossibleCapturingMove(p.row, p.column, move.charAt(1)-48, move.charAt(2)-48, 'a')){
							ret = ""+p.row+p.column+" "+(move.charAt(1))+""+(move.charAt(2))+" a";
							break;
						}
						else if(isPossibleCapturingMove(p.row, p.column, move.charAt(1)-48, move.charAt(2)-48, 'w')){
							ret = ""+p.row+p.column+" "+(move.charAt(1))+""+(move.charAt(2)) + " w";
							break;
						}
					}
				}
			}
		}
		//System.out.println("ret: "+ret);
		return ret;
	}

	Piece.Type activePlayer(){
		return board.activePlayer;
	}
	
	@Override
	public int evaluate() {
		return board.numberRemaining(Piece.Type.WHITE) -
				board.numberRemaining(Piece.Type.BLACK);
	}

	@Override
	public Evaluatable getNextState() {
		// TODO Auto-generated method stub
		return null;
	}
}