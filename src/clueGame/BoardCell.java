package clueGame;

import java.util.HashSet;
import java.util.Set;

public class BoardCell {
	private int row;
	private int col;
	private char initial;
	private char secretPassage;
	private boolean secret;
	private boolean roomLabel;
	private boolean roomCenter;
	private boolean inRoom;
	private boolean occupied;
	private DoorDirection doorDirection;
	private Set<BoardCell> adjList;
	
	public void setSecretPassage(char c) {
		this.secretPassage = c;
		this.secret = true;
	}
	public boolean isSecret() {
		return this.secret;
	}
	public void setRoomLabel(boolean t) {
		this.roomLabel = t;
	}
	public void setRoomCenter(boolean t) {
		this.roomCenter = t;
	}
	
	public void setDoorway(DoorDirection d) {
		this.doorDirection = d;
	}
	
	public char getInitial() {
		return initial;
	}
	
	public char getSecretPassage() {
		return secretPassage;
	}
	
	public boolean isLabel() {
		return roomLabel;
	}
	public boolean isRoomCenter() {
		return roomCenter;
	}
	
	public boolean isDoorway() {
		//if it isn't none, then it is a door
		return !doorDirection.equals(DoorDirection.NONE);
	}
	public DoorDirection getDoorDirection() {
		return doorDirection;
	}
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
	
	public BoardCell(int row, int col, char initial) {
		//initialize row and column and make the adjList an empty set for the moment
		super();
		this.row = row;
		this.col = col;
		adjList = new HashSet<BoardCell>();
		this.occupied = false;
		this.inRoom = false;
		//new stubs for full game, based on UML requirements
		roomCenter = false;
		roomLabel = false;
		this.initial = initial;
		doorDirection = DoorDirection.NONE;
	}

	public Set<BoardCell> getAdjList(){
		return adjList;
	}
}
