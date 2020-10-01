package tests;
import experiment.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.*;

class BoardTestsExp {
	TestBoard board;
	
	@BeforeEach
	public void setUp() {
		board = new TestBoard();
	}
	
	/*
	 * Tests adjacencies for multiple cell locations, denoted in comments above each section.
	 */
	
	@Test
	public void testAdjacency() {
		//top left
		TestBoardCell cell = board.getCell(0, 0);
		Set<TestBoardCell> testList = cell.getAdjList();
		assertTrue(testList.contains(board.getCell(1, 0)));
		assertTrue(testList.contains(board.getCell(0, 1)));
		assertEquals(2, testList.size());
		
		//bottom right
		cell = board.getCell(4, 4);
		testList = cell.getAdjList();
		assertTrue(testList.contains(board.getCell(3, 4)));
		assertTrue(testList.contains(board.getCell(4, 3)));
		assertEquals(2, testList.size());
		
		//right edge
		cell = board.getCell(2, 4);
		testList = cell.getAdjList();
		assertTrue(testList.contains(board.getCell(1, 4)));
		assertTrue(testList.contains(board.getCell(2, 3)));
		assertTrue(testList.contains(board.getCell(3, 4)));
		assertEquals(3, testList.size());
		
		//left edge
		cell = board.getCell(3, 0);
		testList = cell.getAdjList();
		assertTrue(testList.contains(board.getCell(3, 1)));
		assertTrue(testList.contains(board.getCell(2, 0)));
		assertTrue(testList.contains(board.getCell(4, 0)));
		assertEquals(3, testList.size());
		
		//middle
		cell = board.getCell(1, 1);
		testList = cell.getAdjList();
		assertTrue(testList.contains(board.getCell(1, 0)));
		assertTrue(testList.contains(board.getCell(0, 1)));
		assertTrue(testList.contains(board.getCell(1, 2)));
		assertTrue(testList.contains(board.getCell(2, 1)));
		assertEquals(4, testList.size());
		
	}
	
	@Test
	public void testCalcTarget() {
		//Test trivial upper left case
		board.calcTargets(board.getCell(0, 0), 1);
		assertTrue(board.getTargets().contains(board.getCell(0, 1)));
		assertTrue(board.getTargets().contains(board.getCell(1, 0)));
		
		
	}
	
	

}
