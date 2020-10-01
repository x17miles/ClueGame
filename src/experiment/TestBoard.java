package experiment;

import java.util.Set;

public class TestBoard {
	private Set<TestBoardCell> targets;
	public TestBoard() {
		super();
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
		return null;
	}

}
