class Piece{

	public enum Type {WHITE, BLACK, NULL, WHITESACRIFICE, BLACKSACRIFICE};

	Type type;
	int row;
	int column;
	String possibleMoves;
	boolean stronglyConnected;

	public Piece(Type t, int _row, int _column){

		type = t;
		row = _row;
		column = _column;
		if(_row%2 == _column%2){
			stronglyConnected = true;
		}
		else{
			stronglyConnected = false;
		}
	}

	public void setType(Type t){
		type = t;
	}

	public Piece(Piece p){
		type = p.type;
		row = p.row;
		column = p.column;
		stronglyConnected = p.stronglyConnected;
		possibleMoves = p.possibleMoves;
	}
}