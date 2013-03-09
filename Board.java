class Board{

	private int rows;
	private int columns;

	Piece[][] array;

	public Board(int row, int col){

		rows = row;
		columns = col;

		array = new Piece[rows][columns];

		for(int j = 0; j < rows; j++){
			boolean even = true;
			for(int i = 0; i < col; i++){
				if(j < 2){
					array[j][i] = new Piece(Piece.Type.BLACK);
				}
				else if(j == 2){
					if(i == col/2){
						array[j][i] = new Piece(Piece.Type.NULL);
						even = true;
					}
					else if(even){
						array[j][i] = new Piece(Piece.Type.BLACK);
						even = false;
					}
					else{
						array[j][i] = new Piece(Piece.Type.WHITE);
						even = true;
					}
				}
				else{
					array[j][i] = new Piece(Piece.Type.WHITE);
				}
			}
		}
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
	}
}