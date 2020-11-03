package clueGame;

import java.util.ArrayList;
import java.util.Random;

public abstract class Player {
	private String name;
	private int[] startingLocation;
	private String color;
	private ArrayList<Card> hand;
	private ArrayList<Card> seen;
	
	public Player(String name, int[] startingLocation, String color) {
		super();
		this.name = name;
		this.startingLocation = startingLocation;
		this.color = color;
		this.hand = new ArrayList<Card>();
		this.seen = new ArrayList<Card>();
	}
	
	public Card disproveSuggestion(Solution suggestion) {
		//stub
		ArrayList<Card> suggestionCards = new ArrayList<Card>();
		suggestionCards.add(suggestion.person);
		suggestionCards.add(suggestion.room);
		suggestionCards.add(suggestion.weapon);
		ArrayList<Card> matchingCards = new ArrayList<Card>();
		for(Card i : suggestionCards) {
			if (hand.contains(i)) matchingCards.add(i);
		}
		if(matchingCards.size() == 0) return null;
		Random r = new Random();
		return matchingCards.get(r.nextInt(matchingCards.size()));
	}
	
	public void updateSeen(Card seenCard) {
		seen.add(seenCard);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int[] getStartingLocation() {
		return startingLocation;
	}
	public void setStartingLocation(int[] startingLocation) {
		this.startingLocation = startingLocation;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public ArrayList<Card> getHand(){
		return this.hand;
	}
	
	//overide of equals function
	@Override
	public boolean equals(Object obj) {
		if(obj == null || obj.getClass() != this.getClass()) return false;
		Player target = (Player) obj;
		if(target.name.equals(this.name) && target.color.equals(this.color)) return true;
		return false;
	}

	public void updateHand(Card card) {
		this.hand.add(card);
	}
}
