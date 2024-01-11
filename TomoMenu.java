import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class TomoMenu extends JPanel implements ActionListener{
	// size of the rectangle
	public static final int MENU_WIDTH = 300;
	public static final int MENU_HEIGHT = 550;
	
	JButton timerButton;
	JButton checkListButton;
	JButton newNotesButton;
	
	// TODO
	JButton[] backGroundButtons = {new JButton("1"), new JButton("2"), new JButton("3"), new JButton("4"), new JButton("5")};
	String[] dailyQuote = {"wow, awesome", "wow2", "epic, so epic", "w"};
	
	int todaysQuoteIndex;
	int buttonRightShift;
	
	int currentBg;
	
	public Container c;
	public TomoFrame frame;
	
	public TomoMenu(Container c, TomoFrame frame) {
		//super(850, 50, MENU_WIDTH, MENU_HEIGHT);
		this.c = c;
		this.frame = frame;
		
		currentBg = 0;
		
		timerButton = new JButton("TIMER");
		checkListButton = new JButton("CHECKLIST");
		newNotesButton = new JButton("ADD A NEW NOTE");
		
		c.add(timerButton);
		timerButton.setBounds(875,75,90,90);
		
		c.add(checkListButton);
		checkListButton.setBounds(995,75,90,90);
		
		c.add(newNotesButton);
		newNotesButton.setBounds(875,195,255,50);
		
		buttonRightShift = 0;
		
		for (JButton jB : backGroundButtons) {
			c.add(jB);
			jB.setBounds(870 + buttonRightShift,320,45,45);
			buttonRightShift = buttonRightShift + 55;
			
			jB.addActionListener(this);
		}
		
		timerButton.addActionListener(this);
		checkListButton.addActionListener(this);
		newNotesButton.addActionListener(this);
		
		// TODO add an "if it's a new day" checker before generating index
		// also if more quotes are added change multiplier
		
		todaysQuoteIndex = (int)((Math.random()*(4)));
	}
    
	public void draw(Graphics g) {

		// draws the rectangle
		g.drawRect(850, 50, MENU_WIDTH, MENU_HEIGHT);
		
		g.drawRect(70, 50, 10, 10);
		
		g.drawString("CURRENT ROOM:", 875, 275);
		
		if(currentBg == 0) {
			g.drawString("BLANK FRAME OF AN INCOMPLETE PROJECT", 875, 300);
		} else if (currentBg == 1) {
			g.drawString("BLINDING BLUE", 875, 300);
		} else if (currentBg == 2) {
			g.drawString("GREENSCREEN", 875, 300);
		} else if (currentBg == 3) {
			g.drawString("IT'S NOT PURPLE", 875, 300);
		} else if (currentBg == 4) {
			g.drawString("CLOSEUP OF AN APPLE", 875, 300);
		} 
		
		g.drawString("DAILY QUOTE:", 875, 400);
		g.drawString(dailyQuote[todaysQuoteIndex], 875, 415);
		
	}
	
	public void actionPerformed(ActionEvent evt){
		// TODO set up all buttons reactions to being clicked
		
		if (evt.getSource() == timerButton) {
			Pomodoro.timerVis = Pomodoro.timerVis * -1;
		} else if (evt.getSource() == backGroundButtons[0]) {
			currentBg = 0;
			frame.frameBgChange(Color.white);
		} else if (evt.getSource() == backGroundButtons[1]) {
			currentBg = 1;
			frame.frameBgChange(Color.blue);
		} else if (evt.getSource() == backGroundButtons[2]) {
			currentBg = 2;
			frame.frameBgChange(Color.green);
		} else if (evt.getSource() == backGroundButtons[3]) {
			currentBg = 3;
			frame.frameBgChange(Color.magenta);
		} else if (evt.getSource() == backGroundButtons[4]) {
			currentBg = 4;
			frame.frameBgChange(Color.red);
		}
		
	    repaint(); //update screen to show changes
	  }

}
