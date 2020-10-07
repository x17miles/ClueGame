package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Assert;
import org.junit.jupiter.api.*;

import clueGame.*;

class FileInitTests {
	public static final int NUM_ROWS = 29;
	public static final int NUM_COLS = 22;
	public static final int NUM_DOORS = 15;
	private static Board board;
	
	@BeforeAll
	public static void setUp() {
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
	}
	
	
	/*
	 * correct number of rows and columns loaded in
	 */
	@Test
	public void testBoardSize() {
		assertEquals(NUM_ROWS,board.getNumRows());
		assertEquals(NUM_COLS,board.getNumColumns());
	}
	/*
	 * assure rooms loaded in properly
	 */
	@Test
	public void testRooms() {
		//Test Main Bridge
		BoardCell cell = board.getCell(26, 12);
		Room room = board.getRoom(cell);
		assertTrue( room != null);
		assertEquals("Main Bridge",room.getName());
		assertFalse(cell.isLabel());
		assertFalse(cell.isRoomCenter());
		assertFalse(cell.isDoorway());
		
		//Test Medical
		cell = board.getCell(1, 2);
		room = board.getRoom(cell);
		assertTrue(room != null);
		assertEquals("Medical", room.getName());
		assertTrue(cell.isLabel());
		assertFalse(cell.isRoomCenter());
		assertFalse(cell.isDoorway());
		
		//Test Warp Drive Room
		cell = board.getCell(5, 11);
		room = board.getRoom(cell);
		assertTrue(room != null);
		assertEquals("Warp Drive Room", room.getName());
		assertFalse(cell.isLabel());
		assertTrue(cell.isRoomCenter());
		assertFalse(cell.isDoorway());
		
		//Test Sleeping Quarters
		cell = board.getCell(0, 18);
		room = board.getRoom(cell);
		assertTrue(room != null);
		assertEquals("Sleeping Quarters", room.getName());
		assertFalse(cell.isLabel());
		assertFalse(cell.isRoomCenter());
		assertFalse(cell.isDoorway());
		
		//Test dimensional research and secret doorway
		cell = board.getCell(11, 21);
		room = board.getRoom(cell);
		assertTrue(room != null);
		assertEquals("Dimensional Research", room.getName());
		assertFalse(cell.isLabel());
		assertFalse(cell.isRoomCenter());
		assertFalse(cell.isDoorway());
		assertTrue(cell.getSecretPassage() == 'T');
		
		
	}
	
	/*
	 * Test doorways leading into rooms in each direction
	 */
	
	@Test
	public void testDoorways() {
		BoardCell cell = board.getCell(4, 14);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.LEFT, cell.getDoorDirection());
		cell = board.getCell(5, 4);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.UP, cell.getDoorDirection());
		cell = board.getCell(6, 8);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.RIGHT, cell.getDoorDirection());
		cell = board.getCell(19, 14);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.DOWN, cell.getDoorDirection());
		cell = board.getCell(9, 15);
		assertFalse(cell.isDoorway());
		assertEquals(DoorDirection.NONE, cell.getDoorDirection());
	}
	/*
	 * test correct number of doorways
	 */
	@Test
	public void testNumberOfDoorways() {
		int numDoors = 0;
		for (int row = 0; row < board.getNumRows(); row++)
			for (int col = 0; col < board.getNumColumns(); col++) {
				BoardCell cell = board.getCell(row, col);
				if (cell.isDoorway())
					numDoors++;
			}
		Assert.assertEquals(NUM_DOORS, numDoors);
	}
	
	/*
	 * Test cell initials
	 */
	@Test
	public void testCellInitials() {
		//empty initial
		BoardCell cell = board.getCell(13, 11);
		assertEquals('X', cell.getInitial());
		//Dimensional research initial
		cell = board.getCell(11, 16);
		assertEquals('D', cell.getInitial());
		//Greenhouse initial
		cell = board.getCell(15, 4);
		assertEquals('G', cell.getInitial());
		
		//Observatory Initial
		cell = board.getCell(21, 19);
		assertEquals('O', cell.getInitial());
	}

}
