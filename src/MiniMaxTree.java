import java.util.ArrayList;

public class MiniMaxTree {
	Node root;
	
	public class Node {
		Node parent;
		boolean isMaximizeNode;
		Evaluatable state;
		int utilityValue = 0;
		ArrayList<Node> children;
		
		Node(Node _parent, Evaluatable _state, boolean _isMaximizeNode) {
			parent = _parent;
			state = _state;
			isMaximizeNode = _isMaximizeNode;
		}
		
		void createChildren() {
			ArrayList<Evaluatable> childStates = state.getSubsequentStates();
			for (Evaluatable i: childStates) {
				addChild(i);
			}
		}
		
		void addChild(Evaluatable childState) {
			children.add(new Node(this, childState, !(isMaximizeNode)));
		}
		
		void generateUtilityValue() {
			utilityValue = state.evaluate();
		}
	}
	
	MiniMaxTree(Evaluatable initialState) {
		/* TODO How to determine if first row is max or min? */
		root = new Node(null, initialState, true);
	}
	
	void processToDepth(int howDeep) {
		int depthCounter = howDeep;
		Node currentNode = root;
		/* Go until depthCounter = 1 */
		while (depthCounter != 1) {
			currentNode.createChildren();
			/* Not done */
		}
	}
}
