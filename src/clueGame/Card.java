package clueGame;

public class Card {
	private String name;
	private CardType type;
	
	public boolean equals(Card target) {
		return true;
	}

	public Card(String name, CardType type) {
		super();
		this.name = name;
		this.type = type;
	}
	
}
