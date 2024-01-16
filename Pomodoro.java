/* Elaine Qian and Shiloh ZHeng
 * January 11nd, 2024
 * TomoPanel
 * This class handles all aspects of the Pomodoro timer feature
*/

//import statements
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.sound.sampled.*;
import javax.swing.*;

public class Pomodoro extends JPanel implements ActionListener {

	// sizing of the timer
	public static final int POMO_WIDTH = 350;
	public static final int POMO_HEIGHT = 180;

	// x and y coordinates of the timer
	public static int pomoX = 100;
	public static int pomoY = 100;

	// buttons
	public JButton startTimer;
	public JButton pauseTimer;
	public JButton resetTimer;

	// other variable declarations
	public Container c;
	public static int timerVis;

	public int min, sec;

	public long initial;
	public long remaining;

	public boolean play, playAlert = false;

	public boolean mouseDragging;
	public int firstMouseX;
	public int firstMouseY;
	public int firstPomoX;
	public int firstPomoY;
	
	boolean changedAlert = false;
	
	String[] timerOptions = {"Work", "Short Break", "Long Break"};
	JComboBox changeTimer;
	String currentTimer;
	
	String[] alertOptions = {"Apex", "Bells", "Electronic", "Flute", "Marimba"};
	JComboBox changeAlert;
	String currentAlert;
	
	// constructor
	public Pomodoro(Container c) {
		// setup
		this.c = c;

		// starting timer values
		min = 40;
		sec = 0;

		timerVis = -1;
		currentAlert = "Apex.wav";

		// buttons setup
		// initializing
		startTimer = new JButton("START");
		pauseTimer = new JButton("PAUSE");
		resetTimer = new JButton("RESET");

		// adding to screen
		c.add(startTimer);
		c.add(pauseTimer);
		c.add(resetTimer);

		// adding actionListiner to know when clicked
		startTimer.addActionListener(this);
		pauseTimer.addActionListener(this);
		resetTimer.addActionListener(this);
		
		changeTimer = new JComboBox(timerOptions);
		c.add(changeTimer);
		changeTimer.addActionListener(this);

		changeAlert = new JComboBox(alertOptions);
		c.add(changeAlert);
		changeAlert.addActionListener(this);
	}

	// this method takes care of all things drawn to the screen and keeping track of
	// time
	public void draw(Graphics g) {

		// if the timer is open or 'visible'
		if (timerVis == 1) {
			// draws general rectangle outline
			g.drawRect(pomoX, pomoY, POMO_WIDTH, POMO_HEIGHT);

			// draws the title bar and background
			g.setColor(Color.PINK);
			g.fillRect(pomoX, pomoY, POMO_WIDTH, 30);
			g.setColor(Color.black);
			g.setFont(new Font("Times New Roman", Font.PLAIN, 17));
			g.drawString("TIMER", pomoX + 20, pomoY + 20);
			g.drawString("Current Timer:", pomoX + 230, pomoY + 60);
			g.drawString("Current Alert Sound:", pomoX + 70, pomoY + 170);

			// set buttons to be visible and their location
			startTimer.setVisible(true);
			startTimer.setBounds(pomoX + 5, pomoY + 40, 75, 30);

			pauseTimer.setVisible(true);
			pauseTimer.setBounds(pomoX + 5, pomoY + 70, 75, 30);

			resetTimer.setVisible(true);
			resetTimer.setBounds(pomoX + 5, pomoY + 110, 75, 30);

			changeTimer.setVisible(true);
			changeTimer.setBounds(pomoX + 225, pomoY + 65, 125, 30);

			changeAlert.setVisible(true);
			changeAlert.setBounds(pomoX + 220, pomoY + 150, 130, 30);
			
			// sets colour for timer text
			g.setColor(Color.pink);
			g.setFont(new Font("Times New Roman", Font.PLAIN, 60));

			// draws the time left

			if (sec < 10) {
				g.drawString(min + ":0" + sec, pomoX + 85, pomoY + 100);
			} else {
				g.drawString(min + ":" + sec, pomoX + 85, pomoY + 100);
			}

			// if timer has finished and has not played the alert yet
			if (playAlert) {
				
				sec = 0;
				// set play to false so no negative numbers
				g.drawString("0:00", 190, 170);
				repaint();

				// play alert
				playSound(currentAlert);
				playAlert = false;
				play = false;
			}

			// keeps track of time left
			// if the timer is on play mode and 1 second has passed
			if (play && System.currentTimeMillis() - initial >= 1000 && (min > 0 || sec >= 0)) {
				initial = System.currentTimeMillis();
				// decrease seconds variable by 1 for the 1 second passed
				sec--;

				// if seconds was 0 and decreases to -1
				if (sec < 0 && min != 0) {
					// set seconds to 59 and decrease minutes by 1
					sec = 59;
					min--;
				}

				if (sec == -1 && min == 0) {
					playAlert = true;
				}
			}
			// if timer is closed and not visible
		} else {

			// set all buttons', dropdowns' visibility and play to false
			startTimer.setVisible(false);
			pauseTimer.setVisible(false);
			resetTimer.setVisible(false);
			changeTimer.setVisible(false);
			changeAlert.setVisible(false);
			play = false;
		}

	}

