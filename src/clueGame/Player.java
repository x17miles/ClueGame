package clueGame;

public abstract class Player {
	private String name;
	private int[] startingLocation;
	private String color;
	private Card[] hand;
	
	public Player(String name, int[] startingLocation, String color) {
		super();
		this.name = name;
		this.startingLocation = startingLocation;
		this.color = color;
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
	public void setStartingLocation(int[] startingLocation) {
		this.startingLocation = startingLocation;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
	abstract void updateHand();
}
