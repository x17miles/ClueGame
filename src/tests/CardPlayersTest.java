package tests;

import clueGame.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CardPlayersTest {
	private static Board board;
	
	//General setup, obtain the board singleton
	@BeforeEach
	public void setup() {
		board = board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
	}

	@Test
	void playerTest() {
		//Assert that 6 players were loaded in
		Set<Player> players = board.getPlayers();
		assertEquals(6,players.size());
		int humanCount = 0;
		int botCount = 0;
		boolean foundMercenary = false;
		int[] startLoc = {13,1};
		Player mercenary = new ComputerPlayer("Mercenary", startLoc, "Black");
		
		for (Player i : players) {
			if(i.getClass() == HumanPlayer.class) {
				humanCount++;
			}
			if(i.getClass() == ComputerPlayer.class) {
				botCount++;
			}
			if(i.equals(mercenary)) foundMercenary = true;
		}
		//Assert that there are a total of 5 bots, and 1 human
		assertEquals(5, botCount);
		assertEquals(1, humanCount);
		//make sure that the mercenary player was loaded in
		assertTrue(foundMercenary);
		
	}
	@Test
	void weaponsTest() {
		//Assert that 6 weapons were loaded in
		Set<Card> weapons = board.getWeapons();
		assertEquals(6, weapons.size());
		//Make sure that the weapons set contains the blow torch
		boolean foundVal = false;
		Card torch = new Card("Blow Torch", CardType.WEAPON);
		for(Card i : weapons) {
			if (i.equals(torch)) foundVal = true;
		}
		assertTrue(foundVal);
	}
	
	@Test
	void deckTests(){
		//assert that the solution contains the three types of cards
		board.deal();
		Solution solution = board.getSolution();
		assertTrue(solution.person.getType() == CardType.PERSON);
		assertTrue(solution.room.getType() == CardType.ROOM);
		assertTrue(solution.weapon.getType() == CardType.WEAPON);
		
		//Make sure that the cards dealt to each player is > 0, and that hand sizes
		// only vary by 1 (<2)
		int maxCount, minCount;
		maxCount = 0;
		minCount = 100;
		for(Player i : board.getPlayers()) {
			if(maxCount < i.getHand().size()) maxCount = i.getHand().size();
			if(minCount > i.getHand().size()) minCount = i.getHand().size();
		}
		assertTrue(minCount > 0);
		assertTrue(maxCount-minCount < 2);
	}
	
	@Test
	void allCardsDealt() {
		board.deal();
		//Create a list of the cards from every player's hands, make sure there are no repeats
		boolean repeat = false;
		ArrayList<Card> allCards = new ArrayList<Card>();
		for(Player i : board.getPlayers()) {
			for(Card c : i.getHand()) {
				if(allCards.contains(c)) {
					repeat = true;
				}
				allCards.add(c);
			}
		}
		Solution solution = board.getSolution();
		Card[] solCards = {solution.person, solution.room, solution.weapon};
		for(Card c : solCards) {
			if(allCards.contains(c)) {
				repeat = true;
			}
			allCards.add(c);
		}
		//assert that a repeat has not occured and that there are 21 cards in total (9 rooms, 6 players, 6 weapons)
		assertFalse(repeat);
		assertEquals(21, allCards.size());
	}

}
