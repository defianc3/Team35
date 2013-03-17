class Board{

	private int rows;
	private int columns;

	String whiteMoves;
	String blackMoves;

	public Piece.Type activePlayer;

	Piece[][] array;

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

	public int numberRemaining(Piece[][] arr, Piece.Type type){
		int count = 0;
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < columns; j++){
				if(arr[i][j].type == type){
					count++;
				}
			}
		}
		return count;
	}

	public Board copyBoard(){
		Board b;
		if(activePlayer == Piece.Type.WHITE){
			b = new Board(rows, columns, array, Piece.Type.WHITE);
		}
		else{
			b = new Board(rows, columns, array, Piece.Type.BLACK);
		}
		return b;
	}

	public Board(int row, int col, Piece.Type type){

		rows = row;
		columns = col;
		activePlayer = Piece.Type.WHITE;
		whiteMoves = "";
		blackMoves = "";
		activePlayer = type;

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

	public Board(int row, int col, Piece[][] arr, Piece.Type type){

		rows = row;
		columns = col;
		activePlayer = Piece.Type.WHITE;
		whiteMoves = "";
		blackMoves = "";
		activePlayer = type;

		array = new Piece[rows][columns];
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < columns; j++){
				array[i][j] = new Piece(arr[i][j]);
			}
		}
	}

	void movePiece(int row, int col, int row2, int col2, char type){

		String direction = "";
		if(row == row2){
			if(col < col2) direction = "E";
			else direction = "W";
		}
		else if(col == col2){
			if(row < row2) direction = "S";
			else direction = "N";
		}
		else if(row < row2){
			if(col < col2) direction = "NE";
			else direction = "NW";
		}
		else if(row > row2){
			if(col < col2) direction = "SE";
			else direction = "SW";
		}

		Piece.Type other;

		if(activePlayer == Piece.Type.WHITE){
			other = Piece.Type.BLACK;
		}
		else{
			other = Piece.Type.WHITE;
		}

		int temprow = row;
		int tempcol = col;

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

		//consider what to set active player to in the case th1at the current player can make a successive capture

		if(activePlayer == Piece.Type.WHITE){
			activePlayer = Piece.Type.BLACK;
		}
		else{
			activePlayer = Piece.Type.WHITE;
		}

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

	public boolean isPossibleMove(int _row, int _col,String s, char type){

		int row = (int) s.charAt(0) -48;
		int col = (int) s.charAt(1) -48;

		if(array[_row][_col].type != activePlayer){
			return false;
		}

		Piece.Type t = array[_row][_col].type;
		//System.out.println("row: "+row+"  col: "+col);

		if(t == Piece.Type.WHITE){
			if(array[row][col].type == Piece.Type.NULL){
				return true;
			}
		}
		if(t == Piece.Type.BLACK){
			if(array[row][col].type == Piece.Type.NULL){
				return true;
			}
		}
		return false;
	}

	public boolean isPossibleCapturingMove(int _row, int _col,String s,char type){

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

		int row = (int) s.charAt(0) -48;
		int col = (int) s.charAt(1) -48;

		tester.movePiece(_row,_col,row,col,type);
		int temp2 = tester.numberRemaining(other);

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
			String move = ""+row+col;
			if(isPossibleCapturingMove(p.row,p.column,move,'a')){
				System.out.println(p.row+" "+p.column+" "+move);
				return true;
			}
			if(isPossibleCapturingMove(p.row,p.column,move,'w')) return true;
		}
		return false;
	}

	boolean capturingMoveAvailable(){
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < columns; j++){
				if(array[i][j].type == activePlayer){
					String s = possibleMoves(array[i][j]);
					int length = s.length()/3;
					for(int k = 0; k < length; k++){
						int row = (int) s.charAt(1) -48;
						int col = (int) s.charAt(2) -48;
						s = s.substring(3);
						String move = ""+row+col;
						if(isPossibleCapturingMove(i,j,move,'a')) return true;
						if(isPossibleCapturingMove(i,j,move,'w')) return true;
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
			temp+=3;
			if(isPossibleMove(p.row, p.column,move,' ')){
				possible+=" "+move;
			}
		}
		return possible;
	}


	public void prettyprint(){

		String line2 = "--------------------------------------------";

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
					System.out.print("N");
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