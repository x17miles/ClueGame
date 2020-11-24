package clueGame;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.hamcrest.core.IsInstanceOf;

public class ClueGame extends JFrame{
	private String human;
	private static ClueGame theInstance = new ClueGame();
	private boolean win;
	private boolean dead;
	private Board board;
	private KnownCardsPanel cardPanel;
	private GameControlPanel controlPanel;
	private Random rand = new Random();
	private Player currPlayer;
	private boolean moved;
	private boolean suggestionFlag;
	private Room draggedFlag;
	
	//singleton version of game engine
	public static ClueGame getInstance() {
		return theInstance;
	}
	
	public ClueGame(){
		//set up the jframe
		moved = false;
		suggestionFlag = false;
		this.setSize(1200,1200);
		this.setTitle("Clue Game");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//initialize the board
		this.board = board.getInstance();
		this.board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		this.board.initialize();
		this.board.deal();
		for(Player p : board.getPlayers()) {
			if(p instanceof HumanPlayer) {
				this.human = p.getName();
			}
		}
		
		//add the board component to the jframe
		add(board,BorderLayout.CENTER);
		
		//add the cards component to the jframe, set the player to the human player
		this.cardPanel = new KnownCardsPanel();
		cardPanel.setPlayer(board.getPlayer(human));
		this.currPlayer = board.getPlayer(human);
		add(cardPanel,BorderLayout.EAST);
		
		//add the control panel component to the jframe, initialize it with the human player
		this.controlPanel = new GameControlPanel();
		controlPanel.setTurn(board.getPlayer(human), rand.nextInt(6)+1);
		controlPanel.setGuess("I have no guess");
		controlPanel.setGuessResult("Nothing");
		add(controlPanel, BorderLayout.SOUTH);
		board.calcTargets(board.getCell(board.getPlayer(human).getPosition()[0],board.getPlayer(human).getPosition()[1]), rand.nextInt(6)+1);
	}

	public void loadNextPlayer() {
		repaint();
		//don't play if the player died
		if(currPlayer.isDead()) {
			return;
		}
		//reset class flags and display text
		this.win = false;
		this.dead = false;
		this.controlPanel.setGuess("No guess so far");
		this.controlPanel.setGuessResult("No results");
		
		//get ordered player list
		ArrayList<Player> players = board.getPlayerOrder();
		
		//set the current player as next in line
		currPlayer = players.get((players.indexOf(currPlayer) +1 ) % players.size());
		
		//random roll
		int roll = rand.nextInt(6) + 1;
		
		//update control panel
		controlPanel.setTurn(currPlayer, roll);
		
		//get new targets from random roll
		board.calcTargets(board.getCell(currPlayer.getPosition()[0], currPlayer.getPosition()[1]), roll);
		if(currPlayer.getDraggedRoom() != null) {
			board.appendTargets(currPlayer.getDraggedRoom().getCenterCell());
			this.draggedFlag = currPlayer.getDraggedRoom();
			currPlayer.setDraggedRoom(null);
			
			
		}
		//reset turn movement flags
		moved = false;
		suggestionFlag = false;
		if(!currPlayer.getName().equals(human)) {
			//run through automated turn if computer
			processComputerTurn();
		}
	}
	public void processComputerTurn() {
		//accusation
		if(currPlayer.getAccusation() != null) {
			if(board.checkAccusation(currPlayer.getAccusation())) {
				win = true;
				JOptionPane.showMessageDialog(null, currPlayer.getName() + " Won!!!\n" + "Person: " + currPlayer.getAccusation().person.getName()
						+ "\nRoom: " + currPlayer.getAccusation().room.getName() + "\nWeapon: " + currPlayer.getAccusation().weapon.getName());
			} else {
				dead = true;
				JOptionPane.showMessageDialog(null, currPlayer.getName() + " died. Their accusation was wrong."+currPlayer.getAccusation().person.getName()
						+ "\nRoom: " + currPlayer.getAccusation().room.getName() + "\nWeapon: " + currPlayer.getAccusation().weapon.getName());
			}
			this.handleAccuseResult();
			loadNextPlayer();
			//if accusation made, return to end turn
			return;
		}
		
		//get a new target
		BoardCell target = currPlayer.selectTargets();
		if(draggedFlag != null && !target.equals(draggedFlag.getCenterCell())) {
			draggedFlag.removePlayer(currPlayer);
		}
		
		//adjust position
		currPlayer.setPosition(target.getPosition()[0], target.getPosition()[1]);
		moved = true;
		
		//if a room center, handle the suggestion
		if(target.isRoomCenter()) {
			board.getRoom(target).addPlayer(currPlayer);
			handleSuggestion(board.getRoom(target));
			
		}
		//repaint to show changes
		repaint();
	}
	
