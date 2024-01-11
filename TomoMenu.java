/* Shiloh Zheng and Elaine Qian
 * January 11th, 2024
 * TomoMenu
 * This class creates the control menu of the Tomotasker, with buttons to 
 * open new windows/objects that have different functions
*/

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class TomoMenu extends JPanel implements ActionListener {
	// size of the rectangle border of the menu
	public static final int MENU_WIDTH = 300;
	public static final int MENU_HEIGHT = 550;

	JButton timerButton;
	JButton checkListButton;
	JButton newNotesButton;

	JButton[] backGroundButtons = { new JButton("1"), new JButton("2"), new JButton("3"), new JButton("4"),
			new JButton("5") };

	// array of possible quotes
	String[] dailyQuote = { "wow, awesome", "wow2", "epic, so epic", "w" };

	int todaysQuoteIndex;
	int buttonRightShift;

	int currentBg;

	public Container c;
	public TomoFrame frame;

	public TomoMenu(Container c, TomoFrame frame) {
		// constructor of the TomoMenu

		// gets the container and frame, in order to place things
		// and change the background later
		this.c = c;
		this.frame = frame;

		// sets the default background to the plaine white one
		currentBg = 0;

		// creates the buttons that open the timer, tasklist, and sticky notes
		// respectively
		timerButton = new JButton("TIMER");
		checkListButton = new JButton("CHECKLIST");
		newNotesButton = new JButton("ADD A NEW NOTE");

		// adds the timer button to the container and positions it on screen
		c.add(timerButton);
		timerButton.setBounds(875, 75, 90, 90);

		// adds the checklist button to the container and positions it on screen
		c.add(checkListButton);
		checkListButton.setBounds(995, 75, 90, 90);

		// adds the new sticky note button to the container and positions it on screen
		c.add(newNotesButton);
		newNotesButton.setBounds(875, 195, 255, 50);

		// positions the background changing buttons so that they are all beside each
		// other
		buttonRightShift = 0;

		for (JButton jB : backGroundButtons) {
			c.add(jB);
			jB.setBounds(870 + buttonRightShift, 320, 45, 45);
			// increases the x-position of the next background button to place
			buttonRightShift = buttonRightShift + 55;

			// adds an action listener to each background button
			jB.addActionListener(this);
		}

		// added an action listener to the rest of the buttons
		timerButton.addActionListener(this);
		checkListButton.addActionListener(this);
		newNotesButton.addActionListener(this);

		// changes the quote every time the program is opened
		todaysQuoteIndex = (int) ((Math.random() * (4)));
	}

	// draws parts of the menu
	public void draw(Graphics g) {

		// draws the rectangle border of the menu
		g.drawRect(850, 50, MENU_WIDTH, MENU_HEIGHT);

		// adds text that says what background "room" the user is currently in
		g.drawString("CURRENT ROOM:", 875, 275);

		// checks what the background currently is
		// to adds text that says what background "room" the user is currently in
		if (currentBg == 0) {
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

		// adds text that adds a quote on screen
		g.drawString("DAILY QUOTE:", 875, 400);
		g.drawString(dailyQuote[todaysQuoteIndex], 875, 415);

	}

	// called when a button on the menu is pressed
	// in order to change something about the screen
	public void actionPerformed(ActionEvent evt) {

		if (evt.getSource() == timerButton) {
			// if the timer button was pressed, toggle on/off the pomodoro timer
			Pomodoro.timerVis = Pomodoro.timerVis * -1;
		} else if (evt.getSource() == checkListButton) {
			// if the checklist button was pressed, toggle on/off the checklist
			Tasklist.taskVis = Tasklist.taskVis * -1;
		} else if (evt.getSource() == backGroundButtons[0]) {
			// if the 1st background button was pressed, change the background to white
			currentBg = 0;
			frame.frameBgChange(Color.white);
		} else if (evt.getSource() == backGroundButtons[1]) {
			// if the 2nd background button was pressed, change the background to blue
			currentBg = 1;
			frame.frameBgChange(Color.blue);
		} else if (evt.getSource() == backGroundButtons[2]) {
			// if the 3rd background button was pressed, change the background to green
			currentBg = 2;
			frame.frameBgChange(Color.green);
		} else if (evt.getSource() == backGroundButtons[3]) {
			// if the 4th background button was pressed, change the background to magenta
			currentBg = 3;
			frame.frameBgChange(Color.magenta);
		} else if (evt.getSource() == backGroundButtons[4]) {
			// if the 5th background button was pressed, change the background to red
			currentBg = 4;
			frame.frameBgChange(Color.red);
		}

		repaint(); // update screen to show changes
	}

}