	// this method takes care all logistics when a button is clicked
	public void actionPerformed(ActionEvent evt) {

		// if start button is clicked set play to true and played Alert to false
		if (evt.getSource() == startTimer) {
			play = true;
			playAlert = false;

			// set play to false if timer is paused
		} else if (evt.getSource() == pauseTimer) {
			play = false;

			// if reset button is clicked set play to false, sec to 0, and min to respective
			// values based on which timer was last used
		} else if (evt.getSource() == resetTimer) {
			
			currentTimer = changeTimer.getSelectedItem() + "";
			
			if (currentTimer.equals("Work")) {
				min = 40;
			} else if (currentTimer.equals("Short Break")) {
				min = 1;
			} else if (currentTimer.equals("Long Break")) {
				min = 10;
			}
			sec = 0;
			play = false;

		} else if (evt.getSource() == changeTimer) {
			
			currentTimer = changeTimer.getSelectedItem() + "";
			
			if (currentTimer.equals("Work")) {
				min = 40;
			} else if (currentTimer.equals("Short Break")) {
				min = 1;
			} else if (currentTimer.equals("Long Break")) {
				min = 10;
			}
			sec = 0;
			play = false;

		} else if (evt.getSource() == changeAlert) {
			currentAlert = changeAlert.getSelectedItem() + ".wav";
			repaint();
			playSound(currentAlert);
		}

		repaint(); // updates screen to show changes
	}

	// this method plays audio
	public static void playSound(String fileName) {

		// try-catch to prevent crashing
		try {

			// setup code to open the file
			File songClip = new File(fileName);
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(songClip);
			Clip clip = AudioSystem.getClip();
			clip.open(audioStream);

			// start playing
			clip.start();

//			Thread.sleep(4000);
//			clip.stop();

		} catch (Exception e) {
			// nothing will happen if crashes
		}

	}

	// called from TomoPanel when the mouse is pressed
	// saves the x and y values of the mouse press and timer
	public void mousePressed(MouseEvent e) {
		// gets the x and y of the mouse and list when the mouse is pressed
		firstMouseX = e.getX();
		firstMouseY = e.getY();
		firstPomoX = pomoX;
		firstPomoY = pomoY;
	}

	// called from TomoPanel when the mouse is dragged
	// and uses the x and y values at the original click
	// to see if the timer should be dragged
	public void mouseDragged(MouseEvent e) {

		// checks if the x and y values of the mouse fall within the draggable title tab
		// when the mouse
		// was first pressed down
		if (firstMouseX >= firstPomoX && firstMouseX <= firstPomoX + POMO_WIDTH && firstMouseY >= firstPomoY
				&& firstMouseY <= firstPomoY + 30) {
			// if the mouse was in the title tab, allow for the list to be dragged
			mouseDragging = true;
		} else {
			// otherwise, don't drag the list
			mouseDragging = false;
		}

		if (mouseDragging) {
			// if the timer is currently being dragged, make it follow the mouse
			pomoX = e.getX() - (firstMouseX - firstPomoX);
			pomoY = e.getY() - (firstMouseY - firstPomoY);

			// if the mouse drags the title tab offscreen, move it back onscreen
			if (pomoX < 0) {
				pomoX = 0;
			} else if (pomoX > TomoPanel.PANEL_WIDTH - POMO_WIDTH) {
				pomoX = TomoPanel.PANEL_WIDTH - POMO_WIDTH;
			}

			if (pomoY < 0) {
				pomoY = 0;
			} else if (pomoY + 30 > TomoPanel.PANEL_HEIGHT) {
				pomoY = TomoPanel.PANEL_HEIGHT - 30;
			}

			repaint(); // updates screen to show changes
		}
	}

}
