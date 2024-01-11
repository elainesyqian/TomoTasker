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

	//sizing of the timer
	public static final int POMO_WIDTH = 350;
	public static final int POMO_HEIGHT = 180;

	//x and y coordinates of the timer
	public static int pomoX = 100;
	public static int pomoY = 100;

	//buttons
	JButton startTimer;
	JButton pauseTimer;
	JButton resetTimer;
	JButton changeAlert;
	JButton workTime;
	JButton shortBreak;
	JButton longBreak;

	//other variable declarations
	public Container c;
	public static int timerVis;

	public int min, sec;

	public long initial;
	public long remaining;

	public boolean play, wt, lb, sb, playedAlert = false;

	public int firstMouseX;
	public int firstMouseY;
	public int firstPomoX;
	public int firstPomoY;
	
	public boolean mouseDragging;
	
	//constructor
	public Pomodoro(Container c) {
		//setup
		this.c = c;

		//starting timer values
		min = 40;
		sec = 0;
		wt = true;

		timerVis = -1;

		//buttons setup
		//initializing
		startTimer = new JButton("START");
		pauseTimer = new JButton("PAUSE");
		resetTimer = new JButton("RESET");
		changeAlert = new JButton("change alert");
		workTime = new JButton("work timer");
		shortBreak = new JButton("short break");
		longBreak = new JButton("long break");

		//adding to screen
		c.add(startTimer);
		c.add(pauseTimer);
		c.add(resetTimer);
		c.add(changeAlert);
		c.add(workTime);
		c.add(shortBreak);
		c.add(longBreak);

		//adding actionListiner to know when clicked
		startTimer.addActionListener(this);
		pauseTimer.addActionListener(this);
		resetTimer.addActionListener(this);
		changeAlert.addActionListener(this);
		workTime.addActionListener(this);
		shortBreak.addActionListener(this);
		longBreak.addActionListener(this);

	}

	//this method takes care of all things drawn to the screen and keeping track of time
	public void draw(Graphics g) {

		//if the timer is open or 'visible'
		if (timerVis == 1) {
			//draws general rectangle outline
			g.drawRect(pomoX, pomoY, POMO_WIDTH, POMO_HEIGHT);
			
			//draws the title bar and background
			g.setColor(Color.PINK);
			g.fillRect(pomoX, pomoY, POMO_WIDTH, 30);
			g.setColor(Color.black);
			g.setFont(new Font("Times New Roman", Font.PLAIN, 15));
			g.drawString("TIMER", pomoX+20, pomoY+20);
			
			//set buttons to be visible and their location
			startTimer.setVisible(true);
			startTimer.setBounds(pomoX + 5, pomoY + 40, 75, 30);
			
			pauseTimer.setVisible(true);
			pauseTimer.setBounds(pomoX + 5, pomoY + 70, 75, 30);
			
			resetTimer.setVisible(true);
			resetTimer.setBounds(pomoX + 5, pomoY + 110, 75, 30);
			
			changeAlert.setVisible(true);
			changeAlert.setBounds(pomoX + 240, pomoY + 150, 110, 30);
			
			workTime.setVisible(true);
			workTime.setBounds(pomoX + 240, pomoY + 35, 100, 30);
			
			shortBreak.setVisible(true);
			shortBreak.setBounds(pomoX + 240, pomoY + 65, 100, 30);
			
			longBreak.setVisible(true);
			longBreak.setBounds(pomoX + 240, pomoY + 95, 100, 30);

			//sets colour for timer text
			g.setColor(Color.pink);
			g.setFont(new Font("Times New Roman", Font.PLAIN, 60));

			//draws the time left
			if (sec < 10) {
				g.drawString(min + ":0" + sec, pomoX+90, pomoY+100);	
			} else {
				g.drawString(min + ":" + sec, pomoX+90, pomoY+100);
			}

			//if timer has finished and has not played the alert yet
			if (sec == 0 && min == 0 && !playedAlert) {
				//set play to false so no negative numbers
				play = false;
				g.drawString("0:00", 190, 170);
				repaint();
				
				//play alert
				playSFX("alert1.wav");
				playedAlert = true;
			}

			//keeps track of time left
			//if the timer is on play mode and 1 second has passed
			if (play && System.currentTimeMillis() - initial >= 1000 && (min > 0 || sec > 0)) {
				initial = System.currentTimeMillis();
				//decrease seconds variable by 1 for the 1 second passed
				sec--;
				
				//if seconds was 0 and decreases to -1
				if (sec < 0) {
					//set seconds to 59 and decrease minutes by 1
					sec = 59;
					min--;
				}
			}
		//if timer is closed and not visible
		} else {
			
			//set all buttons' visibility and play to false
			startTimer.setVisible(false);
			pauseTimer.setVisible(false);
			resetTimer.setVisible(false);
			changeAlert.setVisible(false);
			workTime.setVisible(false);
			shortBreak.setVisible(false);
			longBreak.setVisible(false);
			play = false;
		}

	}
	
	//this method takes care all logistics when a button is clicked
	public void actionPerformed(ActionEvent evt) {

		//if start button is clicked set play to true and played Alert to false
		if (evt.getSource() == startTimer) {
			play = true;
			playedAlert = false;
			
		//set play to false if timer is paused
		} else if (evt.getSource() == pauseTimer) {
			play = false;
		
		//if reset button is clicked set play to false, sec to 0, and min to respective values based on which timer was last used
		} else if (evt.getSource() == resetTimer) {
			if (wt) {
				min = 40;
			} else if (sb) {
				min = 5;
			} else if (lb) {
				min = 15;
			}
			sec = 0;
			play = false;
		
		//if work time is clicked, then set time to respective values and wt to true and other booleans to false
		} else if (evt.getSource() == workTime) {
			play = false;
			min = 40;
			sec = 0;

			wt = true;
			sb = false;
			lb = false;

		//if short break is clicked, then set time to respective values and sb to true and other booleans to false
		} else if (evt.getSource() == shortBreak) {
			play = false;
			min = 5;
			sec = 0;

			wt = false;
			sb = true;
			lb = false;
			
			//if long break is clicked, then set time to respective values and lb to true and other booleans to false
		} else if (evt.getSource() == longBreak) {
			play = false;
			min = 15;
			sec = 0;

			wt = false;
			sb = false;
			lb = true;
		}

		repaint();
	}

	// this method plays audio
	public static void playSFX(String fileName) {

		// try-catch to prevent crashing
		try {

			//setup code to open the file
			File songClip = new File(fileName);
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(songClip);
			Clip clip = AudioSystem.getClip();
			clip.open(audioStream);

			// start playing
			clip.start();

			Thread.sleep(4000);
			clip.stop();

		} catch (Exception e) {
			// nothing will happen if crashes
		}

	}

	public void mousePressed(MouseEvent e) {
		firstMouseX = e.getX();
		firstMouseY = e.getY();
		firstPomoX = pomoX;
		firstPomoY = pomoY;
	}

	public void mouseDragged(MouseEvent e) {
		if (firstMouseX >= firstPomoX && firstMouseX <= firstPomoX + POMO_WIDTH && firstMouseY >= firstPomoY
				&& firstMouseY <= firstPomoY + 30) {
			mouseDragging = true;
		} else {
			mouseDragging = false;
		}

		if (mouseDragging) {
			pomoX = e.getX() - (firstMouseX - firstPomoX);
			pomoY = e.getY() - (firstMouseY - firstPomoY);
			
			 if (pomoX < 0) { 
				 pomoX = 0;
			  } else if (pomoX > TomoPanel.PANEL_WIDTH  - POMO_WIDTH) {
				  pomoX = TomoPanel.PANEL_WIDTH - POMO_WIDTH;
			  }
			  
			  if (pomoY < 0) {
				  pomoY = 0;
			  } else if (pomoY + 30 > TomoPanel.PANEL_HEIGHT) {
				  pomoY = TomoPanel.PANEL_HEIGHT - 30;
			  }
			
			repaint();
		}
	}

}
