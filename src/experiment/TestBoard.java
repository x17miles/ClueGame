package experiment;

import java.util.Set;
import java.util.HashSet;

public class TestBoard {
	private Set<TestBoardCell> targets;
	private TestBoardCell[][] grid;
	private Set<TestBoardCell> visited;
	final static int COLS = 4;
	final static int ROWS = 4;
	
	
	public TestBoard() {
		super();
		grid = new TestBoardCell[ROWS][COLS];
		targets = new HashSet<TestBoardCell>();
		visited = new HashSet<TestBoardCell>();
		for(int i = 0; i<ROWS; i++) {
			for(int j = 0; j<COLS; j++) {
				//targets.add(new TestBoardCell(i,j));
				grid[i][j] = new TestBoardCell(i,j);
			}
		}
		for(int i = 0; i<ROWS; i++) {
			for(int j = 0; j<COLS; j++) {
				if(i < ROWS -1) grid[i][j].addAdj(grid[i+1][j]);
				if(i > 0) grid[i][j].addAdj(grid[i-1][j]);
				if(j < COLS -1) grid[i][j].addAdj(grid[i][j+1]);
				if(j > 0) grid[i][j].addAdj(grid[i][j-1]);
			}
		}
	}
	
	public void calcTargets(TestBoardCell startCell, int pathlength) {
		targets.clear();
		visited.clear();
		findTargets(startCell, pathlength);
	}
	private void findTargets(TestBoardCell startCell, int pathlength) {
		for(TestBoardCell i : startCell.getAdjList()) {
			if (visited.contains(i) || i.isInRoom()) {
				continue;
			} else{
				visited.add(i);
				if (pathlength == 1 && !i.isOccupied()) {
					targets.add(i);
				} else {
					findTargets(i, pathlength-1);
				}
			}
			visited.remove(i);
		}
	}
	public Set<TestBoardCell> getTargets(){
		return targets;
	}
	public TestBoardCell getCell(int row, int col) {
		//just returning a new one, not correct, but it works
		return grid[row][col];
	}

}
