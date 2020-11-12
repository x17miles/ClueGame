package clueGame;

import java.awt.BorderLayout;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ClueGame extends JFrame{
	private Board board;
	private KnownCardsPanel cardPanel;
	private GameControlPanel controlPanel;
	private Random rand = new Random();
	
	public ClueGame(){
		//set up the jframe
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
		add(cardPanel,BorderLayout.EAST);
		
		//add the control panel component to the jframe, initialize it with the human player
		this.controlPanel = new GameControlPanel();
		controlPanel.setTurn(board.getPlayer("Jimbothy"), rand.nextInt(6)+1);
		controlPanel.setGuess("I have no guess");
		controlPanel.setGuessResult("Nothing");
		add(controlPanel, BorderLayout.SOUTH);
	}
	
	
	
	public static void main(String[] args) {
		ClueGame clueGame = new ClueGame();
		Random rand = new Random();
		Board board = Board.getInstance();
		board.calcTargets(board.getCell(board.getPlayer("Jimbothy").getPosition()[0],board.getPlayer("Jimbothy").getPosition()[1]), rand.nextInt(6)+1);
		clueGame.setVisible(true);
		JOptionPane.showMessageDialog(null,"You are Jimbothy, solve the murder before the other crew members!");
		
		
		
	}
	
	public void nextClicked() {
		
	}
}
