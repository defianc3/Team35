class Main{
	public static void main(String args[]){

		System.out.println("\nX=black  0=white, n='null'(no piece)\n");


		// Board b = new Board(5,9);

		// b.prettyprint();

		// System.out.println("");

		// b.movePiece(3,4,2,4);

		// b.prettyprint();

		// System.out.println("");

		Fanorona game = new Fanorona(5,9);

		game.prettyprint();

		game.capturingMoveAvailable();

		game.board.isPossibleCapturingMove(3,4,"24");

		game.move(3,4,2,4,'a');

		game.capturingMoveAvailable();

		game.prettyprint();

		game.move(1,5,0,4,'w');

		game.prettyprint();

		game.move(2,4,1,5,'a');

		game.prettyprint();
	}
}