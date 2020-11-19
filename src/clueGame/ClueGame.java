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
		
		//add the board component to the jframe
		add(board,BorderLayout.CENTER);
		
		//add the cards component to the jframe, set the player to the human player
		this.cardPanel = new KnownCardsPanel();
		cardPanel.setPlayer(board.getPlayer("Jimbothy"));
		this.currPlayer = board.getPlayer("Jimbothy");
		add(cardPanel,BorderLayout.EAST);
		
		//add the control panel component to the jframe, initialize it with the human player
		this.controlPanel = new GameControlPanel();
		controlPanel.setTurn(board.getPlayer("Jimbothy"), rand.nextInt(6)+1);
		controlPanel.setGuess("I have no guess");
		controlPanel.setGuessResult("Nothing");
		add(controlPanel, BorderLayout.SOUTH);
		board.calcTargets(board.getCell(board.getPlayer("Jimbothy").getPosition()[0],board.getPlayer("Jimbothy").getPosition()[1]), rand.nextInt(6)+1);
	}
	
	public void loadNextPlayer() {
		repaint();
		//get ordered player list
		ArrayList<Player> players = board.getPlayerOrder();
		//set the current player as next in line
		currPlayer = players.get((players.indexOf(currPlayer) +1 ) % players.size());
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
		//reset turn flags
		moved = false;
		suggestionFlag = false;
		if(!currPlayer.getName().equals("Jimbothy")) {
			//run through automated turn if computer
			processComputerTurn();
		}
	}
	public void processComputerTurn() {
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
		if(currPlayer.getName().equals("Jimbothy")) {
			//if the cell is in the target list or in a room in the target list, adjust appropriately
			if(!moved && (board.getTargets().contains(cell) || board.getTargets().contains(board.getRoom(cell).getCenterCell() ))) {
				if(board.getCell(currPlayer.getPosition()[0], currPlayer.getPosition()[1]).getInitial() != 'W') {
					board.getRoom(board.getCell(currPlayer.getPosition()[0], currPlayer.getPosition()[1])).removePlayer(currPlayer);
				}
				currPlayer.setPosition(cell.getPosition()[0], cell.getPosition()[1]);
				if(board.getTargets().contains(board.getRoom(cell).getCenterCell())) {
					BoardCell centerCell = board.getRoom(cell).getCenterCell();
					currPlayer.setPosition(centerCell.getPosition()[0], centerCell.getPosition()[1]);
					handleSuggestion(board.getRoom(cell));
					board.getRoom(cell).addPlayer(currPlayer);
				}
				moved = true;
				if(cell.isRoomCenter()) {
					handleSuggestion(board.getRoom(cell));
				}
				if(draggedFlag != null) {
					draggedFlag.removePlayer(currPlayer);
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
		// player must craft a suggestion, currently blocked by a message
		Solution suggestion;
		if(currPlayer.getName().equals("Jimbothy")) {
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
			JOptionPane.showMessageDialog(null, suggestionPanel);
			//JOptionPane.showMessageDialog(null, weaponDrop.getSelectedItem().toString());
			suggestion = new Solution(board.getDeckCard(peopleDrop.getSelectedItem().toString()), board.getDeckCard(room.getName()), board.getDeckCard(weaponDrop.getSelectedItem().toString())) ;
			//JOptionPane.showMessageDialog(null,"You need to make a suggestion");
			//JOptionPane.showConfirmDialog(null, "Message");
		} else {
			suggestion = currPlayer.createSuggestion(room);
			
		}
		Card disproof = board.handleSuggestion(suggestion, currPlayer);
		if(currPlayer.getName().equals("Jimbothy")) {
			JOptionPane.showMessageDialog(null, "Your suggestion was disproved with card: " + disproof.getName());
			this.cardPanel.setPlayer(currPlayer);
		}

		suggestionFlag = true;
		//move player
		board.getPlayer(suggestion.person.getName()).setPosition(room.getCenterCell().getPosition()[0], room.getCenterCell().getPosition()[1]);
		board.getPlayer(suggestion.person.getName()).setDraggedRoom(room);
		room.addPlayer(board.getPlayer(suggestion.person.getName()));
		repaint();
	}
	private class ComboActionListener implements ActionListener {
		Card selectedCard;
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			JComboBox cb = (JComboBox)e.getSource();
			this.selectedCard = board.getDeckCard((String)cb.getSelectedItem());
		}
		
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
				JOptionPane.showMessageDialog(null,"A suggestion needs to be made");
			}
		} else {
			JOptionPane.showMessageDialog(null, currPlayer.getName() + " needs to move.");
		}
	}
	public void accuseClicked() {
		//stubbed w/ error message for now
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
		//peopleDrop.addActionListener(new ComboActionListener());
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
		
		suggestion = new Solution(board.getDeckCard(peopleDrop.getSelectedItem().toString()), board.getDeckCard(roomDrop.getSelectedItem().toString()), board.getDeckCard(weaponDrop.getSelectedItem().toString())) ;
		boolean result = board.checkAccusation(suggestion);
		if(result) {
			JOptionPane.showMessageDialog(null, "You Win!!! :)");
			win=true;
		} else {
			JOptionPane.showMessageDialog(null, "You Lose! :(" );
			dead = true;
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
