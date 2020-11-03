package clueGame;

import java.util.ArrayList;
import java.util.Random;

public class ComputerPlayer extends Player {

	public ComputerPlayer(String name, int[] startingLocation, String color) {
		super(name, startingLocation, color);
		// TODO Auto-generated constructor stub
	}
	
	public Solution createSuggestion() {
		return new Solution(new Card("stub1", CardType.PERSON), new Card("stub2", CardType.ROOM), new Card("stub3", CardType.WEAPON));
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
