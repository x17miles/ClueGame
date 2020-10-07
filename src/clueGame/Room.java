package clueGame;

public class Room {
	private String name;
	private BoardCell centerCell;
	private BoardCell labelCell;
	public Room() {
		super();
		//stubs
		name = "";
		centerCell = new BoardCell(0,0);
		labelCell = new BoardCell(0,0);
	}
	public String getName() {
		return this.name;
	}
	public BoardCell getLabelCell() {
		return labelCell;
	}
	public BoardCell getCenterCell() {
		return centerCell;
	}
}
