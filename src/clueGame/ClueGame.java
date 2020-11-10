package clueGame;

import java.awt.BorderLayout;
import java.util.*;

import javax.swing.JFrame;

public class ClueGame extends JFrame{
	private Board board;
	private KnownCardsPanel cardPanel;
	private GameControlPanel controlPanel;
	private Random rand = new Random();
	
	public ClueGame(){
		this.setSize(1200,1200);
		this.setTitle("Clue Game");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.board = board.getInstance();
		this.board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		this.board.initialize();
		this.board.deal();
		add(board,BorderLayout.CENTER);
		this.cardPanel = new KnownCardsPanel();
		cardPanel.setPlayer(board.getPlayer("Jimbothy"));
		add(cardPanel,BorderLayout.EAST);
		this.controlPanel = new GameControlPanel();
		controlPanel.setTurn(board.getPlayer("Jimbothy"), rand.nextInt(6)+1);
		controlPanel.setGuess("I have no guess");
		controlPanel.setGuessResult("Nothing");
		add(controlPanel, BorderLayout.SOUTH);
	}
	
	public static void main(String[] args) {
		ClueGame clueGame = new ClueGame();
		clueGame.setVisible(true);
		clueGame.repaint();
	}
}
