import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class MiniMaxTreeTest {
	
	
	class TestEval implements Evaluatable {
		
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
		
	MiniMaxTree.Node rootNode;
	
	@Before
	public void setUp() throws Exception {
		TestEval t = new TestEval();
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
	public void nodeSetterTest() {
		rootNode.setAlpha(10);
		assertEquals(10, rootNode.getAlpha());
		rootNode.setBeta(0);
		assertEquals(0, rootNode.getBeta());
		rootNode.setUtilityValue(1000);
		assertEquals(1000, rootNode.getUtilityValue());
	}
	
	@Test
	public void minMaxUtilitySet() {
		assertEquals(-1000000, rootNode.getAlpha());
		rootNode.setNewUtilityValueIfBetter(-1000001);
		assertEquals(-1000000, rootNode.getAlpha());
		rootNode.setNewUtilityValueIfBetter(-1000000);
		assertEquals(-1000000, rootNode.getAlpha());
		//rootNode.setNewUtilityValueIfBetter(-999999);
		//assertEquals(-999999, rootNode.getAlpha());
	}

}
