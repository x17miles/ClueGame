package tests;
import clueGame.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.*;

public class BoardAdjTargetTest {
private static Board board;
	
	@BeforeAll
	public static void setUp() {
		
		board = Board.getInstance();
		
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		
		board.initialize();
	}
	/*
	 * ADJACENCY TESTS
	 * The following tests assure that a cell on our board correctly lists out its appropriate adjacencies
	 */
	
	@Test
	public void testAdjacencyWalkway() {
		//only walkways as adjacent locations
		Set<BoardCell> testList = board.getAdjList(6, 6);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(5, 6)));
		assertTrue(testList.contains(board.getCell(7, 6)));
		assertTrue(testList.contains(board.getCell(6, 5)));
		assertTrue(testList.contains(board.getCell(6, 7)));
		
		//only has spaces above and below
		testList = board.getAdjList(11, 15);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(10, 15)));
		assertTrue(testList.contains(board.getCell(12, 15)));
		
	}
	@Test
	public void testAdjacencyDoors() {
		//test locations that are doorways themselves
		Set<BoardCell> testList = board.getAdjList(14, 6);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(17, 4)));
		assertTrue(testList.contains(board.getCell(13, 6)));
		assertTrue(testList.contains(board.getCell(14, 7)));
	}
	@Test
	public void testAdjacencyRoom() {
		//locations within rooms that are not centers. They should have no adjacencies
		Set<BoardCell> testList = board.getAdjList(15, 20);
		assertEquals(0, testList.size());
		
		//test room center adjacencies
		testList = board.getAdjList(3,3);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCell(5, 4)));
	}
	@Test
	public void testAdjacencyBoardEdges() {
		//Test the adjacencies on the top edge of the board
		Set<BoardCell> testList = board.getAdjList(0, 7);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(0, 6)));
		assertTrue(testList.contains(board.getCell(1, 7)));
		
		//test adjacencies on the right edge of the board
		testList = board.getAdjList(8, 21);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(7, 21)));
		assertTrue(testList.contains(board.getCell(9, 21)));
		assertTrue(testList.contains(board.getCell(8, 20)));
	}
	@Test
	public void testAdjacencyRoomEdges() {
		//test walkway along the side of the greenhouse
		Set<BoardCell> testList = board.getAdjList(20, 4);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(20, 3)));
		assertTrue(testList.contains(board.getCell(20, 5)));
		assertTrue(testList.contains(board.getCell(21, 4)));
		
		//test walkway along Dimensional Research
		testList = board.getAdjList(10, 18);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(9, 18)));
		assertTrue(testList.contains(board.getCell(10, 17)));
		assertTrue(testList.contains(board.getCell(10, 19)));
		
		
		
	}
	@Test
	public void testAdjacencySecretPassage() {
		//Assure the the main bridge links to the warp drive room
		Set<BoardCell> testList = board.getAdjList(24, 9);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(21, 7)));
		assertTrue(testList.contains(board.getCell(5, 11)));
		
		//assure that dimensional research links to teleporter
		testList = board.getAdjList(16, 17);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(10, 3)));
		assertTrue(testList.contains(board.getCell(13, 18)));
		
	}
	
	
	/*
	 * TARGET TESTS
	 * The following tests assure that the appropriate targets are created for a player given their location
	 * and the path length
	 */
	@Test
	public void testTargetsWalkways() {
		//tests targets for a generic walkway, with a distance of 4
		board.calcTargets(board.getCell(8, 12), 4);
		Set<BoardCell> targets= board.getTargets();
		assertTrue(targets.contains(board.getCell(10, 14)));
		assertTrue(targets.contains(board.getCell(9, 15)));
		assertTrue(targets.contains(board.getCell(8, 14)));
	}
	@Test
	public void testTargetsEntrances() {
		//test the targets from a doorway, which should include medical in its target list
		board.calcTargets(board.getCell(5, 4), 2);
		Set<BoardCell> targets= board.getTargets();
		assertTrue(targets.contains(board.getCell(3, 3)));
		assertTrue(targets.contains(board.getCell(6, 5)));
		assertTrue(targets.contains(board.getCell(5, 2)));
	}
	@Test
	public void testTargetsExitRoom() {
		//test the exit of a normal room (Sleeping Quarters) with a distance of 3
		board.calcTargets(board.getCell(4, 19), 3);
		Set<BoardCell> targets= board.getTargets();
		assertTrue(targets.contains(board.getCell(8, 17)));
		assertTrue(targets.contains(board.getCell(7, 20)));
		assertTrue(targets.contains(board.getCell(7, 16)));
		
	}
	@Test
	public void testTargetsExitSecretPassage() {
		//test the exit of main bridge and assure that the warp drive room is in target list
		board.calcTargets(board.getCell(24, 9), 2);
		Set<BoardCell> targets= board.getTargets();
		assertTrue(targets.contains(board.getCell(5, 11)));
		assertTrue(targets.contains(board.getCell(21, 6)));
		assertTrue(targets.contains(board.getCell(19, 13)));
		
	}
	@Test
	public void testTargetsBlockedByPlayer() {
		//create an arbitrary player within 3 units of the starting cell, assure that target calculation
		//which would normally include that cell now lacks it because the cell is occupied.
		board.getCell(17, 11).setOccupied(true);
		board.calcTargets(board.getCell(16, 9), 3);
		Set<BoardCell> targets= board.getTargets();
		assertFalse(targets.contains(board.getCell(17, 11)));
		assertTrue(targets.contains(board.getCell(14, 8)));
		assertTrue(targets.contains(board.getCell(18, 10)));
		
	}
	

}