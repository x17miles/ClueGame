package clueGame;

public class Solution {

	public Card person;
	public Card room;
	public Card weapon;
	
	public Solution(Card person, Card room, Card weapon) {
		super();
		this.person = person;
		this.room = room;
		this.weapon = weapon;
	}

	@Override
	public boolean equals(Object o) {
		if(o == null || o.getClass() != this.getClass()) return false;
		Solution target = (Solution) o;
		if(this.person.equals(target.person) && this.room.equals(target.room)&&this.weapon.equals(target.weapon)) {
			return true;
		}
		return false;
	}
	
	
	
}
