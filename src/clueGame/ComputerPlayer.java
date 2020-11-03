package clueGame;

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
}
