package experiment;

import java.util.Set;
import java.util.HashSet;

public class TestBoard {
	private Set<TestBoardCell> targets;
	public TestBoard() {
		super();
		targets = new HashSet<TestBoardCell>();
		for(int i = 0; i<0; i++) {
			for(int j = 0; j<0; j++) {
				targets.add(new TestBoardCell(i,j));
			}
		}
	}
	public void calcTargets(TestBoardCell startCell, int pathlength) {
		
	}
	public Set<TestBoardCell> getTargets(){
		return targets;
	}
	public TestBoardCell getCell(int row, int col) {
		//just returning a new one, not correct, but it works
		return new TestBoardCell(row, col);
	}

}
