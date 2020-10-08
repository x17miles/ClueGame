package clueGame;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Board {
	private Set<BoardCell> targets;
	private BoardCell[][] grid;
	private Set<BoardCell> visited;
	private int numRows,numColumns;
	private String layoutConfigFile;
	private String setupConfigFile;
	private Map<Character,Room> roomMap;
	
	private static Board theInstance = new Board();
	
	public Room getRoom(BoardCell cell) {
		return new Room();
	}
	
	public int getNumRows(){
		return this.numRows;
	}
	public int getNumColumns() {
		return this.numColumns;
	}
	
	public void setConfigFiles(String layout, String setup) {
		this.layoutConfigFile = layout;
		this.setupConfigFile = setup;
	}
	public Room getRoom(char c) {
		//return roomMap.get(c);
		//stub
		return new Room();
	}
	
	public void initialize(){
	
	}
	
	public void loadConfigFiles() throws FileNotFoundException {
		loadSetupConfig();
		loadLayoutConfig();
	}
	
	public void loadSetupConfig() throws FileNotFoundException {
		ArrayList<ArrayList<String>> vals = new ArrayList<ArrayList<String>>();
		FileReader reader = new FileReader(setupConfigFile);
		Scanner in = new Scanner(reader);
		while(in.hasNextLine()) {
			String tmpString = in.nextLine();
			vals.add(new ArrayList<String>(tmpString.split(",")));
		}
	}
	
	public void loadLayoutConfig() throws FileNotFoundException {
		FileReader reader = new FileReader(layoutConfigFile);
	}
	
	//Singleton Design Pattern
	private Board() {
		super();
	}
	
	public static Board getInstance() {
		return theInstance;
	}
	
	public void calcTargets(BoardCell startCell, int pathlength) {
		//clear out the targets and visited sets
		targets.clear();
		visited.clear();
		//call the private helper function
		findTargets(startCell, pathlength);
	}
	
	//private helper function to recursively call add targets
	private void findTargets(BoardCell startCell, int pathlength) {
		//iterate through each cell in the current cell's adjList
		for(BoardCell i : startCell.getAdjList()) {
			//if it is visited or a room, skip it
			if (visited.contains(i) || i.isInRoom()) {
				continue;
			} else{
				visited.add(i);
				//if there is only one unit left to move and it isn't an occupied space, add it to targets
				if (pathlength == 1 && !i.isOccupied()) {
					targets.add(i);
				} else {
					//otherwise recursively find the next step
					findTargets(i, pathlength-1);
				}
			}
			visited.remove(i);
		}
	}
	public Set<BoardCell> getTargets(){
		return targets;
	}
	public BoardCell getCell(int row, int col) {
		//just returning a new one, not correct, but it works
		//return grid[row][col];
		//stubbed version
		return new BoardCell(row,col);
	}

}