	public void cellClicked(BoardCell cell) {
		//it must be human player's turn
		if(currPlayer.getName().equals(human)) {
			
			//if the cell is in the target list or in a room in the target list, adjust appropriately
			if(!moved && (board.getTargets().contains(cell) || board.getTargets().contains(board.getRoom(cell).getCenterCell() ))) {
				//remove the occupied tag from the cell the player is currently in
				board.getCell(currPlayer.getPosition()[0], currPlayer.getPosition()[1]).setOccupied(false);
				
				//Remove player from the occupied room if they were dragged
				if(draggedFlag != null) {
					draggedFlag.removePlayer(currPlayer);
				}
				
				//If were in a room, remove them from the room's list of players
				if(board.getCell(currPlayer.getPosition()[0], currPlayer.getPosition()[1]).getInitial() != 'W') {
					board.getRoom(board.getCell(currPlayer.getPosition()[0], currPlayer.getPosition()[1])).removePlayer(currPlayer);
				}
				
				//update their position to the cell they clicked
				currPlayer.setPosition(cell.getPosition()[0], cell.getPosition()[1]);
				
				//if they clicked a cell in a target room, update the position to the center cell of the room
				if(board.getTargets().contains(board.getRoom(cell).getCenterCell())) {
					BoardCell centerCell = board.getRoom(cell).getCenterCell();
					currPlayer.setPosition(centerCell.getPosition()[0], centerCell.getPosition()[1]);
					handleSuggestion(board.getRoom(cell));
					board.getRoom(cell).addPlayer(currPlayer);
				}
				
				//set an occupied flag on their current cell and moved flag for the turn
				board.getCell(currPlayer.getPosition()[0], currPlayer.getPosition()[1]).setOccupied(true);
				moved = true;
				
				//if they chose a room, handle the suggestion
				if(cell.isRoomCenter()) {
					handleSuggestion(board.getRoom(cell));
				}
				
			} else {
				//throw error messages for wrong moves
				if(!moved) {
					JOptionPane.showMessageDialog(null,"Please select a valid target.");
				} else {
					JOptionPane.showMessageDialog(null,"You have alread moved!");
				}
			}
		} else {
			JOptionPane.showMessageDialog(null,"It is not your turn.");
		}
		repaint();
	}
	public void handleSuggestion(Room room) {
		// player must craft a suggestion
		Solution suggestion;
		if(currPlayer.getName().equals(human)) {
			//make a panel for making a suggestion
			JPanel suggestionPanel = new JPanel();
			suggestionPanel.setLayout(new GridLayout(3,2));
			JLabel currRoom = new JLabel("Current Room");
			JTextField roomText = new JTextField(room.getName());
			roomText.setEditable(false);
			JLabel person = new JLabel("Person");
			JComboBox peopleDrop = new JComboBox();
			for(Card i : board.getDeckType(CardType.PERSON)) {
				peopleDrop.addItem(i.getName());
			}
			//peopleDrop.addActionListener(new ComboActionListener());
			JLabel weapon = new JLabel("Weapon");
			JComboBox weaponDrop = new JComboBox();
			for(Card i : board.getDeckType(CardType.WEAPON)) {
				weaponDrop.addItem(i.getName());
			}
			suggestionPanel.add(currRoom);
			suggestionPanel.add(roomText);
			suggestionPanel.add(person);
			suggestionPanel.add(peopleDrop);
			suggestionPanel.add(weapon);
			suggestionPanel.add(weaponDrop);
			
			//a suggestion should be forced, only allow clicking the ok button
			JOptionPane.showMessageDialog(null, suggestionPanel);
			
			//make a suggestion from the result
			suggestion = new Solution(board.getDeckCard(peopleDrop.getSelectedItem().toString()), board.getDeckCard(room.getName()), board.getDeckCard(weaponDrop.getSelectedItem().toString())) ;
			
			
		} else {
			//if computer player, run ai suggestion
			suggestion = currPlayer.createSuggestion(room);
			
		}
		
		//get disproof if it exists
		Card disproof = board.handleSuggestion(suggestion, currPlayer);
		
		//reprint card panel as applicable
		if(currPlayer.getName().equals(human)) {
			this.cardPanel.setPlayer(currPlayer);
		}
		
		//print guess result
		this.controlPanel.setGuess(suggestion.person.getName() +  " in the " + suggestion.room.getName() + " with the " + suggestion.weapon.getName());
		if (disproof != null) {
			if(currPlayer.getName().equals(human)) {
				this.controlPanel.setGuessResult(board.getCardOwner(disproof.getName()).getName() + " disproved with card: " + disproof.getName());
			} else {
				this.controlPanel.setGuessResult(board.getCardOwner(disproof.getName()).getName() + " proved this false");
				
			}
		} else {
			this.controlPanel.setGuessResult("Nobody disproved the suggestion.");
			currPlayer.setAccusation(suggestion);
		}

		suggestionFlag = true;
		
		//move player of suggestion
		if(!currPlayer.getName().equals(suggestion.person.getName())){
			board.getPlayer(suggestion.person.getName()).setPosition(room.getCenterCell().getPosition()[0], room.getCenterCell().getPosition()[1]);
			board.getPlayer(suggestion.person.getName()).setDraggedRoom(room);
			room.addPlayer(board.getPlayer(suggestion.person.getName()));
		}
		repaint();
	}
		
