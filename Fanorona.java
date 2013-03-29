/*

	Fanorona game

	Constructor takes two arguments, the number of rows and columns



	Possible input format:
		[row][column] [row2][column2] [w/a/f]

		Where [row][column] is the piece to move, [row2][column2] is the place to move it, and
		[w/a/f] indicates whether this move is to be a withdrawal capture, approach capture, or capture-free move

	If a capturing move is possible, a capturing move must be taken

*/

/* TODO handle cases where no move is possible */



class Fanorona implements Evaluatable{

	public Board board;

	long player1Time;
	long player2Time;
	
	int numberOfPossibleMoves;
	int lastEvaluated;
	int numberOfTurns;
	
	int lastStateReturned;
	
	String lastStringReturned;
	
	static int cc;
	
	static String tempMove;

	Fanorona(int row, int col){
		board = new Board(row, col,Piece.Type.WHITE);
		player1Time = 0;
		player2Time = 0;
		lastStateReturned = 0;
		numberOfTurns = 0;
	}
	
	public void setCC(int val){
		cc = val;
	}
	
	Fanorona(int row, int col, Board b, Piece.Type active){
		board = new Board(row,col,b.array,active);
		player1Time = 0;
		player2Time = 0;
		numberOfTurns = 0;
	}
	
	private Fanorona getState(int n){
		String move = numberOfMoves3(n);
		//System.out.print("to next state: "+move+"  evaluates to: ");
		int temp = 0;
		Fanorona newState = copyGame();
		
		while(move.length() > 0){
			int index = move.indexOf(' ');
			char type = (move.substring(0, index)).charAt(0);
			move = move.substring(index+1);
			index = move.indexOf(' ');
			int row1 = Integer.parseInt(move.substring(0,index));
			move = move.substring(index+1);
			index = move.indexOf(' ');
			int col1 = Integer.parseInt(move.substring(0,index));
			move = move.substring(index+1);
			index = move.indexOf(' ');
			int row2 = Integer.parseInt(move.substring(0,index));
			move = move.substring(index+1);
			index = move.indexOf('>');
			int col2;
			if(index == -1){
				col2 = Integer.parseInt(move);
				move = "";
			}
			else{
				col2 = Integer.parseInt(move.substring(0,index));
				move = move.substring(index+1);
			}
			
			newState.move(row1, col1, row2, col2, type);
		}
		
		
		
//		for(int i = 0; i < move.length()/7; i++){
//			String move2 = move.substring(temp,temp+7);
//			temp+=8;
//			newState.move(getFirstRow(move2),getFirstColumn(move2),getSecondRow(move2),getSecondColumn(move2),getMoveType(move2));
//		}
		
		
		
		
		
		return newState;
	}
	
	/*
	 * New move input format (same as described in the new requirements paper)
	 * 
	 * A/H/P row1 col1 row2 col2
	 * S row1 col1
	 * 
	 */
	
	
	public static int getFirstRowCMD(String move){
		
		move = move.substring(2);
		int count = 0;
		String row = "";
		for(int i = 0; i < move.length(); i++){
			if( count == 1 && Character.isDigit(move.charAt(i))){
				row += move.charAt(i);
			}
			else if(move.charAt(i) == ' '){
				count++;
			}
		}

		return Integer.parseInt(row);
	}
	
	public static int getFirstColumnCMD(String move){
		
		move = move.substring(2);
		int count = 0;
		String row = "";
		for(int i = 0; i < move.length(); i++){
			if( count == 0 && Character.isDigit(move.charAt(i))){
				row += move.charAt(i);
			}
			else if(move.charAt(i) == ' '){
				count++;
			}
		}

		return Integer.parseInt(row);
	}
	
	public static int getSecondRowCMD(String move){
		
		move = move.substring(2);
		int count = 0;
		String row = "";
		for(int i = 0; i < move.length(); i++){
			if( count == 3 && Character.isDigit(move.charAt(i))){
				row += move.charAt(i);
			}
			else if(move.charAt(i) == ' '){
				count++;
			}
		}

		return Integer.parseInt(row);
	}
	
