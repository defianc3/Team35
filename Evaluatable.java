public interface Evaluatable {
	/**
	 * @return A value representing the board state. Between -1000000 and
	 * 1000000.
	 */
	int evaluate();
	/**
	 * @return A valid next state for the game. Null if no more next states.
	 * Expected to be called repeatedly until there are no more states.
	 */
	Evaluatable getNextState();
}
