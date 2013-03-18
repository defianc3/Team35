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