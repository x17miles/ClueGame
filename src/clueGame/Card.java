package clueGame;

public class Card {
	private String name;
	public String getName() {
		return name;
	}

	private CardType type;
	public CardType getType() {
		return this.type;
	}
	
	//override of equals function
	@Override
	public boolean equals(Object o) {
		if(o == null || o.getClass() != this.getClass()) return false;
		Card target = (Card) o;
		if(this.name.equals(target.name) && this.type.equals(target.type)) return true;
		return false;
	}

	public Card(String name, CardType type) {
		super();
		this.name = name;
		this.type = type;
	}
	
}
