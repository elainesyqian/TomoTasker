import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.event.*;

import javax.swing.Timer;

//import java.util.TimerTask;

public class Pomodoro extends JPanel implements ActionListener {

	public static final int POMO_WIDTH = 350;
	public static final int POMO_HEIGHT = 180;

	public static int pomoX = 100;
	public static int pomoY = 100;

	JButton startTimer;
	JButton pauseTimer;
	JButton resetTimer;
	JButton changeAlert;
	JButton workTime;
	JButton shortBreak;
	JButton longBreak;

	public Container c;
	public static int timerVis;

	// kinda dont need thsi but wtv
	public static Timer timer;

	public static int min, sec;

	public static long initial;
	public long remaining;

	public boolean play, wt, lb, sb, playedAlert = false;

	public int firstMouseX;
	public int firstMouseY;
	public int firstPomoX;
	public int firstPomoY;
	
	public boolean mouseDragging;
	
	public Pomodoro(Container c) {
		this.c = c;
		// t = new Timer();

		min = 40;
		sec = 0;
		wt = true;

		initial = System.currentTimeMillis();

		play = false;
		timerVis = -1;

		startTimer = new JButton("START");
		pauseTimer = new JButton("PAUSE");
		resetTimer = new JButton("RESET");
		changeAlert = new JButton("change alert");

		workTime = new JButton("work timer");
		shortBreak = new JButton("short break");
		longBreak = new JButton("long break");

		c.add(startTimer);
		c.add(pauseTimer);
		c.add(resetTimer);

		c.add(changeAlert);

		c.add(workTime);
		c.add(shortBreak);
		c.add(longBreak);

		startTimer.addActionListener(this);
		pauseTimer.addActionListener(this);
		resetTimer.addActionListener(this);

		changeAlert.addActionListener(this);

		workTime.addActionListener(this);
		shortBreak.addActionListener(this);
		longBreak.addActionListener(this);

	}

	public void draw(Graphics g) {

		if (timerVis == 1) {
			g.drawRect(pomoX, pomoY, POMO_WIDTH, POMO_HEIGHT);
			g.setColor(Color.PINK);
			g.fillRect(pomoX, pomoY, POMO_WIDTH, 30);
			g.setColor(Color.black);
			g.setFont(new Font("Times New Roman", Font.PLAIN, 15));
			g.drawString("TIMER", pomoX+20, pomoY+20);
			
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

			g.setColor(Color.pink);
			g.setFont(new Font("Times New Roman", Font.PLAIN, 60));

			if (sec < 10) {
				g.drawString(min + ":0" + sec, pomoX+90, pomoY+100);
			} else {
				g.drawString(min + ":" + sec, pomoX+90, pomoY+100);
			}

			if (sec == 0 && min == 0 && !playedAlert) {
				play = false;
				playSFX("alert1.wav");
				g.drawString("0:00", 190, 170);
				repaint();
				playedAlert = true;
			}

			if (play && System.currentTimeMillis() - initial >= 1000 && (min > 0 || sec > 0)) {
				initial = System.currentTimeMillis();
				sec--;
				if (sec < 0) {
					sec = 59;
					min--;
				}
			}
		} else {
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

	public void actionPerformed(ActionEvent evt) {

		if (evt.getSource() == startTimer) {
			play = true;
			playedAlert = false;
		} else if (evt.getSource() == pauseTimer) {
			play = false;
		} else if (evt.getSource() == resetTimer) {
			// idk why ths doesnt work, it doesnt seem to register when its clicked
			if (wt) {
				min = 40;
			} else if (sb) {
				min = 5;
			} else if (lb) {
				min = 15;
			}
			System.out.print("test");
			sec = 0;
			play = false;
		} else if (evt.getSource() == workTime) {
			min = 40;
			sec = 0;

			wt = true;
			sb = false;
			lb = false;

			play = false;
		} else if (evt.getSource() == shortBreak) {
			min = 0;
			sec = 10;

			wt = false;
			sb = true;
			lb = false;

			play = false;
		} else if (evt.getSource() == longBreak) {
			min = 15;
			sec = 0;

			wt = false;
			sb = false;
			lb = true;

			play = false;

		}

		repaint();
	}

	// plays sound effects
	public static void playSFX(String fileName) {

		// try-catch to prevent crashing
		try {

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
