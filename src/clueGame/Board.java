package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Board extends JPanel{
	private Set<BoardCell> targets = new HashSet<BoardCell>();
	private BoardCell[][] grid;
	private Set<BoardCell> visited = new HashSet<BoardCell>();
	private int numRows,numColumns;
	private String layoutConfigFile;
	private String setupConfigFile;
	private Map<Character,Room> roomMap;
	private Set<Player> players;
	private ArrayList<Player> orderedPlayers;
	private Set<Card> weapons;
	private Set<Card> deck;
	private Set<Card> dealableCards;
	private Solution solution;
	private static Board theInstance = new Board();
	
	//Singleton Design Pattern
	private Board() {
		super();
	}
	
	public static Board getInstance() {
		return theInstance;
	}
	public void initialize(){
		//call load config files, if any bad config format exceptions are thrown, print out the error message
		try {
			loadConfigFiles();
			generatePlayerOrder();
		}
		catch(BadConfigFormatException e) {
			System.out.println(e.getMessage());
			
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		for(BoardCell[] i: grid) {
			for(BoardCell j: i) {
				j.paint(g,getWidth(), getHeight(),numColumns, numRows);
				if(j.isLabel()) {
					JLabel label = new JLabel(roomMap.get(j.getInitial()).getName());
					label.setBounds(j.getPosition()[1]*getWidth()/numColumns, j.getPosition()[0]*getHeight()/numRows, 100, getHeight()/numRows);
					add(label);
					
				}
			}
		}
	}
	
	public ArrayList<Player> getPlayerOrder(){
		return this.orderedPlayers;
	}
	
	//May not be necessary, but added just in case
	public void setPlayerOrder(ArrayList<Player> p) {
		this.orderedPlayers = p;
	}
	
	public void generatePlayerOrder() {
		this.orderedPlayers = new ArrayList<Player>();
		for(Player p: players) {
			orderedPlayers.add(p);
		}
	}
	
	public Card handleSuggestion(Solution suggestion, Player p) {
		//create an iterable list of the cards in the suggestion
		ArrayList<Card> suggestionCards = new ArrayList<Card>();
		suggestionCards.add(suggestion.person);
		suggestionCards.add(suggestion.room);
		suggestionCards.add(suggestion.weapon);
		//create a counter to go through the next player in line
		int counter = orderedPlayers.indexOf(p);
		
		//go through each player in the order, loop back until you get to the original player
		for(int i = counter+1; i!=counter; i++) {
			if(i >= orderedPlayers.size()) {
				i = 0;
			}
			//if the player has a card that is in the suggestion, return the card
			for(Card c: orderedPlayers.get(i).getHand()) {
				if(suggestionCards.contains(c)) {
					return c;
				}
			}
		}
		//if no cards counter it, return null
		return null;
	}
	
	public boolean checkAccusation(Solution accusation) {
		//.equals is overridden, so this is all we need to do
		if(this.solution.equals(accusation)) return true;
		return false;
	}
	public Solution getSolution() {
		return this.solution;
	}
	//largely used for testing
	public void setSolution(Solution s) {
		this.solution = s;
	}
	//get card from deck using cardName
	public Card getDeckCard(String cardName) {
		for(Card i : deck) {
			if(i.getName().equals(cardName)) return i;
		}
		return null;
	}
	
	public void makeDeck() {
		//First, go through rooms and players and create a card set for each of them
		Set<Card> RoomCards = new HashSet<Card>();
		for(Character i : roomMap.keySet()) {
			if(!roomMap.get(i).isSpace())
				RoomCards.add(new Card(roomMap.get(i).getName(), CardType.ROOM));
		}
		Set<Card> playerCards = new HashSet<Card>();
		for (Player i : players) {
			playerCards.add(new Card(i.getName(), CardType.PERSON));
		}
		
		//initialize the deck with the newly created card sets
		this.deck = new HashSet<Card>();
		deck.addAll(RoomCards);
		deck.addAll(playerCards);
		deck.addAll(weapons);
		
		//remove a random card from each set to be part of the solution
		buildSolution(RoomCards, playerCards);
	}

	private void buildSolution(Set<Card> RoomCards, Set<Card> playerCards) {
		Random r = new Random();
		Card[] solutionCards = new Card[3];
		int counter = r.nextInt(RoomCards.size());
		for(Card i : RoomCards) {
			if(counter == 0) {
				solutionCards[0] = i;
				RoomCards.remove(i);
				break;
			}
			counter--;
		}
		counter = r.nextInt(playerCards.size());
		for(Card i : playerCards) {
			if(counter == 0) {
				solutionCards[1] = i;
				playerCards.remove(i);
				break;
			}
			counter--;
		}
		counter = r.nextInt(weapons.size());
		for(Card i : weapons) {
			if(counter ==0) {
				solutionCards[2] = i;
				weapons.remove(i);
				break;
			}
			counter--;
		}
		
		this.solution = new Solution(solutionCards[1],solutionCards[0],solutionCards[2]);
		
		//dealable cards should be the leftovers after creating the solution
		this.dealableCards = new HashSet<Card>();
		dealableCards.addAll(RoomCards);
		dealableCards.addAll(playerCards);
		dealableCards.addAll(weapons);
	}
	public void dealHands() {
		//initialize a 2d array list to store the hands
		ArrayList<ArrayList<Card>> hands = new ArrayList<ArrayList<Card>>();
		for(int i = 0; i<players.size(); i++) {
			hands.add(new ArrayList<Card>());
		}
		//loop through all of the dealable cards and add it to a hand, looping back once
		// everyone has the same number of cards
		int handCounter = 1;
		for(Card c : dealableCards) {
			hands.get(handCounter-1).add(c);
			handCounter ++;
			if(handCounter > players.size()) handCounter=1;
		}
		//update the hands of each player
		int counter = 0;
		for(Player p : players) {
			for(Card i:hands.get(counter)) {
				p.updateHand(i);
			}
			counter ++;
		}
	}
	
	public void deal() {
		makeDeck();
		dealHands();
	}
	public Set<Card> getDeck(){
		return this.deck;
	}
	//get full set of players
	public Set<Player> getPlayers(){
		return this.players;
	}
	//get player by name
	public Player getPlayer(String name) {
		for(Player i : players) {
			if(i.getName().equals(name)) return i;
		}
		return null;
	}
	public Set<Card> getWeapons(){
		return this.weapons;
	}
	
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
		this.players = new HashSet<Player>();
		this.weapons = new HashSet<Card>();
		for (String[] line : setupVals) {
			if(line[0].contains("//")) {
				continue;
			} else if (line[0].equals("Room") || line[0].equals("Space")) {
				//set up room information here
				this.roomMap.put(line[2].toCharArray()[0], new Room(line[1]));
				if(line[0].equals("Space")) {
					roomMap.get(line[2].toCharArray()[0]).setSpace(true);
				}
				
				//add new players to the board's player list, differentiate between the human child and the computer child of Player
			} else if(line[0].equals("Player")){
				if(line[5].equals("Human")) {
					int[] location = new int[2];
					location[0] = Integer.parseInt(line[3]);
					location[1] = Integer.parseInt(line[4]);
					players.add(new HumanPlayer(line[1], location, line[2]));
				}
				else {
					int[] location = new int[2];
					location[0] = Integer.parseInt(line[3]);
					location[1] = Integer.parseInt(line[4]);
					players.add(new ComputerPlayer(line[1], location, line[2]));
				}
			//if it is a weapon, add it to the weapons cards	
			} else if(line[0].equals("Weapon")) {
				weapons.add(new Card(line[1], CardType.WEAPON));
			}
			
			//otherwise there is an error, and an exception should be thrown
			else {
				throw new BadConfigFormatException("Improperly formatted setup config file");
			}
		}
	}

	private void readSetupFile(ArrayList<String[]> setupVals) throws BadConfigFormatException {
		try {
			FileReader reader = new FileReader("data/"+setupConfigFile);
			Scanner in = new Scanner(reader);
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
								throw new BadConfigFormatException("Error: Invalid secondary cell character " + val.charAt(1));
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
	
	public Player getCardOwner(String cardName) {
		Card c = this.getDeckCard(cardName);
		for(Player p : players) {
			for(Card i : p.getHand()) {
				if (i.equals(c)) {
					return p;
				}
			}
		}
		return null;
	}

}
