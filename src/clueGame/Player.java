package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

public abstract class Player {
	private String name;
	private int[] startingLocation;
	private String color;
	private ArrayList<Card> hand;
	private ArrayList<Card> seen;
	private ArrayList<Room> visitedRooms;
	private int row,col;
	
	public Player(String name, int[] startingLocation, String color) {
		super();
		this.name = name;
		this.startingLocation = startingLocation;
		this.color = color;
		this.hand = new ArrayList<Card>();
		this.seen = new ArrayList<Card>();
		this.row = startingLocation[0];
		this.col = startingLocation[1];
		this.visitedRooms = new ArrayList<Room>();
	}
	public void paint(Graphics g, int width, int height, int cols, int rows) {
		//add a circle with the player's color at the player's current position
		g.setColor(getColorType());
		g.fillOval(col*width/cols, row*height/rows, width/cols -1, height/rows -1);
	}
	
	public abstract Card disproveSuggestion(Solution suggestion);
	
	public void addVisitedRoom(Room room) {
		visitedRooms.add(room);
	}
	public ArrayList<Room> getVisitedRooms(){
		return this.visitedRooms;
	}
	
	public void updateSeen(Card seenCard) {
		seen.add(seenCard);
	}
	public ArrayList<Card> getSeen(){
		return seen;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int[] getStartingLocation() {
		return startingLocation;
	}
	public int[] getPosition() {
		int[] pos = {this.row, this.col};
		return pos;
	}
	public void setStartingLocation(int[] startingLocation) {
		this.startingLocation = startingLocation;
	}
	public String getColor() {
		return color;
	}
	public Color getColorType() {
		try {
			return (Color)Color.class.getField(color.toLowerCase()).get(null);
		} catch (Exception e) {
			return Color.WHITE;
		}
	}
	public void setColor(String color) {
		this.color = color;
	}
	public ArrayList<Card> getHand(){
		return this.hand;
	}
	
	//overide of equals function
	@Override
	public boolean equals(Object obj) {
		if(obj == null || obj.getClass() != this.getClass()) return false;
		Player target = (Player) obj;
		if(target.name.equals(this.name) && target.color.equals(this.color)) return true;
		return false;
	}

	public void updateHand(Card card) {
		this.hand.add(card);
		this.seen.add(card);
	}
	
	//For testing
	public void clearHand() {
		this.hand.clear();
	}
	public void clearSeen() {
		this.seen.clear();
	}

	public abstract Solution createSuggestion(Room room);

	public abstract BoardCell selectTargets();
}
