import java.util.ArrayList;

public class MiniMaxTree {
	Node root;
	
	public class Node {
		Node parent;
		boolean isMaximizeNode;
		Evaluatable state;
		int utilityValue = 0;
		ArrayList<Node> children;
		int alpha;
		int beta;
		
		Node(Node _parent,
				Evaluatable _state,
				boolean _isMaximizeNode,
				int _alphaValue,
				int _betaValue) {
			parent = _parent;
			state = _state;
			isMaximizeNode = _isMaximizeNode;
			if (_isMaximizeNode) {
				utilityValue = -1000000;
			} else {
				utilityValue = 1000000;
			}
		}
		
		Node getNextChild() {
			Evaluatable tempState = state.getNextState();
			if (tempState == null) {
				return null;
			}
			int tempAlpha;
			int tempBeta;
			if (isMaximizeNode) {
				tempAlpha = utilityValue;
				tempBeta = beta;
			} else {
				tempAlpha = alpha;
				tempBeta = utilityValue;
			}
			Node tempNode = new Node(this,
										tempState,
										!(isMaximizeNode),
										tempAlpha,
										tempBeta);
			children.add(tempNode);
			return tempNode;
		}
		
		int generateUtilityValue() {
			utilityValue = state.evaluate();
			return utilityValue;
		}
		
		int getUtilityValue() {
			return utilityValue;
		}
		
		void setUtilityValue(int newValue) {
			utilityValue = newValue;
		}
		
		boolean setNewUtilityValueIfBetter(int newValue) {
			if (isMaximizeNode) {
				if (newValue > utilityValue) {
					utilityValue = newValue;
					return true;
				}
				return false;
			} else {
				if (newValue < utilityValue) {
					utilityValue = newValue;
					return true;
				}
				return false;
			}
		}
		
		boolean isMaximizerNode() {
			return isMaximizeNode;
		}
		
		Node getParent() {
			return parent;
		}
		
		int getAlpha() {
			return alpha;
		}

		void setAlpha(int alpha) {
			this.alpha = alpha;
		}

		int getBeta() {
			return beta;
		}

		void setBeta(int beta) {
			this.beta = beta;
		}
		
		Evaluatable getState() {
			return state;
		}
		
	}
	
	MiniMaxTree(Evaluatable initialState) {
		/* TODO How to determine if first row is max or min? */
		root = new Node(null, initialState, true, -1000000, 1000000);
	}
	
	private void generateTree(Node node, int depth) {
		if (depth > 1) {
			/* Non-leaf nodes */
			Node p = node.getParent(); // For readability
			if ((p.isMaximizerNode() &&
					(p.getUtilityValue() > p.getBeta())) ||
				!(p.isMaximizerNode() &&
						(p.getUtilityValue() < p.getAlpha()))) {
				Node tempNode = node.getNextChild();
				if (tempNode != null) {
					generateTree(tempNode, depth-1);
				}
				/* TODO Should null be a special case? */
			}
		} else {
			/* Leaf nodes */
			int utilityValue = node.generateUtilityValue();
			node.parent.setNewUtilityValueIfBetter(utilityValue);
			generateTree(node.getParent(), depth+1);
		}
	}
	
	Evaluatable processToDepth(int howDeep) {
		generateTree(root, howDeep);
		return root.getState();
	}
}
