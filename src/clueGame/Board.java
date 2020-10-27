package clueGame;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;

public class Board {
	private Set<BoardCell> targets = new HashSet<BoardCell>();
	private BoardCell[][] grid;
	private Set<BoardCell> visited = new HashSet<BoardCell>();
	private int numRows,numColumns;
	private String layoutConfigFile;
	private String setupConfigFile;
	private Map<Character,Room> roomMap;
	private Map<Character,Room> spaces;
	
	private static Board theInstance = new Board();
	
	public Room getRoom(BoardCell cell) {
		return roomMap.get(cell.getInitial());
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
	public Room getRoom(char initial) {
		return roomMap.get(initial);
	}
	
	public void initialize(){
		//call load config files, if any bad config format exceptions are thrown, print out the error message
		try {
			loadConfigFiles();
		}
		catch(BadConfigFormatException e) {
			System.out.println(e.getMessage());
			
		}
	}
	
	public void loadConfigFiles() throws BadConfigFormatException {
			loadSetupConfig();
			loadLayoutConfig();
		
	}
	
	public void loadSetupConfig() throws BadConfigFormatException {
		ArrayList<String[]> setupVals = new ArrayList<String[]>();
		readSetupFile(setupVals);		
		parseSetupFile(setupVals);	
	}

	private void parseSetupFile(ArrayList<String[]> setupVals) throws BadConfigFormatException {
		this.roomMap = new HashMap<Character,Room>();
		for (String[] line : setupVals) {
			if(line[0].contains("//")) {
				continue;
			} else if (line[0].equals("Room") || line[0].equals("Space")) {
				//set up room information here
				this.roomMap.put(line[2].toCharArray()[0], new Room(line[1]));
				if(line[0].equals("Space")) {
					roomMap.get(line[2].toCharArray()[0]).setSpace(true);
				}
			} else {
				throw new BadConfigFormatException("Improperly formatted setup config file");
			}
		}
	}

	private void readSetupFile(ArrayList<String[]> setupVals) throws BadConfigFormatException {
		try {
			FileReader reader = new FileReader("data/"+setupConfigFile);
			Scanner in = new Scanner(reader);
		
			this.spaces = new HashMap<Character, Room>();
			while(in.hasNextLine()) {
				String tmpString = in.nextLine();
				setupVals.add(tmpString.split(", "));
			}
			in.close();
		}
		catch(FileNotFoundException e) {
			throw new BadConfigFormatException("Error: " + setupConfigFile + " not found.");
		}
	}
	
	public void loadLayoutConfig() throws BadConfigFormatException {
		ArrayList<String[]> layoutVals = new ArrayList<String[]>();
		
		readLayoutFile(layoutVals);
		parseLayoutFile(layoutVals);
		loadAdjLists();
	}

	private void parseLayoutFile(ArrayList<String[]> layoutVals) throws BadConfigFormatException {
		this.numColumns = layoutVals.get(0).length;
		this.numRows = layoutVals.size();
		grid = new BoardCell[layoutVals.size()][layoutVals.get(0).length];
		//error handle in this loop if the size differs / invalid characters etc.
		for (int i = 0; i<layoutVals.size(); i++) {
			for (int j = 0; j<layoutVals.get(0).length; j++) {
				try {
					//if cell isn't in setup files, then there is an issue
					if(!roomMap.containsKey(layoutVals.get(i)[j].charAt(0))) {
						throw new BadConfigFormatException("Error: cell added with improper cell initial");
					}
					grid[i][j] = new BoardCell(i,j,layoutVals.get(i)[j].charAt(0));
					
					//Handle multi-character cells, add appropriate info to the cell
					String val = layoutVals.get(i)[j];
					if(val.length()>1) {
						switch (val.charAt(1)) {
						case '^':
							grid[i][j].setDoorway(DoorDirection.UP);
							break;
						case '<':
							grid[i][j].setDoorway(DoorDirection.LEFT);
							break;
						case 'v':
							grid[i][j].setDoorway(DoorDirection.DOWN);
							break;
						case '>':
							grid[i][j].setDoorway(DoorDirection.RIGHT);
							break;
						case '*':
							grid[i][j].setRoomCenter(true);
							roomMap.get(val.charAt(0)).setCenterCell(grid[i][j]);
							break;
						case '#':
							grid[i][j].setRoomLabel(true);
							roomMap.get(val.charAt(0)).setLabelCell(grid[i][j]);
							break;
						default:
							//if the cell contains another room's initial, it is a secret passage, otherwise throw exception
							if(roomMap.containsKey(val.charAt(1)) && !roomMap.get(val.charAt(1)).isSpace()) grid[i][j].setSecretPassage(val.charAt(1));
							else {
								throw new BadConfigFormatException("Error: Invalid secondary cell character");
							}
							break;
						}
					}
				}
				catch(ArrayIndexOutOfBoundsException e) {
					throw new BadConfigFormatException("Error: Non standard rows, columns, or non-rectangular board shape");
				}
			}
		}
	}

	private void readLayoutFile(ArrayList<String[]> layoutVals) throws BadConfigFormatException {
		try {
			FileReader reader = new FileReader("data/"+layoutConfigFile);
			Scanner in = new Scanner(reader);
			while(in.hasNextLine()) {
				String tmpString = in.nextLine();
				layoutVals.add(tmpString.split(","));
			}
			in.close();
		}
		catch(FileNotFoundException e) {
			throw new BadConfigFormatException("Error " + layoutConfigFile + " file not found");
		}
	}
	
	private void loadAdjLists() {
		for (int i = 0; i < this.numRows; i++) {
			for (int j = 0; j < this.numColumns; j++) {
				
				//walkways
				
				if (roomMap.get(grid[i][j].getInitial()).getName().equals("Walkway")) {
					//if directly adjacent rooms are walkways, add them
					if(i >0 ) {
						if (roomMap.get(grid[i-1][j].getInitial()).getName().equals("Walkway")) grid[i][j].addAdj(grid[i-1][j]);
					} if (i < this.numRows -1) {
						if (roomMap.get(grid[i+1][j].getInitial()).getName().equals("Walkway")) grid[i][j].addAdj(grid[i+1][j]);
					} if(j >0 ) {
						if (roomMap.get(grid[i][j-1].getInitial()).getName().equals("Walkway")) grid[i][j].addAdj(grid[i][j-1]);
					} if (j < this.numColumns -1) {
						if (roomMap.get(grid[i][j+1].getInitial()).getName().equals("Walkway")) grid[i][j].addAdj(grid[i][j+1]);
					}
					//if the room is a doorway, add the room center to its list and vise versa (thus taking care of room center adjacent doorways)
					if(grid[i][j].isDoorway()) {
						switch (grid[i][j].getDoorDirection()){
						case UP:
							grid[i][j].addAdj(roomMap.get(grid[i-1][j].getInitial()).getCenterCell());
							roomMap.get(grid[i-1][j].getInitial()).getCenterCell().addAdj(grid[i][j]);
							break;
						case DOWN:
							grid[i][j].addAdj(roomMap.get(grid[i+1][j].getInitial()).getCenterCell());
							roomMap.get(grid[i+1][j].getInitial()).getCenterCell().addAdj(grid[i][j]);
							break;
						case LEFT:
							grid[i][j].addAdj(roomMap.get(grid[i][j-1].getInitial()).getCenterCell());
							roomMap.get(grid[i][j-1].getInitial()).getCenterCell().addAdj(grid[i][j]);
							break;
						case RIGHT:
							grid[i][j].addAdj(roomMap.get(grid[i][j+1].getInitial()).getCenterCell());
							roomMap.get(grid[i][j+1].getInitial()).getCenterCell().addAdj(grid[i][j]);
							break;
						case NONE:
						default:
							break;
						}
					}
				} //The only other adjacencies are those of secret passages, so add those as well
				
				else if (grid[i][j].isSecret()) {
					roomMap.get(grid[i][j].getInitial()).getCenterCell().addAdj(roomMap.get(grid[i][j].getSecretPassage()).getCenterCell());
				}
			}
		}
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
		visited.add(startCell);
		for(BoardCell i : startCell.getAdjList()) {
			//if it is visited or a room, skip it
			if (visited.contains(i) || i.isInRoom()) {
				continue;
			} else{
				visited.add(i);
				//if there is only one unit left to move and it isn't an occupied space, add it to targets
				if (pathlength == 1 && !i.isOccupied()) {
					targets.add(i);
				} else if (i.isRoomCenter()) {
					targets.add(i);
				}
				else if (pathlength <1) {
					return;
				}
				else if (!i.isOccupied()){
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
		return grid[row][col];
	}
	public Set<BoardCell> getAdjList(int row, int col){
		return grid[row][col].getAdjList();
	}

}
