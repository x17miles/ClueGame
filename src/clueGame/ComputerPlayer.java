package clueGame;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

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
		//the room card must be the room that we are creating a suggestion for
		roomCard = board.getDeckCard(room.getName());
		//if there are people/weapons that have not been seen, prioritize those, 
		//create a list of them first
		boolean personFound = false;
		boolean weaponFound = false;
		for(Card i : board.getDeck()) {
			if(i.getType() == CardType.PERSON && !this.getSeen().contains(i) && !this.getHand().contains(i)) {
				unseenPeople.add(i);
				personFound = true;
				
			} else if (i.getType() == CardType.WEAPON && !this.getSeen().contains(i) && !this.getHand().contains(i)) {
				unseenWeapons.add(i);
				weaponFound = true;
			}
		}
		//choose a random weapon/person from the unseen lists
		if (weaponFound) {
			weaponCard = unseenWeapons.get(r.nextInt(unseenWeapons.size()));
		}
		if (personFound) {
			personCard = unseenPeople.get(r.nextInt(unseenPeople.size()));
		}
		
		//otherwise choose a card from the already seen cards
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
		//return a solution object as the suggestion
		return new Solution(personCard, roomCard, weaponCard);
	}
	
	public BoardCell selectTargets() {
		Board board = Board.getInstance();
		Set<BoardCell> targets = board.getTargets();
		int counter = r.nextInt(targets.size());
		//first if there is an unvisited room in the list, return that
		for(BoardCell i : targets) {
			if(i.isRoomCenter() && !this.getVisitedRooms().contains(board.getRoom(i))) {
				//add the room to the seen room list
				this.addVisitedRoom(board.getRoom(i));
				return i;
			}
		}
		//otherwise, return a random target
		for (BoardCell i : targets) {
			if (counter == 0) {
				return i;
			}
			counter --;
		}
		
		return null;
	}

	@Override
	public Card disproveSuggestion(Solution suggestion) {
		//create a iterable list of cards in the suggestion
		ArrayList<Card> suggestionCards = new ArrayList<Card>();
		suggestionCards.add(suggestion.person);
		suggestionCards.add(suggestion.room);
		suggestionCards.add(suggestion.weapon);
		
		//keep a list of kards that match the player has
		ArrayList<Card> matchingCards = new ArrayList<Card>();
		for(Card i : suggestionCards) {
			if (super.getHand().contains(i)) matchingCards.add(i);
		}
		//if there are no matching cards, return nothing, otherwise return a random matching card
		if(matchingCards.size() == 0) return null;
		Random r = new Random();
		return matchingCards.get(r.nextInt(matchingCards.size()));
	}
}
