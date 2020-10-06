package tests;
import experiment.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.*;

class BoardTestsExp {
	private TestBoard board;
	
	@BeforeEach
	public void setUp() {
		board = new TestBoard();
	}
	
	/*
	 * Tests adjacencies for multiple cell locations, denoted in comments above each section.
	 */
	
	@Test
	public void testTopLeftAdjacency() {
		//test top left adjacency
		TestBoardCell cell = board.getCell(0, 0);
		Set<TestBoardCell> testList = cell.getAdjList();
		assertTrue(testList.contains(board.getCell(1, 0)));
		assertTrue(testList.contains(board.getCell(0, 1)));
		assertEquals(2, testList.size());
	}
	
	@Test
	public void testBottomRightAdjacency() {
		//test bottom right adjacency
		TestBoardCell cell = board.getCell(3, 3);
		Set<TestBoardCell> testList = cell.getAdjList();
		assertTrue(testList.contains(board.getCell(2, 3)));
		assertTrue(testList.contains(board.getCell(3, 2)));
		assertEquals(2, testList.size());
	}
	@Test
	public void testEdges() {
		
		//test right edge adjacency
		TestBoardCell cell = board.getCell(2, 3);
		Set<TestBoardCell> testList = cell.getAdjList();
		assertTrue(testList.contains(board.getCell(1, 3)));
		assertTrue(testList.contains(board.getCell(2, 2)));
		assertTrue(testList.contains(board.getCell(3, 3)));
		assertEquals(3, testList.size());
		
		//test left edge adjacency
		cell = board.getCell(2, 0);
		testList = cell.getAdjList();
		assertTrue(testList.contains(board.getCell(2, 1)));
		assertTrue(testList.contains(board.getCell(1, 0)));
		assertTrue(testList.contains(board.getCell(3, 0)));
		assertEquals(3, testList.size());
	}
	@Test
	public void testMiddle() {
		//test middle adjacency
		TestBoardCell cell = board.getCell(1, 1);
		Set<TestBoardCell> testList = cell.getAdjList();
		assertTrue(testList.contains(board.getCell(1, 0)));
		assertTrue(testList.contains(board.getCell(0, 1)));
		assertTrue(testList.contains(board.getCell(1, 2)));
		assertTrue(testList.contains(board.getCell(2, 1)));
		assertEquals(4, testList.size());
		
	}
	//END OF ADJACENCY TESTS
	
	
	/*
	 * Test targert calculations given the position of a piece and the setup of the board
	 */
	
	@Test
	public void testCalcTarget1Unit() {
		//Test trivial upper left case
		board.calcTargets(board.getCell(0, 0), 3);
		assertTrue(board.getTargets().contains(board.getCell(0, 1)));
		assertTrue(board.getTargets().contains(board.getCell(1, 0)));
		assertTrue(board.getTargets().contains(board.getCell(0, 3)));
		assertTrue(board.getTargets().contains(board.getCell(3, 0)));
		assertTrue(board.getTargets().contains(board.getCell(2, 1)));
		assertTrue(board.getTargets().contains(board.getCell(1, 2)));
	}
	
	@Test
	public void testCalcTargetTwoThree() {
		//Test case for 3 movement
		board.calcTargets(board.getCell(2,3), 3);
		assertTrue(board.getTargets().contains(board.getCell(2, 2)));
		assertTrue(board.getTargets().contains(board.getCell(3, 3)));
		assertTrue(board.getTargets().contains(board.getCell(0, 2)));
		assertTrue(board.getTargets().contains(board.getCell(1, 3)));
		assertTrue(board.getTargets().contains(board.getCell(1, 1)));
		assertTrue(board.getTargets().contains(board.getCell(3, 1)));
		
	}
	
	@Test
	public void testCalcTargetOneTwo() {
		//Test case for 3 movement
		board.calcTargets(board.getCell(1, 2), 3);
		assertTrue(board.getTargets().contains(board.getCell(0, 0)));
		assertTrue(board.getTargets().contains(board.getCell(2, 0)));
	}
	
	
	@Test
	public void testCalcTarget3Units() {
		board.calcTargets(board.getCell(0, 0), 3);
		assertTrue(board.getTargets().contains(board.getCell(3, 0)));
		assertTrue(board.getTargets().contains(board.getCell(2, 1)));
		
	}
	@Test
	public void testCalcTarget6Units() {
		board.calcTargets(board.getCell(0, 0), 6);
		assertTrue(board.getTargets().contains(board.getCell(3, 3)));
		assertTrue(board.getTargets().contains(board.getCell(0, 2)));
		
	}
	@Test
	public void testCalcOccupied() {
		board.getCell(0, 0).setOccupied(true);
		board.calcTargets(board.getCell(0, 1), 1);
		assertFalse(board.getTargets().contains(board.getCell(0, 0)));
		assertTrue(board.getTargets().contains(board.getCell(1,1)));
	}
	
	

}
