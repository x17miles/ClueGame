package clueGame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Room {
	private String name;
	private BoardCell centerCell;
	private BoardCell labelCell;
	private boolean space;
	private ArrayList<Player> playersInRoom;
	private Set<Player> playerSet;
	
	/*
	 * @param name - what the name of the room is, e.g. Observatory
	 */
	public Room(String name) {
		super();
		this.name = name;
		space = false;
		//centerCell and label cell will be initialized by the board
		centerCell = new BoardCell(0,0,'-');
		labelCell = new BoardCell(0,0,'-');
		playersInRoom = new ArrayList<Player>();
		playerSet = new HashSet<Player>();
	}
	
	public ArrayList<Player> getPlayersInRoom() {
		return playersInRoom;
	}

	public void addPlayer(Player p) {
		if(!playerSet.contains(p)) {
			this.playersInRoom.add(p);
			this.playerSet.add(p);
		}
			
	}
	
	public void removePlayer(Player p) {
		this.playerSet.remove(p);
		playersInRoom = new ArrayList<Player>();
		for(Player i : playerSet) {
			playersInRoom.add(i);
		}
	}

	public void setCenterCell(BoardCell centerCell) {
		this.centerCell = centerCell;
	}

	public void setLabelCell(BoardCell labelCell) {
		this.labelCell = labelCell;
	}

	public void setSpace(boolean space) {
		this.space = space;
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
	public boolean isSpace() {
		return space;
	}
}
