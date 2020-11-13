package clueGame;

import java.awt.BorderLayout;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ClueGame extends JFrame{
	private static ClueGame theInstance = new ClueGame();
	private Board board;
	private KnownCardsPanel cardPanel;
	private GameControlPanel controlPanel;
	private Random rand = new Random();
	private Player currPlayer;
	private boolean moved;
	
	public static ClueGame getInstance() {
		return theInstance;
	}
	
	public ClueGame(){
		//set up the jframe
		moved = false;
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
		ArrayList<Player> players = board.getPlayerOrder();
		currPlayer = players.get((players.indexOf(currPlayer) +1 ) % players.size());
		cardPanel.setPlayer(currPlayer);
		int roll = rand.nextInt(6) + 1;
		controlPanel.setTurn(currPlayer, roll);
		board.calcTargets(board.getCell(currPlayer.getPosition()[0], currPlayer.getPosition()[1]), roll);
		moved = false;
	}
	public void processTurn() {
		// check accusation
		// check player movement
		// handle suggestion / run suggestion.
		// handle next click
	}
	public void cellClicked(BoardCell cell) {
		if(!moved && board.getTargets().contains(cell)) {
			currPlayer.setPosition(cell.getPosition()[0], cell.getPosition()[1]);
			moved = true;
		} else {
			if(!moved) {
				JOptionPane.showMessageDialog(null,"Please select a valid target.");
			} else {
				JOptionPane.showMessageDialog(null,"You have alread moved!");
			}
		}
	}
	
	//handle loading next player
	//rotate player to next in order
	//random roll
	//display targets
	
	//handle accusation
	//if accusation clicked, check if it matches the solution. if yes, win, if no, lose
	
	//assuring movement
	//player clicks correct location
	//adjust player location, redraw board
	
	//handle suggestion / disproof
	//if in room, run a suggestion. 
	
	//next handle
	//if player is done, moves to next
	
	
	
	
	
	public static void main(String[] args) {
		ClueGame clueGame = ClueGame.getInstance();
		JOptionPane.showMessageDialog(null,"You are Jimbothy, solve the murder before the other crew members!");
		clueGame.setVisible(true);
		//clueGame.loadNextPlayer();
		
		
	}
	
	public void nextClicked() {
		JOptionPane.showMessageDialog(null, "you clicked next");
	}
	public void accuseClicked() {
		JOptionPane.showMessageDialog(null, "you clicked accuse");
	}
}
