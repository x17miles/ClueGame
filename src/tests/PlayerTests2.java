package tests;

import clueGame.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlayerTests2 {
	private static Board board;
	@BeforeEach
	public void setup() {
		board = board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
	}
	
	@Test
	//check correct accusation and an accusation with a wrong answer
	void checkAccusationTest() {
		board.deal();
		board.setSolution(new Solution(board.getDeckCard("Captain"),board.getDeckCard("Medical"),board.getDeckCard("Phaser")));
		assertTrue(board.checkAccusation(new Solution(board.getDeckCard("Captain"),board.getDeckCard("Medical"),board.getDeckCard("Phaser"))));
		assertFalse(board.checkAccusation(new Solution(board.getDeckCard("Cook"),board.getDeckCard("Medical"),board.getDeckCard("Phaser"))));
		assertFalse(board.checkAccusation(new Solution(board.getDeckCard("Captain"),board.getDeckCard("Observatory"),board.getDeckCard("Phaser"))));
		assertFalse(board.checkAccusation(new Solution(board.getDeckCard("Captain"),board.getDeckCard("Medical"),board.getDeckCard("RayGun"))));
		
	}

}
