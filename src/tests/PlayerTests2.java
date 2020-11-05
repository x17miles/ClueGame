package tests;

import clueGame.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.Set;

import javax.print.Doc;

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
		//correct person
		assertTrue(board.checkAccusation(new Solution(board.getDeckCard("Captain"),board.getDeckCard("Medical"),board.getDeckCard("Phaser"))));
		//wrong person
		assertFalse(board.checkAccusation(new Solution(board.getDeckCard("Cook"),board.getDeckCard("Medical"),board.getDeckCard("Phaser"))));
		//wrong room
		assertFalse(board.checkAccusation(new Solution(board.getDeckCard("Captain"),board.getDeckCard("Observatory"),board.getDeckCard("Phaser"))));
		//wrong weapon
		assertFalse(board.checkAccusation(new Solution(board.getDeckCard("Captain"),board.getDeckCard("Medical"),board.getDeckCard("RayGun"))));
		
	}
	
	@Test
	void checkDisproveSuggestion() {
		board.deal();
		int[] startLoc = {0,0};
		Player p = new ComputerPlayer("dummy", startLoc, "red");	
		//set standard suggestion
		Solution suggestion = new Solution(board.getDeckCard("Captain"),board.getDeckCard("Medical"),board.getDeckCard("RayGun"));
		//dummy should have nothing in common with the actual solution
		assertEquals(null, p.disproveSuggestion(suggestion));
		
		
		//1 card known
		p.updateHand(board.getDeckCard("Captain"));
		assertEquals(board.getDeckCard("Captain"), p.disproveSuggestion(suggestion));
		
		
		//2 cards known, show randomness
		p.updateHand(board.getDeckCard("Medical"));
		int captCount, medCount;
		captCount=medCount=0;
		for(int i = 0; i < 10; i++) {
			Card proof = p.disproveSuggestion(suggestion);
			if(board.getDeckCard("Captain").equals(proof)) captCount++;
			else if(board.getDeckCard("Medical").equals(proof)) medCount++;
		}
		assertTrue(medCount>0);
		assertTrue(captCount>0);
		assertEquals(10,medCount+captCount);	
	}
	
	@Test
	void makeSuggestionTest() {
		board.deal();
		for(Player p: board.getPlayers()) {
			p.clearHand();
		}
		Player testPlayerOne = board.getPlayer("Captain");
		Player testPlayerTwo = board.getPlayer("Cook");
		Player testPlayerThree = board.getPlayer("Doctor");
		Player testPlayerHuman = board.getPlayer("Jimbothy");
		
		Solution suggestion = new Solution(board.getDeckCard("Captain"),board.getDeckCard("Medical"),board.getDeckCard("RayGun"));
		//Suggestion no one can disprove, because noone has cards
		assertEquals(null, board.handleSuggestion(suggestion, testPlayerOne));
		
		//suggestion that won't be disproven, because only player 1 has the card
		testPlayerOne.updateHand(board.getDeckCard("Cook"));
		assertEquals(null, board.handleSuggestion(suggestion, testPlayerOne));
		
		//suggestion that only player 2 can disprove, because only player 2 has one of the cards
		testPlayerTwo.updateHand(board.getDeckCard("Medical"));
		assertEquals(board.getDeckCard("Medical"), board.handleSuggestion(suggestion, testPlayerOne));
		//Clear both hands for new test
		testPlayerOne.clearHand();
		testPlayerTwo.clearHand();
		
		//assert player disproves suggestion
		testPlayerHuman.updateHand(board.getDeckCard("RayGun"));
		assertEquals(board.getDeckCard("RayGun"), board.handleSuggestion(suggestion, testPlayerOne));
		testPlayerHuman.clearHand();
		
		//get the order of the players, assert that next player in the order chooses the disproving card
		ArrayList<Player> orderedPlayers = new ArrayList<Player>();
		orderedPlayers.add(testPlayerOne);
		orderedPlayers.add(testPlayerTwo);
		orderedPlayers.add(testPlayerThree);
		orderedPlayers.get(1).updateHand(board.getDeckCard("Medical"));
		orderedPlayers.get(2).updateHand(board.getDeckCard("RayGun"));
		board.setPlayerOrder(orderedPlayers);
		assertEquals(board.getDeckCard("Medical"), board.handleSuggestion(suggestion, testPlayerOne));
		assertEquals(board.getDeckCard("RayGun"), board.handleSuggestion(suggestion, testPlayerTwo));
	}
	
	
	@Test
	public void testComputerSuggestion() {
		//setup
		board.deal();
		Player p1 = board.getPlayer("Captain");
		p1.clearHand();
		p1.updateSeen(board.getDeckCard("RayGun"));
		p1.updateSeen(board.getDeckCard("Cook"));
		int[] startLoc = {3,3};
		p1.setStartingLocation(startLoc);
		
		//counters for weapons to assert randomness
		int PhaserCounter = 0;
		int PortalCounter = 0;
		int SaberCounter = 0;
		int TorchCounter = 0;
		int HammerCounter = 0;
		
		//counters for people
		int CaptainCounter=0, DoctorCounter=0, EngineerCounter=0, MercenaryCounter=0, JimbothyCounter=0;
		
		//create 10 suggestions, assert that they use cards that the player has not seen
		for(int i = 0; i < 10; i++) {
			Solution suggestion = p1.createSuggestion(board.getRoom(board.getCell(3, 3)));
			//assert unseen person and weapon
			assertTrue(!p1.getSeen().contains(suggestion.person));
			assertTrue(!p1.getSeen().contains(suggestion.weapon));
			//assert that the correct room is used
			assertEquals("Medical", suggestion.room.getName());
			//count up each use of weapon card to assert randomness
			switch (suggestion.weapon.getName()) {
			case "Phaser":
				PhaserCounter ++;
				break;
			case "Portal to Vogon":
				PortalCounter ++;
				break;
			case "Light-Saber":
				SaberCounter++;
				break;
			case "Blow Torch":
				TorchCounter++;
				break;
			case "Space Hammer":
				HammerCounter++;
				break;
			}
			switch (suggestion.person.getName()) {
			case "Captain":
				CaptainCounter++;
				break;
			case "Doctor":
				DoctorCounter++;
				break;
			case "Engineer":
				EngineerCounter++;
				break;
			case "Mercenary":
				MercenaryCounter++;
				break;
			case "Jimbothy":
				JimbothyCounter++;
				break;
			}
		}
		//all of these weapons should sum to 10 uses
		// assert that no single weapon is used in all 10 suggestions
		assertEquals(10, PhaserCounter + PortalCounter + SaberCounter + TorchCounter + HammerCounter);
		assertTrue(PhaserCounter < 10);
		assertTrue(PortalCounter < 10);
		assertTrue(SaberCounter < 10);
		assertTrue(TorchCounter < 10);
		assertTrue(HammerCounter < 10);
		
		//assert that people are randomly selected
		assertEquals(10, CaptainCounter + DoctorCounter+EngineerCounter+MercenaryCounter+JimbothyCounter);
		assertTrue(CaptainCounter < 10);
		assertTrue(DoctorCounter < 10);
		assertTrue(EngineerCounter < 10);
		assertTrue(MercenaryCounter < 10);
		assertTrue(JimbothyCounter < 10);
	} 
	
	@Test
	void oneUnseenSuggestionTest() {
		board.deal();
		Player p1 = board.getPlayer("Captain");
		p1.clearHand();
		//Update seen so that there are only 2 unseen cards Jimbothy (a person) and RayGun (a weapon).
		//assert that the suggestion consists of those two and the appropriate room.
		p1.updateSeen(board.getDeckCard("Captain"));
		p1.updateSeen(board.getDeckCard("Cook"));
		p1.updateSeen(board.getDeckCard("Doctor"));
		p1.updateSeen(board.getDeckCard("Engineer"));
		p1.updateSeen(board.getDeckCard("Mercenary"));
		p1.updateSeen(board.getDeckCard("Phaser"));
		p1.updateSeen(board.getDeckCard("Portal to Vogon"));
		p1.updateSeen(board.getDeckCard("Light-Saber"));
		p1.updateSeen(board.getDeckCard("Blow Torch"));
		p1.updateSeen(board.getDeckCard("Space Hammer"));
		assertEquals(new Solution(board.getDeckCard("Jimbothy"), board.getDeckCard("Medical"), board.getDeckCard("RayGun")),
				p1.createSuggestion(board.getRoom(board.getCell(3, 3))));
	}

}
