package clueGame;

public class Room {
	private String name;
	private BoardCell centerCell;
	private BoardCell labelCell;
	private boolean space;
	
	
	public void setCenterCell(BoardCell centerCell) {
		this.centerCell = centerCell;
	}

	public void setLabelCell(BoardCell labelCell) {
		this.labelCell = labelCell;
	}

	public boolean isSpace() {
		return space;
	}

	public void setSpace(boolean space) {
		this.space = space;
	}

	public Room(String name) {
		super();
		this.name = name;
		space = false;
		//centerCell and label cell will be initialized by the board
		centerCell = new BoardCell(0,0,'-');
		labelCell = new BoardCell(0,0,'-');
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
