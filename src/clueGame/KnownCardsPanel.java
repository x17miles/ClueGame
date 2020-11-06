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
		ArrayList<JTextField> handItems = getHandItems(type);
		ArrayList<JTextField> seenItems = getSeenItems(type);
		panel.setBorder(new TitledBorder(new EtchedBorder(), type.getString()));
		panel.setLayout(new GridLayout(2+handItems.size()+seenItems.size(), 0));
		JLabel inHandLabel = new JLabel("In Hand:");
		panel.add(inHandLabel);
		for(JTextField jtf : handItems) {
			panel.add(jtf);
		}
		JLabel seenLabel = new JLabel("Seen:");
		panel.add(seenLabel);
		for(JTextField jtf : seenItems) {
			panel.add(jtf);
		}
		return panel;
		
	}
	public ArrayList<JTextField> getHandItems(CardType type){
		ArrayList<JTextField> handItems = new ArrayList<JTextField>();
		for(Card c : hand) {
			if(c.getType() == type) {
				JTextField textField = new JTextField(20);
				textField.setEditable(false);
				textField.setText(c.getName());
				handItems.add(textField);
			}
		}
		if(handItems.size() == 0) {
			handItems.add(createNoneBlock());
		}
		return handItems;
	}
	public ArrayList<JTextField> getSeenItems(CardType type){
		ArrayList<JTextField> seenItems = new ArrayList<JTextField>();
		for(Card c : seen) {
			if(c.getType() == type && !hand.contains(c)) {
				JTextField textField = new JTextField(20);
				textField.setEditable(false);
				textField.setText(c.getName());
				seenItems.add(textField);
			}
		}
		if(seenItems.size() == 0) {
			seenItems.add(createNoneBlock());
		}
		return seenItems;
	}
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
        Board board = Board.getInstance();
        board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
        board.initialize();
        board.deal();
        Player p = board.getPlayer("Captain");
        p.clearSeen();
        for(Card c : board.getDeck()) {
        	if(!p.getHand().contains(c)) {
        		p.updateSeen(c);
        	}
        }
        panel.setPlayer(p);
	}

}
