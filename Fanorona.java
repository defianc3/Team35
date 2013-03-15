/*

	Fanorona game

	Constructor takes two arguments, the number of rows and columns



	Possible input format:
		Upon prompt for input, play lists the move as such:
			[row][column] [row2][column2] [w/a/f]

			Where [row][column] is the piece to move, [row2][column2] is the place to move it, and
			[w/d] indicates whether this move is to be a withdrawal capture, approach capture, or capture-free move

	If a capturing move is possible, a capturing move must be taken

*/



class Fanorona{

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

		board.capturingMoveAvailable();

		return true;
	}

	public void move(int row1, int col1, int row2, int col2, char type){
	
		String s = ""+row2+col2;

		//boolean needToCapture = capturingMoveAvailable();

		//boolean valid = board.isPossibleMove(row1,row2,s);
		boolean valid = board.isPossibleMove(row1,col1,s, type);
		//check to see if move is valid
		//System.out.println("type: "+type);
		if(valid){
			System.out.println("valid");
			board.movePiece(row1,col1,row2,col2,type);
		}
	}

	Piece.Type activePlayer(){
		return board.activePlayer;
	}
}