	public void nextClicked() {
		//make sure they have moved
		if(this.moved) {
			//if in a room, make sure they made a suggestion
			if( board.getCell(currPlayer.getPosition()[0], currPlayer.getPosition()[1]).getInitial() != 'W' && this.suggestionFlag) {
				loadNextPlayer();
			} else if (board.getCell(currPlayer.getPosition()[0], currPlayer.getPosition()[1]).getInitial() == 'W') {
				loadNextPlayer();
			} else {
				
		//throw appropriate error messages
				if(!currPlayer.isDead()) JOptionPane.showMessageDialog(null,"A suggestion needs to be made");
			}
		} else if(!currPlayer.isDead()) {
			 JOptionPane.showMessageDialog(null, currPlayer.getName() + " needs to move.");
		} else if(currPlayer.isDead()) {
			//if they are dead, move on
			loadNextPlayer();
		}
	}
	
	
	public void accuseClicked() {
		//if not player's turn, they cannot click accuse
		if(!currPlayer.getName().equals(human)) {
			JOptionPane.showMessageDialog(null, "It's not your turn.");
			return;
		}
		
		//otherwise, make a suggestion via dropdown dialog
		Solution suggestion;
		JPanel suggestionPanel = new JPanel();
		suggestionPanel.setLayout(new GridLayout(3,2));
		JLabel currRoom = new JLabel("Current Room");
		JComboBox roomDrop = new JComboBox();
		for(Card i : board.getDeckType(CardType.ROOM)) {
			roomDrop.addItem(i.getName());
		}
		
		JLabel person = new JLabel("Person");
		JComboBox peopleDrop = new JComboBox();
		for(Card i : board.getDeckType(CardType.PERSON)) {
			peopleDrop.addItem(i.getName());
		}
		JLabel weapon = new JLabel("Weapon");
		JComboBox weaponDrop = new JComboBox();
		for(Card i : board.getDeckType(CardType.WEAPON)) {
			weaponDrop.addItem(i.getName());
		}
		suggestionPanel.add(currRoom);
		suggestionPanel.add(roomDrop);
		suggestionPanel.add(person);
		suggestionPanel.add(peopleDrop);
		suggestionPanel.add(weapon);
		suggestionPanel.add(weaponDrop);
		JOptionPane.showMessageDialog(null, suggestionPanel);
		
		//extract the suggestion from the dialog
		suggestion = new Solution(board.getDeckCard(peopleDrop.getSelectedItem().toString()), board.getDeckCard(roomDrop.getSelectedItem().toString()), board.getDeckCard(weaponDrop.getSelectedItem().toString())) ;
		boolean result = board.checkAccusation(suggestion);
		
		//handle result
		if(result) {
			JOptionPane.showMessageDialog(null, "You Win!!! :)");
			win=true;
		} else {
			JOptionPane.showMessageDialog(null, "You Lose! :(" );
			dead = true;
		}
		handleAccuseResult();
	}
	
	public void handleAccuseResult() {
		if(currPlayer.getName().equals(human) || win) {
			setVisible(false);
			dispose();
		} else {
			currPlayer.setDead(dead);
		}
	}
	
	
	public static void main(String[] args) {
		//get the clue game instance
		ClueGame clueGame = ClueGame.getInstance();
		//initial message
		JOptionPane.showMessageDialog(null,"You are Jimbothy, solve the murder before the other crew members!");
		//run the game
		clueGame.setVisible(true);
		
		
	}
	
	
}
