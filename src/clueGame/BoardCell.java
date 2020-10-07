package clueGame;

import java.util.HashSet;
import java.util.Set;

public class BoardCell {
	private int row;
	private int col;
	private char initial;
	private char secretPassage;
	private boolean roomLabel;
	private boolean roomCenter;
	private boolean inRoom;
	private boolean occupied;
	private Set<BoardCell> adjList;
	
	public boolean isInRoom() {
		return inRoom;
	}

	public void setInRoom(boolean inRoom) {
		this.inRoom = inRoom;
	}

	public boolean isOccupied() {
		return occupied;
	}

	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}
	public void addAdj(BoardCell cell) {
		this.adjList.add(cell);
	}

	
	public BoardCell(int row, int col) {
		//initialize row and column and make the adjList an empty set for the moment
		super();
		this.row = row;
		this.col = col;
		adjList = new HashSet<BoardCell>();
		this.occupied = false;
		this.inRoom = false;
	}

	public Set<BoardCell> getAdjList(){
		return adjList;
	}
}