	public static int getSecondColumnCMD(String move){
		
		move = move.substring(2);
		int count = 0;
		String row = "";
		for(int i = 0; i < move.length(); i++){
			if( count == 2 && Character.isDigit(move.charAt(i))){
				row += move.charAt(i);
			}
			else if(move.charAt(i) == ' '){
				count++;
			}
		}

		return Integer.parseInt(row);
	}
	
	
	
	public static int getFirstRow(String move){
		
		move = move.substring(2);
		int count = 0;
		String row = "";
		for(int i = 0; i < move.length(); i++){
			if( count == 0 && Character.isDigit(move.charAt(i))){
				row += move.charAt(i);
			}
			else if(move.charAt(i) == ' '){
				count++;
			}
		}

		return Integer.parseInt(row);
	}
	
	public static int getFirstColumn(String move){
		
		move = move.substring(2);
		int count = 0;
		String row = "";
		for(int i = 0; i < move.length(); i++){
			if( count == 1 && Character.isDigit(move.charAt(i))){
				row += move.charAt(i);
			}
			else if(move.charAt(i) == ' '){
				count++;
			}
		}

		return Integer.parseInt(row);
	}
	
	public static int getSecondRow(String move){
		
		move = move.substring(2);
		int count = 0;
		String row = "";
		for(int i = 0; i < move.length(); i++){
			if( count == 2 && Character.isDigit(move.charAt(i))){
				row += move.charAt(i);
			}
			else if(move.charAt(i) == ' '){
				count++;
			}
		}

		return Integer.parseInt(row);
	}
	
	public static int getSecondColumn(String move){
		
		move = move.substring(2);
		int count = 0;
		String row = "";
		for(int i = 0; i < move.length(); i++){
			if( count == 3 && Character.isDigit(move.charAt(i))){
				row += move.charAt(i);
			}
			else if(move.charAt(i) == ' '){
				count++;
			}
		}

		return Integer.parseInt(row);
	}
	
