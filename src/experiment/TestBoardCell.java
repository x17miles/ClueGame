package experiment;


import java.util.Collections;
import java.util.Set;
import java.util.HashSet;


public class TestBoardCell {
	private int row;
	private int col;
	private boolean inRoom;
	private Set<TestBoardCell> adjList;
	
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

	private boolean occupied;
	public TestBoardCell(int row, int col) {
		super();
		this.row = row;
		this.col = col;
	}

	public Set<TestBoardCell> getAdjList(){
		adjList = new HashSet<TestBoardCell>();
		adjList.add(this);
		return adjList;
	}
	
}
