import java.util.ArrayList;

public interface Evaluatable {
	int evaluate();
	ArrayList<Evaluatable> getSubsequentStates();
}
