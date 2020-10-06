package experiment;


import java.util.Collections;
import java.util.Set;
import java.util.HashSet;


public class TestBoardCell {
	private int row;
	private int col;
	private boolean inRoom;
	private boolean occupied;
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
	public void addAdj(TestBoardCell cell) {
		this.adjList.add(cell);
	}

	
	
	public TestBoardCell(int row, int col) {
		super();
		this.row = row;
		this.col = col;
		adjList = new HashSet<TestBoardCell>();
		this.occupied = false;
		this.inRoom = false;
	}

	public Set<TestBoardCell> getAdjList(){
		return adjList;
	}
	
}
