/* Elaine Qian and Shiloh Zheng
 * January 21st, 2024
 * Pomodoro
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
	public JButton controlTimer;
	public JButton resetTimer;
	public JButton closeButton;

	// other variable declarations
	public Container c;
	public static int timerVis;

	public int min, sec;

	public long initial;
	public long remaining;

	public boolean play, playAlert, changedAlert = false;

	// dropdown for timer options
	public String[] timerOptions = { "Work", "Short Break", "Long Break" };
	public JComboBox changeTimer;
	public String currentTimer;

	// dropdown for alert options
	public String[] alertOptions = { "Apex", "Bells", "Electronic", "Flute", "Marimba" };
	public JComboBox changeAlert;
	public String currentAlert;

	// variables for dragging
	public boolean mouseDragging;
	public int firstMouseX;
	public int firstMouseY;
	public int firstPomoX;
	public int firstPomoY;

	public ImageIcon x, start, pause, reset;

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

		// getting the icons for the pause/play button
		start = new ImageIcon(new ImageIcon("play.png").getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH));
		pause = new ImageIcon(new ImageIcon("pause.png").getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH));

		// creating the pause/play button
		controlTimer = new JButton("START");
		// adding the button to the screen
		c.add(controlTimer);
		controlTimer.addActionListener(this);
		controlTimer.setFocusable(false);
		controlTimer.setMargin(new Insets(20, 30, 20, 20));
		// adding the start icon onto it to begin with
		controlTimer.setIcon(start);

		// gets the icon for the restart timer button
		reset = new ImageIcon(new ImageIcon("reset.png").getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH));
		// creating the restart timer button
		resetTimer = new JButton("RESET");
		// adding the button to the screen
		c.add(resetTimer);
		resetTimer.addActionListener(this);
		resetTimer.setFocusable(false);
		resetTimer.setMargin(new Insets(20, 30, 20, 20));
		// adding the reset icon to it
		resetTimer.setIcon(reset);

		// getting the x icon
		x = new ImageIcon(new ImageIcon("x.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		// creating the close window button
		closeButton = new JButton("X");
		// adding the button to the screen
		c.add(closeButton);
		closeButton.addActionListener(this);
		closeButton.setFocusable(false);
		closeButton.setMargin(new Insets(20, 33, 21, 20));
		// adding the x icon to it
		closeButton.setIcon(x);

		// add the timer type dropdown so that the user can choose which timer they use
		changeTimer = new JComboBox(timerOptions);
		// sets the font for this dropdown menu
		changeTimer.setFont(new Font("Tahoma", Font.PLAIN, 12));
		// adds the dropdown to the screen
		c.add(changeTimer);
		// adds an ActionListener to see when it's being interacted with
		changeTimer.addActionListener(this);

		// add the alert dropdown so that the user can choose which alert they would
		// like to use
		changeAlert = new JComboBox(alertOptions);
		// sets the font for this dropdown menu
		changeAlert.setFont(new Font("Tahoma", Font.PLAIN, 12));
		// adds the dropdown to the screen
		c.add(changeAlert);
		// adds an ActionListener to see when it's being interacted with
		changeAlert.addActionListener(this);
	}

	// this method takes care of all things drawn to the screen and keeping track of
	// time
	public void draw(Graphics g) {

		// if the timer is open or 'visible'
		if (timerVis == 1) {
			// draws general rectangle outline
			g.setColor(Color.WHITE);
			g.fillRect(pomoX, pomoY, POMO_WIDTH, POMO_HEIGHT);

			// draws the title bar and background
			g.setColor(Color.PINK);
			g.fillRect(pomoX, pomoY, POMO_WIDTH, 30);

			// window text
			g.setColor(Color.black);

			// sets the font for the timer

			g.setFont(new Font("Tahoma", Font.PLAIN, 17));
			g.drawString("TIMER", pomoX + 20, pomoY + 20);

			// text on screen
			g.drawString("Current Timer:", pomoX + 230, pomoY + 60);
			g.drawString("Current Alert Sound:", pomoX + 67, pomoY + 170);

			// set buttons and dropdowns to be visible and their location
			controlTimer.setVisible(true);
			controlTimer.setBounds(pomoX + 5, pomoY + 35, 45, 45);

			resetTimer.setVisible(true);
			resetTimer.setBounds(pomoX + 5, pomoY + 85, 45, 45);

			closeButton.setVisible(true);
			closeButton.setBounds(pomoX + POMO_WIDTH - 27, pomoY + 5, 21, 21);

			changeTimer.setVisible(true);
			changeTimer.setBounds(pomoX + 225, pomoY + 65, 125, 30);

			changeAlert.setVisible(true);
			changeAlert.setBounds(pomoX + 220, pomoY + 150, 130, 30);

			// sets colour for timer display
			g.setColor(Color.pink);
			g.setFont(new Font("Times New Roman", Font.PLAIN, 60));

			// draws the time left
			if (sec < 10) {
				g.drawString(min + ":0" + sec, pomoX + 65, pomoY + 100);
			} else {
				g.drawString(min + ":" + sec, pomoX + 65, pomoY + 100);
			}

			// if timer has finished and has not played the alert yet
			if (playAlert) {

				// repaint to the screen so displays 0:00
				sec = 0;
				g.drawString("0:00", 190, 170);
				controlTimer.setIcon(start);
				repaint();

				// play alert
				playSound(currentAlert);

				// set playAlert to false the audio doesn't overlap
				playAlert = false;

				// set play to false so no negative numbers
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

				// set playAlert to true when seconds reaches -1 (workaround, bc otherwise, the
				// alert goes off when displaying 0:01)
				if (sec == -1 && min == 0) {
					playAlert = true;
				}
			}
			// if timer is closed and not visible
		} else {

			// set all buttons', dropdowns' visibility and play to false
			controlTimer.setVisible(false);
			resetTimer.setVisible(false);
			closeButton.setVisible(false);
			changeTimer.setVisible(false);
			changeAlert.setVisible(false);
			play = false;
			controlTimer.setIcon(start);
		}

	}

	// this method takes care all logistics when a button is clicked
	public void actionPerformed(ActionEvent evt) {

		// if start button is clicked set play to true and playedAlert to false
		if (evt.getSource() == controlTimer) {
			if (play) {
				play = false;
				controlTimer.setIcon(start);
			} else {

				if (min == 0 && sec == 0) {
					if (currentTimer.equals("Work")) {
						min = 40;
					} else if (currentTimer.equals("Short Break")) {
						min = 5;
					} else if (currentTimer.equals("Long Break")) {
						min = 15;
					}
				}

				play = true;
				playAlert = false;
				controlTimer.setIcon(pause);
			}

			// if reset button is clicked set play to false, sec to 0, and min to
			// respective values based on which timer was last used
		} else if (evt.getSource() == resetTimer) {
			currentTimer = changeTimer.getSelectedItem() + "";

			if (currentTimer.equals("Work")) {
				min = 40;
			} else if (currentTimer.equals("Short Break")) {
				min = 5;
			} else if (currentTimer.equals("Long Break")) {
				min = 15;
			}
			sec = 0;
			play = false;
			controlTimer.setIcon(start);

		} else if (evt.getSource() == closeButton) {
			timerVis = -1;

			// if timer dropdown is clicked
		} else if (evt.getSource() == changeTimer) {

			// change the current timer to what is selected
			currentTimer = changeTimer.getSelectedItem() + "";

			// set min to respective values based on timer type
			if (currentTimer.equals("Work")) {
				min = 40;
			} else if (currentTimer.equals("Short Break")) {
				min = 5;
			} else if (currentTimer.equals("Long Break")) {
				min = 15;
			}

			// set sec to 0 and play to false
			sec = 0;
			play = false;
			controlTimer.setIcon(start);

			// if alert dropdown is clicked
		} else if (evt.getSource() == changeAlert) {

			// change the current alert to what is selected
			currentAlert = changeAlert.getSelectedItem() + ".wav";

			// play the alert that has been chosen so that the user knows what it sounds
			// like
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

		} catch (Exception e) {
			// nothing will happen if crashes
		}

	}

	// called from TomoPanel when the mouse is pressed
	// saves the x and y values of the mouse press and timer
	public void mousePressed(MouseEvent e) {
		// gets the x and y of the mouse and timer when the mouse is pressed
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
			// if the mouse was in the title tab, allow for the timer to be dragged
			mouseDragging = true;
		} else {
			// otherwise, don't drag the timer
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
