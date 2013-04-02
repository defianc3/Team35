import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;


class TreeEval implements Evaluatable {
	
	ArrayList<Integer> childValues = new ArrayList<Integer>();
	private static int lastRetreived;
	int value;		

	TreeEval(int _value) {
		childValues.add(2);
		childValues.add(3);
		childValues.add(4);
		childValues.add(6);
		childValues.add(null);
		childValues.add(3);
		childValues.add(7);
		childValues.add(null);
		childValues.add(null); //Last node for simple tree
		/*childValues.add(10);
		childValues.add(null);
		childValues.add(null);
		childValues.add(null); */
		
		value = _value;
	}
	
	@Override
	public int evaluate() {
		return value;
	}

	@Override
	public Evaluatable getNextState() {
		Integer returnVal = childValues.get(lastRetreived);
		lastRetreived++;
		if (returnVal == null) {
			return null;
		} else {
			return new TreeEval(returnVal);
		}
	}

	@Override
	public String getMove(boolean type) {
		return null;
	}
	
}

public class MiniMaxTreeTest {
	
	
	class TestEval implements Evaluatable {
		
		int initialVal;
		int val;
		int addition = 1;
		
		public TestEval(int _val) {
			initialVal = _val;
			val = _val;
		}
		
		public int evaluate() {
			return val;
		}
		
		public TestEval getNextState() {
			if (addition < 3) {
				TestEval t = new TestEval(val + addition);
				addition += 1;
				return t;
			} else {
				return null;
			}
		}

		@Override
		public String getMove(boolean type) {
			return null;
		}
	}
	

		
	MiniMaxTree.Node rootNode;
	Evaluatable state;
	MiniMaxTree mMT;
	
	@Before
	public void setUp() throws Exception {
		TestEval t = new TestEval(1);
		state = t;
		mMT = new MiniMaxTree(t);
		rootNode = mMT.new Node(null, t, true, -1000000, 1000000);
	}
	
	@Test
	public void createNode() {
		assertEquals(null, rootNode.getParent());
		assertEquals(-1000000, rootNode.getAlpha());
		assertEquals(1000000, rootNode.getBeta());
		assertEquals(true, rootNode.isMaximizerNode());
		assertEquals(-1000000, rootNode.getUtilityValue());
		//assertEquals(null, rootNode.getNextChild());
	}
	
	@Test
	public void nodeSetter() {
		rootNode.setAlpha(10);
		assertEquals(10, rootNode.getAlpha());
		rootNode.setBeta(0);
		assertEquals(0, rootNode.getBeta());
		rootNode.setUtilityValue(1000);
		assertEquals(1000, rootNode.getUtilityValue());
	}
	
	@Test
	public void maxUtilitySet() {
		assertEquals(-1000000, rootNode.getUtilityValue());
		rootNode.setNewUtilityValueIfBetter(-1000001, rootNode);
		assertEquals(-1000000, rootNode.getUtilityValue());
		rootNode.setNewUtilityValueIfBetter(-1000000, rootNode);
		assertEquals(-1000000, rootNode.getUtilityValue());
		rootNode.setNewUtilityValueIfBetter(-999999, rootNode);
		assertEquals(-999999, rootNode.getUtilityValue());
		rootNode.setNewUtilityValueIfBetter(0, rootNode);
		assertEquals(0, rootNode.getUtilityValue());
		rootNode.setNewUtilityValueIfBetter(-1, rootNode);
		assertEquals(0, rootNode.getUtilityValue());
	}
	
	@Test
	public void minUtilitySet() {
		TestEval t = new TestEval(1);
		MiniMaxTree mmt = new MiniMaxTree(t);
		rootNode = mmt.new Node(null, t, false, -1000000, 1000000);
		assertEquals(1000000, rootNode.getUtilityValue());
		rootNode.setNewUtilityValueIfBetter(1000001, rootNode);
		assertEquals(1000000, rootNode.getUtilityValue());
		rootNode.setNewUtilityValueIfBetter(1000000, rootNode);
		assertEquals(1000000, rootNode.getUtilityValue());
		rootNode.setNewUtilityValueIfBetter(999999, rootNode);
		assertEquals(999999, rootNode.getUtilityValue());
		rootNode.setNewUtilityValueIfBetter(0, rootNode);
		assertEquals(0, rootNode.getUtilityValue());
		rootNode.setNewUtilityValueIfBetter(-1, rootNode);
		assertEquals(-1, rootNode.getUtilityValue());
	}
	
	@Test
	public void simpleNodeState() {
		assertEquals(state, rootNode.getState());
		rootNode.generateUtilityValue();
		assertEquals(1, rootNode.getUtilityValue());
	}
	
	@Test
	public void parentChildCreationMax() {
		MiniMaxTree.Node childNode = rootNode.getNextChild();
		assertNotNull(childNode);
		assertEquals(-1000000, rootNode.getUtilityValue());
		rootNode.generateUtilityValue();
		assertEquals(1, rootNode.getUtilityValue());
		assertEquals(1000000, childNode.getUtilityValue());
		childNode.generateUtilityValue();
		assertEquals(2, childNode.getUtilityValue());
		assertEquals(rootNode, childNode.getParent());
		assertEquals(childNode, rootNode.children.get(0));
	}
	
	@Test
	public void parentChildCreationMin() {
		TestEval t = new TestEval(1);
		MiniMaxTree mmt = new MiniMaxTree(t);
		rootNode = mmt.new Node(null, t, false, -1000000, 1000000);
		MiniMaxTree.Node childNode = rootNode.getNextChild();
		assertNotNull(childNode);
		assertEquals(1000000, rootNode.getUtilityValue());
		rootNode.generateUtilityValue();
		assertEquals(1, rootNode.getUtilityValue());
		assertEquals(-1000000, childNode.getUtilityValue());
		childNode.generateUtilityValue();
		assertEquals(2, childNode.getUtilityValue());
		assertEquals(rootNode, childNode.getParent());
		assertEquals(childNode, rootNode.children.get(0));
	}
	
	/*@Test
	public void oneDeepTreeGen() {
		Evaluatable tempTE = mMT.processToDepth(1);
	}
	
	@Test
	public void twoDeepTreeGen() {
		Evaluatable tempTE = mMT.processToDepth(2);
	}
	
	@Test
	public void fourDeepTreeGen() {
		Evaluatable tempE = new TreeEval(1);
		mMT = new MiniMaxTree(tempE);
		mMT.processToDepth(4);
		LinkedList<Evaluatable> path = mMT.getPath();
	}*/
}
