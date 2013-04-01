import java.util.ArrayList;
import java.util.LinkedList;

public class MiniMaxTree {
	Node root;
	
	public class Node {
		Node parent;
		boolean isMaximizeNode;
		Evaluatable state;
		int utilityValue = 0;
		Node bestChildNode;
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

		boolean setNewUtilityValueIfBetter(int newValue, Node newNode) {
			if (isMaximizeNode) {
				if (newValue > utilityValue) {
					utilityValue = newValue;
					bestChildNode = newNode;
					return true;
				}
				return false;
			} else {
				if (newValue < utilityValue) {
					utilityValue = newValue;
					bestChildNode = newNode;
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
		
		Node getBestChildNode() {
			return bestChildNode;
		}

		Evaluatable getState() {
			return state;
		}
	}
	
	MiniMaxTree(Evaluatable initialState) {
		root = new Node(null, initialState, true, -1000000, 1000000);
	}

	private void genTree(Node node, int currentDepth, int depth, 
			boolean ignoreRemaining, long start, long limit) throws InterruptedException {
		
		long temp = System.currentTimeMillis();
		if((temp-start > limit-500 && limit != 0)){
			throw new InterruptedException();
		}
		
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
			genTree(node.getParent(), currentDepth + 1, depth, false,start,limit);
			return;
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
					node.getParent().setNewUtilityValueIfBetter(
							node.getUtilityValue(), node.getBestChildNode());
					genTree(node.getParent(), currentDepth + 1, depth, false,start, limit);
					return;
				}
			} else {
				genTree(childNode, currentDepth - 1, depth, false,start,limit);
				return;
			}
		} else { //currentDepth == 1
			node.generateUtilityValue();
			node.getParent().setNewUtilityValueIfBetter
			(node.generateUtilityValue(), node);
			if (node.getParent().isMaximizerNode() &&
					(node.getUtilityValue() > node.getBeta())) {
				/* If the parent is a maximizer node, and the utility value is
				 * higher than beta, ignore the remaining children of the
				 * parent */
				genTree(node.getParent(), currentDepth + 1, depth, true,start,limit);
				return;
			} else if (!(node.getParent().isMaximizerNode()) &&
					(node.getUtilityValue() < node.getAlpha())) {
				/* If the parent is a minimizer node, and the utility value is
				 * lower than alpha, ignore the remaining children of the
				 * parent. */
				genTree(node.getParent(), currentDepth + 1, depth, true,start,limit);
				return;
			} else {
				/* The remaining children are not ignored */
				genTree(node.getParent(), currentDepth + 1, depth, false,start,limit);
				return;
			}

		}
	}
	
	Evaluatable processToDepth(int howDeep,long start,long limit) throws InterruptedException {
		genTree(root, howDeep, howDeep, false,start,limit);
		return root.getState();
	}
	
	public LinkedList<Evaluatable> getPath() {
		LinkedList<Evaluatable> bestPath = new LinkedList<Evaluatable>();
		if (root.getBestChildNode() == null) {
			bestPath.add(root.getState());
			return bestPath;
		}
		Node tempNode = root.getBestChildNode();
		bestPath.addFirst(tempNode.getState());
		while (tempNode.getParent() != null) {
			bestPath.addFirst(tempNode.getParent().getState());
			tempNode = tempNode.getParent();
		}
		return bestPath;
	}
}
