class Board{

	public int rows;
	public int columns;

	String whiteMoves;
	String blackMoves;

	public Piece.Type activePlayer;

	Piece[][] array;
	
	String latestDirectionMoved;

	//Returns the number of pieces of Type type on the board
	public int numberRemaining(Piece.Type type){
		int count = 0;
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < columns; j++){
				if(array[i][j].type == type){
					count++;
				}
			}
		}
		return count;
	}

	//prints out the number of pieces remaining for each player
	public void printScore(Piece.Type ty){
		if(ty == Piece.Type.WHITE){
			System.out.println("White score: "+numberRemaining(ty));
		}
		else if(ty == Piece.Type.BLACK){
			System.out.println("Black score: "+numberRemaining(ty));
		}
	}

	//Returns a deep copy of the board, preserving the current active player
	public Board copyBoard(){
		Board b;
		if(activePlayer == Piece.Type.WHITE){
			b = new Board(rows, columns, array, Piece.Type.WHITE);
		}
		else{
			b = new Board(rows, columns, array, Piece.Type.BLACK);
		}
		b.whiteMoves = this.whiteMoves;
		b.blackMoves = this.blackMoves;
		return b;
	}

	//Creates a new board and populates it with pieces
	//activePlayer is set to Type type
	public Board(int row, int col, Piece.Type type){

		rows = row;
		columns = col;
		whiteMoves = "";
		blackMoves = "";
		activePlayer = type;
		latestDirectionMoved = "";

		array = new Piece[rows][columns];

		for(int j = 0; j < rows; j++){
			boolean even = true;
			for(int i = 0; i < col; i++){
				if(j < 2){
					array[j][i] = new Piece(Piece.Type.BLACK,j,i);
				}
				else if(j == 2){
					if(i == col/2){
						array[j][i] = new Piece(Piece.Type.NULL,j,i);
						even = true;
					}
					else if(even){
						array[j][i] = new Piece(Piece.Type.BLACK,j,i);
						even = false;
					}
					else{
						array[j][i] = new Piece(Piece.Type.WHITE,j,i);
						even = true;
					}
				}
				else{
					array[j][i] = new Piece(Piece.Type.WHITE,j,i);
				}
			}
		}
	}

	//Creates a new Board object and populates it with pieces of the same type as arr
	public Board(int row, int col, Piece[][] arr, Piece.Type type){

		rows = row;
		columns = col;
		whiteMoves = "";
		blackMoves = "";
		activePlayer = type;
		latestDirectionMoved = "";

		array = new Piece[rows][columns];
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < columns; j++){
				array[i][j] = new Piece(arr[i][j]);
			}
		}
	}

	//
	boolean movePiece(int row, int col, int row2, int col2, char type){

		if(array[row2][col2].type != Piece.Type.NULL){
			return false;
		}

		String direction = getDirection(row,col,row2,col2);
		
		if(direction == latestDirectionMoved){
			latestDirectionMoved = "";
			return false;
		}
		else{
			latestDirectionMoved = direction;
		}

		Piece.Type other;

		if(activePlayer == Piece.Type.WHITE){
			other = Piece.Type.BLACK;
		}
		else{
			other = Piece.Type.WHITE;
		}

		int temprow = row;
		int tempcol = col;		//these temp variables are used to hold the values of previous iterations through the following loop
		int temprow2 = row2;
		int tempcol2 = col2;

		if(type == 'a'){
			int count = 0;
			while((temprow2 != -1 && tempcol2 != -1) &&(temprow2 < rows && tempcol2 < columns) ){

				int nextrow = (temprow2-temprow)+temprow2;
				int nextcol = (tempcol2-tempcol)+tempcol2;

				temprow = temprow2;
				tempcol = tempcol2;

				count++;
				if(array[temprow2][tempcol2].type == other){
					array[temprow2][tempcol2].setType(Piece.Type.NULL);
				}
				else if(count>1){
					break;
				}

				temprow2 = nextrow;
				tempcol2 = nextcol;
			}
		}
		else if(type == 'w'){
			int count = 0;
			while((temprow != -1 && tempcol != -1) && (temprow < rows && tempcol < columns)){

				int nextrow = (temprow-temprow2)+temprow;
				int nextcol = (tempcol-tempcol2)+tempcol;

				int ttr = temprow;
				int ttc = tempcol;

				count++;
				if(array[temprow][tempcol].type == other){
					array[temprow][tempcol].setType(Piece.Type.NULL);
				}
				else if(count>1){
					break;
				}

				temprow = nextrow;
				tempcol = nextcol;

				temprow2 = ttr;
				tempcol2 = ttc;

			}
		}

		else if(type == 'f'){

		}

		array[row2][col2].setType(activePlayer);
		array[row][col].setType(Piece.Type.NULL);

		return true;
	}

	String connectedSpaces(Piece p){
		
		int row = p.row;
		int col = p.column;
		String s = "";

		if(row != rows-1){
			s+=" "+(row+1)+(col);
		}
		if(row != 0){
			s+=" "+(row-1)+(col);
		}
		if(col != columns-1){
			s+=" "+(row)+(col+1);
		}
		if(col != 0){
			s+=" "+(row)+(col-1);
		}

		if(p.stronglyConnected){
			if(row != 0 && row != rows-1){
				if(col != columns-1 && col != 0){
					s+=" "+(row+1)+(col+1);
					s+=" "+(row-1)+(col+1);
					s+=" "+(row-1)+(col-1);
					s+=" "+(row+1)+(col-1);
				}
				else if(col == 0){
					s+=" "+(row+1)+(col+1);
					s+=" "+(row-1)+(col+1);
				}
				else if(col == columns-1){
					s+=" "+(row-1)+(col-1);
					s+=" "+(row+1)+(col-1);
				}
			}
			else if(row == 0){
				if(col == 0){
					s+=" "+(row+1)+(col+1);
				}
				else if(col == columns-1){
					s+=" "+(row+1)+(col-1);
				}
				else{
					s+=" "+(row+1)+(col-1);
					s+=" "+(row+1)+(col+1);
				}
			}
			else{
				if(col == 0){
					s+=" "+(row-1)+(col+1);
				}
				else if(col == columns-1){
					s+=" "+(row-1)+(col-1);
				}
				else{
					s+=" "+(row-1)+(col-1);
					s+=" "+(row-1)+(col+1);
				}
			}
		}
		return s;
	}

	//Determines whether or not a move is allowable (if the original position is of type
	//activePlayer, and the second position is null)
	//public boolean isPossibleMove(int _row, int _col,String s, char type){
	public boolean isPossibleMove(int _row, int _col,int row2, int col2, char type){

		//if(type != 'f'){
		//	return isPossibleCapturingMove(_row,_col,row2,col2,type);
		//}
		
		//if(capturingMoveAvailable()){
		//	return false;
				
		if(array[_row][_col].type != activePlayer){
			return false;
		}

		if(array[row2][col2].type == Piece.Type.NULL){
			return true;
		}

		return false;
	}

	public boolean isPossibleCapturingMove(int _row, int _col, int row2, int col2,char type){

		boolean successive = false;
		int prow = 0;
		int pcol = 0;

		String visitedList = "";

		String activeMoves;
		if(activePlayer == Piece.Type.WHITE){
			activeMoves = whiteMoves;
		}
		else{
			activeMoves = blackMoves;
		}

		if(activeMoves.endsWith("\n") || activeMoves.length() == 0){
			//this is the first move of black's turn, any piece can be moved
			//leave successive as false
		}
		else{
			prow = activeMoves.charAt(activeMoves.length()-2)-48;
			pcol = activeMoves.charAt(activeMoves.length()-1)-48; //representing the origin row and column
			successive = true;
			
			int newlineIndex = activeMoves.lastIndexOf('\n');
			if(newlineIndex == -1){
				//'\n' not found, so set the first index to 0
			//	newlineIndex = 0;
			}
			newlineIndex++;
			String movesThisTurn = activeMoves.substring(newlineIndex, activeMoves.length());
			int i = 0;
			while(i < movesThisTurn.length()){
				if(movesThisTurn.charAt(i) != '>'){
					visitedList += movesThisTurn.charAt(i);
				}
				else if(i != movesThisTurn.length()-3){
					i+=2; //found a '>', skip ahead 2 spaces
				}
				i++;
			}
		}
		
		String direction = getDirection(_row, _col,row2,col2);
		
		if(successive && direction == latestDirectionMoved){
			return false;
		}


		//System.out.println("Visited moves: "+visitedList);

		if(successive && (prow != _row || pcol != _col)){
			//System.out.println("returning false");
			return false;
		}

		if(successive){
		//	System.out.println("row2: "+row2+"  col2: "+col2);
			for(int i = 0; i < visitedList.length()-1; i+=2){
		//		System.out.println("i: "+visitedList.charAt(i)+"  i2: "+visitedList.charAt(i+1));
				if(row2 == visitedList.charAt(i)-48 && col2 == visitedList.charAt(i+1)-48){
			//		System.out.println("returning false2");
					return false;
				}
			}
		}

		Piece.Type other;
		if(activePlayer == Piece.Type.WHITE){
			other = Piece.Type.BLACK;
		}
		else{
			other = Piece.Type.WHITE;
		}

		Board tester = copyBoard();
		int temp = tester.numberRemaining(other);

		//tester.prettyprint();
		//System.out.println(s);

		boolean valid = tester.movePiece(_row,_col,row2,col2,type);
		if(!valid){
			return false;
		}
		int temp2 = tester.numberRemaining(other);
		//System.out.println("temp: "+temp+"temp2: "+temp2);

		if(temp2 < temp) return true;
		return false;
	}

	boolean capturingMoveAvailable(Piece p){
		String latestMove = "";
		if(activePlayer == Piece.Type.WHITE && whiteMoves.length() != 0){
			int temp = whiteMoves.lastIndexOf("\n");
			if(temp != -1)
				latestMove = whiteMoves.substring(temp);
		}
		else if(activePlayer == Piece.Type.BLACK && blackMoves.length() != 0){
			int temp = blackMoves.lastIndexOf("\n");
			if(temp != -1)
				latestMove = blackMoves.substring(temp);
		}

		String s = possibleMoves(p);
		int length = s.length()/3;
		for(int k = 0; k < length; k++){
			int row = (int) s.charAt(1) -48;
			int col = (int) s.charAt(2) -48;
			s = s.substring(3);
			if(isPossibleCapturingMove(p.row,p.column,row,col,'a')){
			//	System.out.println(p.row+" "+p.column+" "+move);
				return true;
			}
			if(isPossibleCapturingMove(p.row,p.column,row,col,'w')) return true;
		}
		return false;
	}

	boolean capturingMoveAvailable(){
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < columns; j++){
				if(array[i][j].type == activePlayer){
					String s = possibleMoves(array[i][j]);
					//System.out.println("possible moves from "+i+""+j+": "+s);
					int length = s.length()/3;
					for(int k = 0; k < length; k++){
						int row = (int) s.charAt(1) -48;
						int col = (int) s.charAt(2) -48;
						s = s.substring(3);
						String move = ""+row+col;
						//System.out.println("testing "+move);
						if(isPossibleCapturingMove(i,j,row,col,'a')) return true;
						if(isPossibleCapturingMove(i,j,row,col,'w')) return true;
					}
				}
			}
		}

		return false;
	}

	String possibleMoves(Piece p){
		String connected = connectedSpaces(p);

		String possible = "";

		int temp = 1;
		for(int i = 0; i < connected.length()/3;i++){
			String move = ""+connected.charAt(temp)+connected.charAt(temp+1);
			if(isPossibleMove(p.row, p.column,connected.charAt(temp)-48, connected.charAt(temp+1)-48,' ')){
				possible+=" "+move;
			}
			temp+=3;
		}
		return possible;
	}
	
	String possibleMovesWithDirection(Piece p){
		String connected = connectedSpaces(p);

		String possible = "";

		int temp = 1;
		for(int i = 0; i < connected.length()/3;i++){
			String move = ""+connected.charAt(temp)+connected.charAt(temp+1);
			if(isPossibleMove(p.row, p.column,connected.charAt(temp)-48, connected.charAt(temp+1)-48,' ')){
				possible+=" "+p.row+""+p.column+" "+move+" f";
			}
			temp+=3;
		}
		return possible;
	}

	public String PossibleCapturingMoves(Piece p){
		
		String connected = possibleMoves(p);

		String possiblecapt = "";
		int temp = 1;
		for(int i = 0; i < connected.length()/3; i++){
			String move = ""+connected.charAt(temp)+connected.charAt(temp+1);
			int row = connected.charAt(temp)-48;
			int col = connected.charAt(temp+1)-48;
			temp += 3;

			if(isPossibleCapturingMove(p.row, p.column,row,col,'a')){
				possiblecapt+=" "+move;
			}

			if(isPossibleCapturingMove(p.row, p.column,row,col,'w')){
				possiblecapt+=" "+move;
			}
		}
		return possiblecapt;
	}
	
	public String PossibleCapturingMovesWithDirection(Piece p){
		
		String connected = possibleMoves(p);

		String possiblecapt = "";
		int temp = 1;
		for(int i = 0; i < connected.length()/3; i++){
			String move = ""+connected.charAt(temp)+connected.charAt(temp+1);
			int row = connected.charAt(temp)-48;
			int col = connected.charAt(temp+1)-48;
			temp += 3;

			if(isPossibleCapturingMove(p.row, p.column,row,col,'a')){
				possiblecapt+=" "+p.row+""+p.column+" "+move+" a";
			}

			if(isPossibleCapturingMove(p.row, p.column,row,col,'w')){
				possiblecapt+=" "+p.row+""+p.column+" "+move+" w";
			}
		}
		return possiblecapt;
	}
	
	public String getDirection(int _row, int _col, int row2, int col2){
		String direction = "";
		if(_row == row2){
			if(_col < col2) direction = "E";
			else direction = "W";
		}
		else if(_col == col2){
			if(_row < row2) direction = "S";
			else direction = "N";
		}
		else if(_row < row2){
			if(_col < col2) direction = "NE";
			else direction = "NW";
		}
		else if(_row > row2){
			if(_col < col2) direction = "SE";
			else direction = "SW";
		}	
		return direction;
	}


	public void prettyprint(){

		String line2 = "--------------------------------------------";

		int t = 0;
		for(int i = 0; i < columns; i++){
			System.out.print(i+"    ");
		}
		System.out.println();
		char c = 'A';
		for(int i = 0; i < columns; i++){
			System.out.print(c + "    ");
			c++;
		}
		System.out.println("\n"+line2);
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < columns; j++){
				if(array[i][j].type == Piece.Type.BLACK){
					System.out.print("X");
				}
				if(array[i][j].type == Piece.Type.WHITE){
					System.out.print("O");
				}
				if(array[i][j].type == Piece.Type.NULL){
					//System.out.print("N");
					System.out.print(" ");
				}
				if(j != columns-1){
					System.out.print("----");
				}
			}
			System.out.print("  | "+i);
			System.out.print("\n");
			String line = "";
			line += "| ";
			if(i != rows-1){
				for(int j = 0; j < columns; j++){
					if(j%2 == 0 && i != rows-1 && j != columns-1 && i%2 == 0){
						line += "\\  ";
					}
					else if(j%2 == 1 && i != rows-1 && j != columns-1 && i%2 == 1){
					 	line += "\\  ";
					}
					else if(j%2 == 1 && i != rows-1 && j != columns-1 && i%2 == 0){
					 	line += "/  ";
					}
					else if(j%2 == 0 && i != rows-1 && j != columns-1 && i%2 == 1){
					 	line += "/  ";
					}
					else{
					}
					if(j != columns -1) line += "| ";
				}
				System.out.println(line+" |");
			}
		}
		System.out.println();
		System.out.println("Active player: "+activePlayer);
		System.out.println();
	}
}