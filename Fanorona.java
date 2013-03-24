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
	
	int numberOfPossibleMoves;
	int lastEvaluated;
	
	String lastStringReturned;
	
	static int cc;

	Fanorona(int row, int col){
		board = new Board(row, col,Piece.Type.WHITE);
		player1Time = 0;
		player2Time = 0;
	}
	
	public void setCC(int val){
		cc = val;
	}
	
	Fanorona(int row, int col, Board b, Piece.Type active){
		board = new Board(row,col,b.array,active);
		player1Time = 0;
		player2Time = 0;
	}
	
	private Fanorona getState(int n){
		
		return this;
	}
	
	
	public String numberOfMoves2(int row, int column, boolean recursion,int c,int goal){
		int count = 0;
		if(this.board.capturingMoveAvailable(this.board.array[row][column])){
			String possibleMoves = this.board.PossibleCapturingMovesWithDirection(this.board.array[row][column]);
			//System.out.print("possible moves: "+possibleMoves);
			//System.out.println("    length: "+possibleMoves.length());
			int temp = 0;
			for(int k = 0; k < possibleMoves.length()/8; k++){
				String move = possibleMoves.substring(temp+1,temp+8);
				String retval = "";
				if(moveHasSuccessiveCaptures(move)){
					Fanorona newGame = copyGame();
					int tempRow = getSecondRow(move);	//hold the destination position so i can call recursively
					int tempCol = getSecondColumn(move);
					newGame.move(getFirstRow(move), getFirstColumn(move), getSecondRow(move), getSecondColumn(move), getMoveType(move));
					//count += newGame.numberOfMoves2(tempRow, tempCol, true,c,goal);
					String result = numberOfMoves2(tempRow,tempCol,false,c,goal);
					boolean number = true;
					for(int L = 0; L < result.length(); L++){
						if(!Character.isDigit(result.charAt(L))){
							number = false;
						}
					}
					if(number){
						count += Integer.parseInt(result);
						cc += Integer.parseInt(result);
					}
					else if(cc == goal){
						return result;
					}
				}
				else{
					count++;
					cc++;
					if(cc == goal) return move;
				}
				temp+=8;
				if(cc== goal) return move;
				/*TODO make this code more general. now it just does the same thing as count += length/8 */
			}
		}
		else if(!recursion){
			String possibleMoves = this.board.possibleMovesWithDirection(this.board.array[row][column]);
			//System.out.println("Possible moves: "+possibleMoves);
			int temp = 0;
			for(int k = 0; k < possibleMoves.length()/8; k++){
				//also generalize this code
				String move = possibleMoves.substring(temp+1,temp+8);
				count++;
				cc++;
				if(cc == goal){
					return move;
				}
			}
		}
		return ""+c;
	}
	
	public String numberOfMoves2(int c,int goal){
		int count = 0;
		cc = 0;
		for(int i = 0; i < board.rows; i++){
			for(int j = 0; j < board.columns; j++){
				if(board.capturingMoveAvailable(board.array[i][j])){
					//count += numberOfMoves2(i,j,false,c,goal);
					String result = numberOfMoves2(i,j,false,c,goal);
					boolean number = true;
					for(int k = 0; k < result.length(); k++){
						if(!Character.isDigit(result.charAt(k))){
							number = false;
						}
					}
					if(number){
						count += Integer.parseInt(result);
						cc+=Integer.parseInt(result);
					}
					else if(cc == goal){
						return result;
					}
				}
				else if(!capturingMoveAvailable()){
					String possibleMoves = board.possibleMovesWithDirection(board.array[i][j]);
					//System.out.println("Possible moves: "+possibleMoves);
					int temp = 0;
					for(int k = 0; k < possibleMoves.length()/8; k++){
						//also generalize this code
						String move = possibleMoves.substring(temp+1,temp+8);
						count++;
						cc++;
						if(cc == goal) return move;
					}
				}
			}
		}
		return "Error";
	}
	
	
	private int getFirstRow(String move){
		//defined assuming standard 9x5 board
		return move.charAt(0)-48;
	}
	
	private int getFirstColumn(String move){
		//defined assuming standard 9x5 board
		
		return move.charAt(1)-48;
	}
	
	private int getSecondRow(String move){
		//defined assuming standard 9x5 board
		
		return move.charAt(3)-48;
	}
	
	private int getSecondColumn(String move){
		//defined assuming standard 9x5 board
		
		return move.charAt(4)-48;
	}
	
	private char getMoveType(String move){
		//defined assuming standard 9x5 board
		
		return move.charAt(6);
	}
	
	private boolean moveHasSuccessiveCaptures(String move){
		Fanorona moveTest = copyGame();
		Piece.Type original = this.activePlayer();
		moveTest.move(getFirstRow(move),getFirstColumn(move),getSecondRow(move),getSecondColumn(move),getMoveType(move));
		moveTest.board.activePlayer = original;
		if(original == Piece.Type.WHITE && moveTest.board.whiteMoves.endsWith("\n")){
			moveTest.board.whiteMoves = moveTest.board.whiteMoves.substring(0,moveTest.board.whiteMoves.length()-1);
		}
		else if(original == Piece.Type.BLACK && moveTest.board.blackMoves.endsWith("\n")){ 
			moveTest.board.blackMoves = moveTest.board.blackMoves.substring(0,moveTest.board.blackMoves.length()-1);
		}
		if(moveTest.board.capturingMoveAvailable(moveTest.board.array[getSecondRow(move)][getSecondColumn(move)])){
			return true;
		}
		else{
			return false;
		}
	}
	
	public int numberOfMoves(int row, int column, boolean recursion){
		int count = 0;
		if(this.board.capturingMoveAvailable(this.board.array[row][column])){
			String possibleMoves = this.board.PossibleCapturingMovesWithDirection(this.board.array[row][column]);
			//System.out.print("possible moves: "+possibleMoves);
			//System.out.println("    length: "+possibleMoves.length());
			int temp = 0;
			for(int k = 0; k < possibleMoves.length()/8; k++){
				String move = possibleMoves.substring(temp+1,temp+8);
				if(moveHasSuccessiveCaptures(move)){
					Fanorona newGame = copyGame();
					int tempRow = getSecondRow(move);	//hold the destination position so i can call recursively
					int tempCol = getSecondColumn(move);
					newGame.move(getFirstRow(move), getFirstColumn(move), getSecondRow(move), getSecondColumn(move), getMoveType(move));
					count += newGame.numberOfMoves(tempRow, tempCol, true);
				}
				else{
					count++;
				}
				temp+=8;
				/*TODO make this code more general. now it just does the same thing as count += length/8 */
			}
		}
		else if(!recursion){
			String possibleMoves = this.board.possibleMovesWithDirection(this.board.array[row][column]);
			//System.out.println("Possible moves: "+possibleMoves);
			for(int k = 0; k < possibleMoves.length()/8; k++){
				//also generalize this code
				count++;
			}
		}
		return count;
	}
	
	public int numberOfMoves(){
		int count = 0;
		for(int i = 0; i < board.rows; i++){
			for(int j = 0; j < board.columns; j++){
				if(board.capturingMoveAvailable(board.array[i][j])){
					count += numberOfMoves(i,j,false);
				}
				else if(!capturingMoveAvailable()){
					String possibleMoves = board.possibleMovesWithDirection(board.array[i][j]);
					//System.out.println("Possible moves: "+possibleMoves);
					for(int k = 0; k < possibleMoves.length()/8; k++){
						//also generalize this code
						count++;
					}
				}
			}
		}
		return count;
	}
	
	//Calculates the total number of possible moves (state changes)
	public void recalculateNumberOfMoves(){
		numberOfPossibleMoves = numberOfMoves();
		lastEvaluated = 0;
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
			//System.out.println("valid");
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
				//System.out.println("Successive capture available");
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
		return getState(0);
		//return null;
	}
}