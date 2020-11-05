package clueGame;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class GameControlPanel extends JPanel{
	private JTextField turn;
	private JTextField rollAmount;
	private JButton accuse;
	private JButton next;
	private JTextField guess;
	private JTextField guessResult;
	
	public void setGuess(String guess) {
		this.guess.setText(guess);
	}
	
	public void setGuessResult(String guessResult) {
		this.guessResult.setText(guessResult);
	}

	public void setTurn(Player p, int roll) {
		this.turn.setText(p.getName());
		this.turn.setForeground(p.getColorType());
		this.rollAmount.setText(Integer.toString(roll));
	}

	public GameControlPanel() {
		//Initiallizing private variables
		turn = new JTextField(20);
		turn.setEditable(false);
		rollAmount = new JTextField(5);
		rollAmount.setEditable(false);
		accuse = new JButton("Make Accusation");
		next = new JButton("NEXT!");
		guess = new JTextField(20);
		guess.setEditable(false);
		guessResult = new JTextField(20);
		guessResult.setEditable(false);
		//Initialize board
		setLayout(new GridLayout(2,0));
		JPanel panel = createRowOne();
		add(panel);
		panel = createRowTwo();
		add(panel);
		
	}
	
	private JPanel createRowOne() {
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,4));
		
		//First column
		JPanel colOne = new JPanel();
		colOne.setLayout(new GridLayout(2,0));
		JLabel whoseTurn = new JLabel("Whose Turn?");
		colOne.add(whoseTurn);
		colOne.add(turn);
		
		//Second column
		JPanel colTwo = new JPanel();
		colTwo.setLayout(new GridLayout(0,2));
		JLabel roll = new JLabel("Roll:");
		colTwo.add(roll);
		colTwo.add(rollAmount);
		
		panel.add(colOne);
		panel.add(colTwo);
		panel.add(accuse);
		panel.add(next);
		return panel;
	}
	
	private JPanel createRowTwo() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,2));
		
		//First column
		JPanel panel1 = new JPanel();
		panel1.setBorder(new TitledBorder(new EtchedBorder(),"Guess"));
		panel1.add(guess);
		
		//Second column
		JPanel panel2 = new JPanel();
		panel2.setBorder(new TitledBorder(new EtchedBorder(),"Guess Result"));
		panel2.add(guessResult);
		
		panel.add(panel1);
		panel.add(panel2);
		return panel;
	}
	
	public static void main(String[] args) {
        GameControlPanel panel = new GameControlPanel();  // create the panel
        JFrame frame = new JFrame();  // create the frame
        frame.setContentPane(panel); // put the panel in the frame
        frame.setSize(750, 180);  // size the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
        frame.setVisible(true); // make it visible
        int[] startLoc = {0,0};
        panel.setTurn(new ComputerPlayer("Captain", startLoc, "red"),3);

        // test filling in the data
        panel.setGuess( "I have no guess!");
        panel.setGuessResult( "So you have nothing?");
 }
}
