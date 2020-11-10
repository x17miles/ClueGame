package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

public class BoardCell{
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
	
	/*
	 * @param row - the row number of the cell, starting with 0
	 * @param col - the column number of the cell, starting with 0
	 * @param initial - the initial of the room/space that the cell exists in, e.g. O for Observatory
	 */
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
	
	public void paint(Graphics g, int width, int height, int cols, int rows) {
//		if(initial == 'W') {
//			g.setColor(Color.RED);
//		}
		if(initial == 'W') {
			g.setColor(Color.RED);
		} else if (initial == 'X') {
			g.setColor(Color.black);
		} else if (secret) {
			g.setColor(Color.orange);
		}
		else {
			g.setColor(Color.gray);
		}
			
		g.fillRect(col*width/cols, row*height/rows, width/cols-1, height/rows-1);
		//g.fillRect(col*width/cols, row*height/rows, 3, 3);
	}
	
	public void setSecretPassage(char c) {
		this.secretPassage = c;
		this.secret = true;
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
	public void setInRoom(boolean inRoom) {
		this.inRoom = inRoom;
	}

	
	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}
	public void addAdj(BoardCell cell) {
		this.adjList.add(cell);
	}
	
	public char getInitial() {
		return initial;
	}
	public int[] getPosition() {
		int[] pos = {row,col};
		return pos;
	}
	
	public char getSecretPassage() {
		return secretPassage;
	}
	public Set<BoardCell> getAdjList(){
		return adjList;
	}
	
	
	public DoorDirection getDoorDirection() {
		return doorDirection;
	}
	

	
	public boolean isInRoom() {
		return inRoom;
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
	public boolean isOccupied() {
		return occupied;
	}
	public boolean isSecret() {
		return this.secret;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(obj == null || obj.getClass() != this.getClass()) return false;
		BoardCell target = (BoardCell) obj;
		if(this.row == target.row && this.col == target.col && this.initial ==target.getInitial()) return true;
		return false;
	}

	
	
}
