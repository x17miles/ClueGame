package tests;

import clueGame.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CardPlayersTest {
	private static Board board;
	
	@BeforeEach
	public void setup() {
		board = board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
	}

	@Test
	void playerTest() {
		Set<Player> players = board.getPlayers();
		assertEquals(6,players.size());
	}

}
