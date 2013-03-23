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

	long player1Time;
	long player2Time;

	Fanorona(int row, int col){
		board = new Board(row, col,Piece.Type.WHITE);
		player1Time = 0;
		player2Time = 0;
	}
	
	Fanorona(int row, int col, Board b, Piece.Type active){
		board = new Board(row,col,b.array,active);
		player1Time = 0;
		player2Time = 0;
	}

	public void printTime(){
		System.out.println("White time: "+player1Time/1000.0);
		System.out.println("Black time: "+player2Time/1000.0+"\n");
	}

	void prettyprint(){
		board.prettyprint();
	}
	
	public Fanorona copyGame(){
		return new Fanorona(board.rows, board.columns, board.copyBoard(), board.activePlayer);
	}
	
	public Board copyBoard(){
		return board.copyBoard();
	}

	void printScore(){
		board.printScore(Piece.Type.WHITE);
		board.printScore(Piece.Type.BLACK);
		System.out.println();
	}

	boolean isPossibleCapturingMove(int row1, int col1, int row2, int col2, char type){
		return board.isPossibleCapturingMove(row1, col1,row2,col2, type);
	}

	boolean capturingMoveAvailable(){

		return board.capturingMoveAvailable();
	}

	//returns true if a successive capture is possible, false otherwise
	public boolean move(int row1, int col1, int row2, int col2, char type){

		//boolean valid = board.isPossibleMove(row1,col1,row2, col2, type);
		boolean valid = false;
		//boolean captAvail = false;
		if(capturingMoveAvailable() && board.isPossibleCapturingMove(row1,col1,row2,col2,type)){
			valid = true;
		}
		else if(!capturingMoveAvailable() && board.isPossibleMove(row1,col1,row2,col2,type)){
			valid = true;
		}

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

			//board.prettyprint();
			
			if(board.capturingMoveAvailable(board.array[row2][col2]) && type != 'f'){
				System.out.println("Successive capture available");
				return true;
			}

			if(board.activePlayer == Piece.Type.WHITE){
				board.whiteMoves += "\n";
			}
			else{
				board.blackMoves += "\n";
			}
			
			board.latestDirectionMoved = "";

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

		String ret = "";
		if(capturingMoveAvailable()){
			for(int i = 0; i < board.rows; i++){
				for(int j = 0; j < board.columns; j++){
					String move = board.PossibleCapturingMoves(board.array[i][j]);
					Piece p = board.array[i][j];
					if(move.length() > 0){
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
			return ret;
		}
		else{
			for(int i = 0; i < board.rows; i++){
				for(int j = 0; j < board.columns; j++){
					String move = board.possibleMoves(board.array[i][j]);
					Piece p = board.array[i][j];
					if(move.length() > 0){
						if(board.isPossibleMove(p.row, p.column, move.charAt(1)-48, move.charAt(2)-48, 'f')){
							ret = ""+p.row+p.column+" "+(move.charAt(1))+""+(move.charAt(2))+" f";
							break;
						}
					}
				}
			}
			return ret;
		}
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