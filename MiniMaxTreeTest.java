import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class MiniMaxTreeTest {
	
	
	class TestEval implements Evaluatable {
		
		public int val;
		
		public TestEval(int _val) {
			val = _val;
		}
		
		public int evaluate() {
			return 1;
		}
		
		public int getVal() {
			return val;
		}
		
		public TestEval getNextState() {
			TestEval t = new TestEval(val + 1);
			return t;
		}
	}
		
	MiniMaxTree.Node rootNode;
	Evaluatable state;
	
	@Before
	public void setUp() throws Exception {
		TestEval t = new TestEval(1);
		state = t;
		MiniMaxTree mmt = new MiniMaxTree(t);
		rootNode = mmt.new Node(null, t, true, -1000000, 1000000);
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
		rootNode.setNewUtilityValueIfBetter(-1000001);
		assertEquals(-1000000, rootNode.getUtilityValue());
		rootNode.setNewUtilityValueIfBetter(-1000000);
		assertEquals(-1000000, rootNode.getUtilityValue());
		rootNode.setNewUtilityValueIfBetter(-999999);
		assertEquals(-999999, rootNode.getUtilityValue());
		rootNode.setNewUtilityValueIfBetter(0);
		assertEquals(0, rootNode.getUtilityValue());
		rootNode.setNewUtilityValueIfBetter(-1);
		assertEquals(0, rootNode.getUtilityValue());
	}
	
	@Test
	public void minUtilitySet() {
		TestEval t = new TestEval(1);
		MiniMaxTree mmt = new MiniMaxTree(t);
		rootNode = mmt.new Node(null, t, false, -1000000, 1000000);
		assertEquals(1000000, rootNode.getUtilityValue());
		rootNode.setNewUtilityValueIfBetter(1000001);
		assertEquals(1000000, rootNode.getUtilityValue());
		rootNode.setNewUtilityValueIfBetter(1000000);
		assertEquals(1000000, rootNode.getUtilityValue());
		rootNode.setNewUtilityValueIfBetter(999999);
		assertEquals(999999, rootNode.getUtilityValue());
		rootNode.setNewUtilityValueIfBetter(0);
		assertEquals(0, rootNode.getUtilityValue());
		rootNode.setNewUtilityValueIfBetter(-1);
		assertEquals(-1, rootNode.getUtilityValue());
	}
	
	@Test
	public void simpleNodeState() {
		assertEquals(state, rootNode.getState());
		rootNode.generateUtilityValue();
		assertEquals(1, rootNode.getUtilityValue());
	}
}
