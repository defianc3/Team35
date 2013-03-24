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
			alpha = _alphaValue;
			beta = _betaValue;
			children = new ArrayList<Node>();
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

	private void genTree(Node node, int currentDepth, int depth, 
						 boolean ignoreRemaining) {
		if (depth == 1) {
			/* To handle the case where only the root node is processed */
			node.generateUtilityValue();
			return;
		}
		if ((currentDepth == depth) && ignoreRemaining) {
			return;
		}
		if (ignoreRemaining) {
			/* No need to process children */
			genTree(node.getParent(), currentDepth + 1, depth, false);
			return;
			/* TODO Should the above always be false? */
		}
		if (currentDepth > 1) {
			Node childNode = node.getNextChild();
			if (childNode == null) {
				if (node.getParent() == null) {
					return;
				}
				if (node.children.size() == 0)  {
					/* node contains a win or loss state */
					node.generateUtilityValue();
					/* Utility value should be +-1000000 */
				} else {
					/* Done processing children of node */
					/* Do something about that here */
					node.getParent().setNewUtilityValueIfBetter(
							node.getUtilityValue());
					genTree(node.getParent(), currentDepth + 1, depth, false);
					return;
				}
			} else {
				genTree(childNode, currentDepth - 1, depth, false);
				return;
			}
		} else { //currentDepth == 1
			node.generateUtilityValue();
			node.getParent().setNewUtilityValueIfBetter
							 (node.generateUtilityValue());
			if (node.getParent().isMaximizerNode() &&
					(node.getUtilityValue() > node.getBeta())) {
				/* If the parent is a maximizer node, and the utility value is
				 * higher than beta, ignore the remaining children of the
				 * parent */
				genTree(node.getParent(), currentDepth + 1, depth, true);
				return;
			} else if (!(node.getParent().isMaximizerNode()) &&
					(node.getUtilityValue() < node.getAlpha())) {
				/* If the parent is a minimizer node, and the utility value is
				 * lower than alpha, ignore the remaining children of the
				 * parent. */
				genTree(node.getParent(), currentDepth + 1, depth, true);
				return;
			} else {
				/* The remaining children are not ignored */
				genTree(node.getParent(), currentDepth + 1, depth, false);
				return;
			}
			
		}
	}
	
	Evaluatable processToDepth(int howDeep) {
		genTree(root, howDeep, howDeep, false);
		return root.getState();
	}
}
