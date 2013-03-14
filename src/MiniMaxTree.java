import java.util.ArrayList;

public class MiniMaxTree<T extends Evaluatable> {
	Node root;
	
	public class Node {
		Node parent;
		T state;
		int utilityValue = 0;
		ArrayList<Node> children;
		
		Node(Node _parent, T _state) {
			parent = _parent;
			state = _state;
		}
		
		void addChild(Node _child) {
			children.add(_child);
		}
		
		void generateUtilityValue() {
			utilityValue = state.evaluate();
		}
	}
	
	MiniMaxTree(T state) {
		root = new Node(null, state);
	}
	
	/* TODO Needed? Or use lazy evaluation? */
	private void populateToDepth(int depth) {
		
	}
	
	void alphaCut() {
		
	}
	
	void betaCut() {
		
	}
}
