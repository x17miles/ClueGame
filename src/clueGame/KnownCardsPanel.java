package clueGame;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.*;

public class KnownCardsPanel extends JPanel{
	private Player player;
	private ArrayList<Card> hand,seen;
	private ArrayList<JTextField> people,rooms,weaons;
	private JPanel mainPanel;
	
	public void setPlayer(Player p) {
		this.player = p;
		hand = player.getHand();
		seen = player.getSeen();
		remove(mainPanel);
		mainPanel = createKnownPanel();
		add(mainPanel);
		revalidate();
		repaint();
	}
	public KnownCardsPanel() {
		//initialize instance variables
		this.hand = new ArrayList<Card>();
		this.seen = new ArrayList<Card>();
		//initialize panel
		setLayout(new GridLayout (0, 1));
		mainPanel = createKnownPanel();
		add(mainPanel);
	}
	public JPanel createKnownPanel() {
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Known Cards"));
		panel.setLayout(new GridLayout(3,0));
		
		//People
		panel.add(createPanelsFromCardType(CardType.PERSON));
		panel.add(createPanelsFromCardType(CardType.ROOM));
		panel.add(createPanelsFromCardType(CardType.WEAPON));
		return panel;
	}
	
	public JPanel createPanelsFromCardType(CardType type) {
		JPanel panel = new JPanel();
		//we need to obtain the  jtextfields for the hand items and seen items
		ArrayList<JTextField> handItems = getHandItems(type);
		ArrayList<JTextField> seenItems = getSeenItems(type);
		//add a title
		panel.setBorder(new TitledBorder(new EtchedBorder(), type.getString()));
		//the size is 2 for labels + the total amount of JTextFields
		panel.setLayout(new GridLayout(2+handItems.size()+seenItems.size(), 0));
		
		//add in hand items
		JLabel inHandLabel = new JLabel("In Hand:");
		panel.add(inHandLabel);
		for(JTextField jtf : handItems) {
			panel.add(jtf);
		}
		//add seen items
		JLabel seenLabel = new JLabel("Seen:");
		panel.add(seenLabel);
		for(JTextField jtf : seenItems) {
			panel.add(jtf);
		}
		return panel;
		
	}
	public ArrayList<JTextField> getHandItems(CardType type){
		ArrayList<JTextField> handItems = new ArrayList<JTextField>();
		//loop through cards in hand
		for(Card c : hand) {
			//if the type matches, create a JTextField of the object
			if(c.getType() == type) {
				JTextField textField = new JTextField(20);
				textField.setEditable(false);
				textField.setText(c.getName());
				handItems.add(textField);
			}
		}
		//if no seen items, return a none block
		if(handItems.size() == 0) {
			handItems.add(createNoneBlock());
		}
		return handItems;
	}
	public ArrayList<JTextField> getSeenItems(CardType type){
		ArrayList<JTextField> seenItems = new ArrayList<JTextField>();
		//loop through all cards in seen
		for(Card c : seen) {
			//if it matches the type, add a jtextfield of the object
			if(c.getType() == type && !hand.contains(c)) {
				JTextField textField = new JTextField(20);
				textField.setEditable(false);
				textField.setText(c.getName());
				seenItems.add(textField);
			}
		}
		//if none, add a none block
		if(seenItems.size() == 0) {
			seenItems.add(createNoneBlock());
		}
		return seenItems;
	}
	//create a block that says NONE
	public JTextField createNoneBlock() {
		JTextField noneBlock = new JTextField(20);
		noneBlock.setEditable(false);
		noneBlock.setText("None");
		return noneBlock;
	}
	
	public static void main(String[] args) {
		KnownCardsPanel panel = new KnownCardsPanel();  // create the panel
		JFrame frame = new JFrame();  // create the frame
        frame.setContentPane(panel); // put the panel in the frame
        frame.setSize(180, 800);  // size the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
        frame.setVisible(true); // make it visible
        
        //add player w/ all cards
        Board mainBoard = Board.getInstance();
        mainBoard.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
        mainBoard.initialize();
        mainBoard.deal();
        Player p = mainBoard.getPlayer("Jimbothy");
        p.clearSeen();
        for(Card c : mainBoard.getDeck()) {
        	if(!p.getHand().contains(c)) {
        		p.updateSeen(c);
        	}
        }
        //update the panel
        panel.setPlayer(p);
	}

}
