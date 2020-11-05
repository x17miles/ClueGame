package clueGame;

import java.util.ArrayList;
import java.util.Random;

public class ComputerPlayer extends Player {
	private Random r = new Random();
	public ComputerPlayer(String name, int[] startingLocation, String color) {
		super(name, startingLocation, color);
		// TODO Auto-generated constructor stub
	}
	
	public Solution createSuggestion(Room room) {
		Card roomCard = null, personCard = null, weaponCard = null;
		ArrayList<Card> unseenPeople = new ArrayList<Card>();
		ArrayList<Card> unseenWeapons = new ArrayList<Card>();
		Board board = Board.getInstance();
		roomCard = board.getDeckCard(room.getName());
		//get player card
		boolean personFound = false;
		boolean weaponFound = false;
		for(Card i : board.getDeck()) {
			if(i.getType() == CardType.PERSON && !this.getSeen().contains(i)) {
				unseenPeople.add(i);
				personFound = true;
				
			} else if (i.getType() == CardType.WEAPON && !this.getSeen().contains(i)) {
				unseenWeapons.add(i);
				weaponFound = true;
			}
		}

		if (weaponFound) {
			weaponCard = unseenWeapons.get(r.nextInt(unseenWeapons.size()));
		}
		if (personFound) {
			personCard = unseenPeople.get(r.nextInt(unseenPeople.size()));
		}
		
		for (Card i : this.getSeen()) {
			if(personFound && weaponFound) {
				break;
			} else if (!personFound && i.getType() == CardType.PERSON) {
				personCard = i;
				personFound = true;
			} else if (! weaponFound && i.getType() == CardType.WEAPON) {
				weaponCard = i;
				weaponFound = true;
			}
		}
		return new Solution(personCard, roomCard, weaponCard);
	}
	
	public BoardCell selectTargets() {
		return new BoardCell(0,0,'-');
	}

	@Override
	public Card disproveSuggestion(Solution suggestion) {
		ArrayList<Card> suggestionCards = new ArrayList<Card>();
		suggestionCards.add(suggestion.person);
		suggestionCards.add(suggestion.room);
		suggestionCards.add(suggestion.weapon);
		ArrayList<Card> matchingCards = new ArrayList<Card>();
		for(Card i : suggestionCards) {
			if (super.getHand().contains(i)) matchingCards.add(i);
		}
		if(matchingCards.size() == 0) return null;
		Random r = new Random();
		return matchingCards.get(r.nextInt(matchingCards.size()));
	}
}
