package clueGame;

public enum CardType {
	PERSON("Person"),ROOM("Room"),WEAPON("Weapon");
	
	
	private String name;
	private CardType(String s) {
		this.name = s;
	}
	public String getString() {
		return this.name;
	}
}
