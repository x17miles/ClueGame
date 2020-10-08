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
	public Room getRoom(char c) {
		//return roomMap.get(c);
		//stub
		return roomMap.get(c);
	}
	
	public void initialize(){
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
		ArrayList<String[]> vals = new ArrayList<String[]>();
		try {
			FileReader reader = new FileReader("data/"+setupConfigFile);
			Scanner in = new Scanner(reader);
		
			this.spaces = new HashMap<Character, Room>();
			while(in.hasNextLine()) {
				String tmpString = in.nextLine();
				vals.add(tmpString.split(", "));
			}
			in.close();
		}
		catch(FileNotFoundException e) {
			throw new BadConfigFormatException("Error: " + setupConfigFile + " not found.");
		}
		
		this.roomMap = new HashMap<Character,Room>();
		for (String[] line : vals) {
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
	
	public void loadLayoutConfig() throws BadConfigFormatException {
		ArrayList<String[]> vals = new ArrayList<String[]>();
		
		try {
			FileReader reader = new FileReader("data/"+layoutConfigFile);
			Scanner in = new Scanner(reader);
			while(in.hasNextLine()) {
				String tmpString = in.nextLine();
				vals.add(tmpString.split(","));
			}
			in.close();
		}
		catch(FileNotFoundException e) {
			throw new BadConfigFormatException("Error " + layoutConfigFile + " file not found");
		}
		this.numColumns = vals.get(0).length;
		this.numRows = vals.size();
		grid = new BoardCell[vals.size()][vals.get(0).length];
		//error handle in this loop if the size differs / invalid characters etc.
		for (int i = 0; i<vals.size(); i++) {
			for (int j = 0; j<vals.get(0).length; j++) {
				try {
					//if cell isn't in setup files, then there is an issue
					if(!roomMap.containsKey(vals.get(i)[j].charAt(0))) {
						throw new BadConfigFormatException("Error: cell added with improper cell initial");
					}
					grid[i][j] = new BoardCell(i,j,vals.get(i)[j].charAt(0));
					
					//Handle multi-character cells, add appropriate info to the cell
					String val = vals.get(i)[j];
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
		return grid[row][col];
	}

}
