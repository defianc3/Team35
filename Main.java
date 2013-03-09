class Main{
	public static void main(String args[]){

		System.out.println("\nX=black  0=white, n='null'(no piece)\n");


		Board b = new Board(5,9);

		b.prettyprint();

		System.out.println("");
	}
}