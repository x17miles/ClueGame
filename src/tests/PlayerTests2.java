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
	
	@Test
	void checkDisproveSuggestion() {
		board.deal();
		int[] startLoc = {0,0};
		Player p = new ComputerPlayer("dummy", startLoc, "red");	
		//set standard suggestion
		Solution suggestion = new Solution(board.getDeckCard("Captain"),board.getDeckCard("Medical"),board.getDeckCard("RayGun"));
		//cook should have nothing in common with the actual solution
		assertEquals(null, p.disproveSuggestion(suggestion));
		//1 card known
		p.updateHand(board.getDeckCard("Captain"));
		assertEquals(board.getDeckCard("Captain"), p.disproveSuggestion(suggestion));
		//2 cards known, show randomness
		p.updateHand(board.getDeckCard("Medical"));
		int captCount, medCount;
		captCount=medCount=0;
		for(int i = 0; i < 10; i++) {
			if(board.getDeckCard("Captain").equals(p.disproveSuggestion(suggestion))) captCount++;
			if(board.getDeckCard("Medical").equals(p.disproveSuggestion(suggestion))) medCount++;
		}
		assertTrue(medCount>0);
		assertTrue(captCount>0);
		assertTrue(medCount+captCount == 10);
		
		
		
		
	}

}