	public static char getMoveType(String move){
		
		char t = move.charAt(0);
		if(!(t == 'A' || t == 'W' || t == 'P' || t == 'S' || t == 'F' || t == 'N')){
			throw new RuntimeException("Invalid type: "+t);
		}
		return t;
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
	
	
	public String numberOfMoves3(int row, int column, boolean recursion, int goal, String tempString){
		int count = 0;
		if(this.board.capturingMoveAvailable(this.board.array[row][column])){
			String possibleMoves = this.board.PossibleCapturingMovesWithDirection(this.board.array[row][column]);
			int temp = 0;
			
			while(possibleMoves.length() > 0){
				int row1 = getFirstRow(possibleMoves);
				int col1 = getFirstColumn(possibleMoves);
				int row2 = getSecondRow(possibleMoves);
				int col2 = getSecondColumn(possibleMoves);
				char type = getMoveType(possibleMoves);
				String move = type+" "+row1+" "+col1+" "+row2+" "+col2;
				if(moveHasSuccessiveCaptures(move)){
					if(tempString.length() == 0){
						tempString += move;
					}
					else{
						tempString += ">"+move;
					}
					Fanorona newGame = copyGame();
					int tempRow = getSecondRow(move);	//hold the destination position so i can call recursively
					int tempCol = getSecondColumn(move);
					newGame.move(getFirstRow(move), getFirstColumn(move), getSecondRow(move), getSecondColumn(move), getMoveType(move));
					String tempCount = newGame.numberOfMoves3(tempRow, tempCol, true,goal,tempString);
					tempString = tempCount;
					if(cc == goal) return tempString;
				}
				else{
					count++;
					cc++;
					if(cc == goal){
						if(tempString.length() == 0){
							return move;
						}
						else{
							return tempString + ">"+move;
						}
					}
					else{
						int index = possibleMoves.indexOf(',');
						if(index == -1 || possibleMoves.substring(index+1).length() < 2){
							possibleMoves = "";
						}
						else{
							possibleMoves = possibleMoves.substring(index+2);
						}
					}
				}
			}
		}		
			
		else if(!recursion){
			String possibleMoves = this.board.possibleMovesWithDirection(this.board.array[row][column]);
			possibleMoves = possibleMoves.substring(1);
			
			while(possibleMoves.length() > 0){
				int row1 = getFirstRow(possibleMoves);
				int col1 = getFirstColumn(possibleMoves);
				int row2 = getSecondRow(possibleMoves);
				int col2 = getSecondColumn(possibleMoves);
				char type = getMoveType(possibleMoves);
				String move = type+" "+row1+" "+col1+" "+row2+" "+col2;
				int index = possibleMoves.indexOf(',');
				possibleMoves = possibleMoves.substring(index+1);
				count++;
				cc++;
				if(cc == goal) return move;
				
			}
		}
		return "";
	}
	
	public String numberOfMoves3(int goal){
		cc = 0;
		int count = 0;
		String eMove = "";
		for(int i = 0; i < board.rows; i++){
			for(int j = 0; j < board.columns; j++){
				eMove = "";
				tempMove = "";
				if(board.capturingMoveAvailable(board.array[i][j])){
					eMove = numberOfMoves3(i,j,false,goal,eMove);
					if(cc == goal) return eMove;
				}
				else if(!capturingMoveAvailable()){

					String possibleMoves = board.possibleMovesWithDirection(board.array[i][j]);
					
					while(possibleMoves.length() > 0){
						int row1 = getFirstRow(possibleMoves);
						int col1 = getFirstColumn(possibleMoves);
						int row2 = getSecondRow(possibleMoves);
						int col2 = getSecondColumn(possibleMoves);
						char type = getMoveType(possibleMoves);
						String move = type+" "+row1+" "+col1+" "+row2+" "+col2;
						int index = possibleMoves.indexOf(',');
						possibleMoves = possibleMoves.substring(index+1);
						count++;
						cc++;
						if(cc == goal) return move;
					}
				}
			}
		}
		return tempMove;
	}
	
	public int numberOfMoves(int row, int column, boolean recursion){
		int count = 0;
		if(this.board.capturingMoveAvailable(this.board.array[row][column])){
			String possibleMoves = this.board.PossibleCapturingMovesWithDirection(this.board.array[row][column]);
			int temp = 0;
			
			while(possibleMoves.length() > 0){
				int row1 = getFirstRow(possibleMoves);
				int col1 = getFirstColumn(possibleMoves);
				int row2 = getSecondRow(possibleMoves);
				int col2 = getSecondColumn(possibleMoves);
				char type = getMoveType(possibleMoves);
				String move = type+" "+row1+" "+col1+" "+row2+" "+col2;
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
				int index = possibleMoves.indexOf(',');
				if(possibleMoves.length() < index+2){
					possibleMoves = "";
				}
				else{
					possibleMoves = possibleMoves.substring(index+2);
				}
			}
		}
		else if(!recursion){
			String possibleMoves = this.board.possibleMovesWithDirection(this.board.array[row][column]);
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
					/* TODO fix this loop for paika moves */
					String possibleMoves = board.possibleMovesWithDirection(board.array[i][j]);
					/* TODO look into this code for possible bug related to paika moves */
					if(possibleMoves.length() > 0){
						possibleMoves = possibleMoves.substring(1);
					}
					
					while(possibleMoves.length() > 0){
						int row1 = getFirstRow(possibleMoves);
						int col1 = getFirstColumn(possibleMoves);
						int row2 = getSecondRow(possibleMoves);
						int col2 = getSecondColumn(possibleMoves);
						char type = getMoveType(possibleMoves);
						String move = type+" "+row1+" "+col1+" "+row2+" "+col2;
						int index = possibleMoves.indexOf(',');
						possibleMoves = possibleMoves.substring(index+1);
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
		Fanorona f = new Fanorona(board.rows, board.columns, board.copyBoard(), board.activePlayer);
		f.board.whiteMoves = this.board.whiteMoves;
		f.board.blackMoves = this.board.blackMoves;
		
		f.board.whiteMovesFull = this.board.whiteMovesFull;
		f.board.blackMovesFull = this.board.blackMovesFull;
		
		return f;
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
	
	public boolean move(String move){
		
		Piece.Type original = activePlayer();
		Piece.Type other;
		if(original == Piece.Type.WHITE){
			other = Piece.Type.BLACK;
		}
		else{
			other = Piece.Type.WHITE;
		}
		
		if(move.equals("N")){
			if(activePlayer() == Piece.Type.WHITE){
				board.whiteMovesFull += "N\n";
			}
			else{
				board.blackMovesFull += "N\n";
			}
			board.activePlayer = other;
			return false;
		}
		
		while(move.length() != 0){
			
			int row1;
			int col1;
			int row2;
			int col2;
			char moveType;
		
			row1 = getFirstRow(move);
			col1 = getFirstColumn(move);
			row2 = getSecondRow(move);
			col2 = getSecondColumn(move);
			moveType = getMoveType(move);
			
			int index = move.indexOf('+');
			if(index == -1){
				move = "";
			}
			else{
				move = move.substring(index+2);
			}
			
			move2(row1, col1, row2, col2, moveType);
			board.activePlayer = original;
		}
		
		if(activePlayer() == Piece.Type.WHITE){
			board.whiteMoves += "\n";
			board.whiteMovesFull += "\n";
		}
		else{
			board.blackMoves += "\n";
			board.blackMovesFull += "\n";
		}
		
		board.latestDirectionMoved = "";
		board.activePlayer = other;
		numberOfTurns++;
		return true;
	}

	//returns true if a successive capture is possible, false otherwise
	public boolean move(int row1, int col1, int row2, int col2, char type){

		boolean valid = false;
		if(capturingMoveAvailable() && board.isPossibleCapturingMove(row1,col1,row2,col2,type) || type == 'S'){
			valid = true;
		}
		else if(!capturingMoveAvailable() && board.isPossibleMove(row1,col1,row2,col2)){
			valid = true;
		}

		//check to see if move is valid
		if(valid){
			Piece.Type temp = board.activePlayer;
			board.movePiece(row1,col1,row2,col2,type);
			
			lastStateReturned = 0;
			board.activePlayer = temp;
			
			if(type == 'S'){
				if(board.activePlayer == Piece.Type.WHITE){
					board.whiteMoves += ""+row1+col1+">"+row2+col2;
					
					board.whiteMovesFull += type+" "+row1+" "+col1;
				}
				else{
					board.blackMoves += ""+row1+col1+">"+row2+col2;
					board.blackMovesFull += type+" "+row1+" "+col1;
				}
			}
			else{
				if(board.activePlayer == Piece.Type.WHITE){
					board.whiteMoves += ""+row1+col1+">"+row2+col2;
					if(board.whiteMovesFull.lastIndexOf('\n') != board.whiteMovesFull.length()-1 && board.whiteMovesFull.length()!=0){
						board.whiteMovesFull += " + ";
					}
					board.whiteMovesFull += type+" "+row1+" "+col1+" "+row2+" "+col2;
				}
				else{
					board.blackMoves += ""+row1+col1+">"+row2+col2;
					if(board.blackMovesFull.lastIndexOf('\n') != board.blackMovesFull.length()-1 && board.blackMovesFull.length()!=0){
						board.blackMovesFull += " + ";
					}
					board.blackMovesFull += type+" "+row1+" "+col1+" "+row2+" "+col2;
				}
			}
			
			if(board.capturingMoveAvailable(board.array[row2][col2]) && !(type == 'P' || type == 'S')){
				return true;
			}

			if(board.activePlayer == Piece.Type.WHITE){
				board.whiteMoves += "\n";
				board.whiteMovesFull += "\n";
			}
			else{
				board.blackMoves += "\n";
				board.blackMovesFull += "\n";
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
			//System.out.println("Invalid move entered");
		}
		return false;
	}
	
	public boolean move2(int row1, int col1, int row2, int col2, char type){

		boolean valid = false;
		if(capturingMoveAvailable() && board.isPossibleCapturingMove(row1,col1,row2,col2,type) || type == 'S'){
			valid = true;
		}
		else if(!capturingMoveAvailable() && board.isPossibleMove(row1,col1,row2,col2)){
			valid = true;
		}

		//check to see if move is valid
		if(valid){
			Piece.Type temp = board.activePlayer;
			board.movePiece(row1,col1,row2,col2,type);
			
			lastStateReturned = 0;
			board.activePlayer = temp;
			
			if(type == 'S'){
				if(board.activePlayer == Piece.Type.WHITE){
					board.whiteMoves += ""+row1+col1+">"+row2+col2;
					
					board.whiteMovesFull += type+" "+row1+" "+col1;
				}
				else{
					board.blackMoves += ""+row1+col1+">"+row2+col2;
					board.blackMovesFull += type+" "+row1+" "+col1;
				}
			}
			else{
				if(board.activePlayer == Piece.Type.WHITE){
					board.whiteMoves += ""+row1+col1+">"+row2+col2;
					if(board.whiteMovesFull.lastIndexOf('\n') != board.whiteMovesFull.length()-1 && board.whiteMovesFull.length()!=0){
						board.whiteMovesFull += " + ";
					}
					board.whiteMovesFull += type+" "+row1+" "+col1+" "+row2+" "+col2;
				}
				else{
					board.blackMoves += ""+row1+col1+">"+row2+col2;
					if(board.blackMovesFull.lastIndexOf('\n') != board.blackMovesFull.length()-1 && board.blackMovesFull.length()!=0){
						board.blackMovesFull += " + ";
					}
					board.blackMovesFull += type+" "+row1+" "+col1+" "+row2+" "+col2;
				}
			}
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
						if(board.isPossibleMove(p.row, p.column, move.charAt(1)-48, move.charAt(2)-48)){
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
		recalculateNumberOfMoves();
		int newlast = lastStateReturned+1;
		if(newlast > numberOfPossibleMoves){
			return null;
		}
		return getState(++lastStateReturned);
		//return null;
	}
	
	public String getMove(boolean type){
		
		String movesList;
		if(type){
			movesList = board.whiteMovesFull;
		}
		else{
			movesList = board.blackMovesFull;
		}
		
		int temp = movesList.lastIndexOf('\n');
		if(temp == -1){
			return movesList;
		}
		movesList = movesList.substring(0,temp);
		temp = movesList.lastIndexOf('\n');
		if(temp == -1){
			return movesList;
		}
		return movesList.substring(temp+1,movesList.length());
	}
	
	public String getLastNMoves(boolean type, int n){
		
		return "";
	}
	
	public void removeSacrifices(Piece.Type type){
		
		Piece.Type toRemove;
		if(type == Piece.Type.WHITE){
			toRemove = Piece.Type.WHITESACRIFICE;
		}
		else{
			toRemove = Piece.Type.BLACKSACRIFICE;
		}
		
		for(int i = 0; i < board.rows; i++){
			for(int j = 0; j < board.columns; j++){
				if(board.array[i][j].type == toRemove){
					board.array[i][j].setType(Piece.Type.NULL);
					break;
				}
			}
		}
	}
	
	public boolean validMoveSystax(String move){
		
		if(getMoveType(move) == 'N'){
			return true;
		}
		
		while(move.length() != 0){
			
			int row1;
			int col1;
			int row2;
			int col2;
			char moveType;
		
			try{
				row1 = getFirstRow(move);
				col1 = getFirstColumn(move);
				row2 = getSecondRow(move);
				col2 = getSecondColumn(move);
				moveType = getMoveType(move);
			}
			catch(Exception e){
				return false;
			}
			
			if(row1 > board.rows || row2 > board.rows){
				return false;
			}
			if(col1 > board.columns || col2 > board.columns){
				return false;
			}
			if(!(moveType == 'A' || moveType == 'W' || moveType == 'P' || moveType == 'S' || moveType == 'N')){
				return false;
			}
			
			int index = move.indexOf('+');
			if(index == -1){
				move = "";
			}
			else{
				move = move.substring(index+2);
				if(move.length() == 0) return false;
			}
		}
		return true;
	}
	
	//Takes a move as entered by the user and converts to its internal representation
	public String convertToInternalMove(String move){
		
		String internalMove = "";
		while(move.length() != 0){
			
			int row1;
			int col1;
			int row2;
			int col2;
			char moveType;
			
			moveType = Fanorona.getMoveType(move);
			if(moveType == 'S'){
				row2 = 0;
				col2 = 0;
			}
			else{
				row2 = board.rows-Fanorona.getSecondRowCMD(move);
	  			col2 = Fanorona.getSecondColumnCMD(move)-1;
			}
			row1 = board.rows-Fanorona.getFirstRowCMD(move);
  			col1 = Fanorona.getFirstColumnCMD(move)-1;
  			
  			int index = move.indexOf('+');
  			if(index == -1){
  				move = "";
  			}
  			else{
  				move = move.substring(index+2);
  			}
			
  			if(internalMove.length() == 0){
  				internalMove += moveType + " "+row1+" "+col1+" "+row2+" "+ col2;
  			}
  			else{
  				internalMove += " + "+ moveType + " "+row1+" "+col1+" "+row2+" "+ col2;
  			}
		}
		return internalMove;
	}
	
	String getAIMove(Piece.Type t){
		
		Piece.Type otherPlayer = t;
		
		MiniMaxTree mmt = new MiniMaxTree(copyGame());
		mmt.processToDepth(2);
		
		String bestMove;
		if(otherPlayer == Piece.Type.BLACK){
			bestMove = "";
			int minimum = 100000;
			for(int i = 0; i < mmt.root.children.size();i++){
				if(mmt.root.children.get(i).getUtilityValue() < minimum){
					bestMove = mmt.root.children.get(i).getState().getMove(false);
					minimum = mmt.root.children.get(i).getUtilityValue();
				}
			}
		}
		else{
			bestMove = "";
			int maximum = -100000;
			for(int i = 0; i < mmt.root.children.size();i++){
				if(mmt.root.children.get(i).getUtilityValue() > maximum){
					bestMove = mmt.root.children.get(i).getState().getMove(true);
					maximum = mmt.root.children.get(i).getUtilityValue();
				}
			}
		}
		
		
		return bestMove;
	}
	
	
	int checkEndGame(){
		if(board.numberRemaining(Piece.Type.WHITE) == 0){
			return 1;
		}
		else if(board.numberRemaining(Piece.Type.BLACK) == 0){
			return -1;
		}
		else if(numberOfTurns >= board.rows*10){
			return 2;
		}
		return 0;
	}
	
	boolean isPossibleCapturingMove(String move){
		
		Fanorona game = copyGame();
		Piece.Type other;
		if(game.activePlayer() == Piece.Type.WHITE){
			other = Piece.Type.BLACK;
		}
		else{
			other = Piece.Type.WHITE;
		}
		int temp1 = game.board.numberRemaining(other);
		game.move(move);
		int temp2 = game.board.numberRemaining(other);
		
		if(temp2 < temp1){
			return true;
		}
		else{
			return false;
		}
	}
}