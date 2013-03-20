import static org.junit.Assert.*;

import org.junit.Test;


public class MiniMaxTreeTest {
	
	
	public class TestEval implements Evaluatable {
		
		TestEval() {
			
		}
		
		public int evaluate() {
			return 1;
		}
		
		public TestEval getNextState() {
			TestEval t = new TestEval();
			return t;
		}
	}
	
	@Test
	public void createNode() {
		TestEval t = new TestEval();
		MiniMaxTree mmt = new MiniMaxTree(t);
		MiniMaxTree.Node testNode = mmt.new Node(null, t, true, -1000000, 1000000);
		assertEquals(-1000000, testNode.getAlpha());
		assertEquals(1000000, testNode.getBeta());
	}

}
