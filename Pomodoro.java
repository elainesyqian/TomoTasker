import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import javax.swing.Timer;

//import java.util.TimerTask;

public class Pomodoro extends JPanel implements ActionListener {

	public static final int POMO_WIDTH = 350;
	public static final int POMO_HEIGHT = 150;

	public static int POMO_X = 100;
	public static int POMO_Y = 100;

	JButton startTimer;
	JButton pauseTimer;
	JButton resetTimer;
	JButton changeAlert;
	JButton workTime;
	JButton shortBreak;
	JButton longBreak;

	public Container c;

	// kinda dont need thsi but wtv
	public static Timer timer;

	public static int min, sec;

	public static long initial;
	public long remaining;

	public boolean play;

	public Pomodoro(Container c) {
		this.c = c;
		// t = new Timer();

		min = 40;
		sec = 0;

		initial = System.currentTimeMillis();

		play = false;

		startTimer = new JButton("START");
		pauseTimer = new JButton("PAUSE");
		resetTimer = new JButton("RESET");
		changeAlert = new JButton("change alert");

		workTime = new JButton("work timer");
		shortBreak = new JButton("short break");
		longBreak = new JButton("long break");

		c.add(startTimer);
		startTimer.setBounds(105, 110, 75, 30);

		c.add(pauseTimer);
		pauseTimer.setBounds(105, 140, 75, 30);

		c.add(resetTimer);
		resetTimer.setBounds(105, 170, 75, 30);

		c.add(changeAlert);
		changeAlert.setBounds(340, 220, 110, 30);

		c.add(workTime);
		workTime.setBounds(340, 105, 100, 30);

		c.add(shortBreak);
		shortBreak.setBounds(340, 135, 100, 30);

		c.add(longBreak);
		longBreak.setBounds(340, 165, 100, 30);

		startTimer.addActionListener(this);
		pauseTimer.addActionListener(this);
		changeAlert.addActionListener(this);

		workTime.addActionListener(this);
		shortBreak.addActionListener(this);
		longBreak.addActionListener(this);

	}

	public void draw(Graphics g) {
		g.drawRect(POMO_X, POMO_Y, POMO_WIDTH, POMO_HEIGHT);

		g.setColor(Color.pink);
		g.setFont(new Font("Times New Roman", Font.PLAIN, 40));
		if (sec < 10) {
			g.drawString(min + ":0" + sec, 200, 150);
		} else {
			g.drawString(min + ":" + sec, 200, 150);
		}
		if (play && System.currentTimeMillis() - initial >= 1000 && (min > 0 || sec > 0)) {
			initial = System.currentTimeMillis();
			sec--;
			if (sec < 0) {
				sec = 59;
				min--;
			}
		}

	}

	public void actionPerformed(ActionEvent evt) {

		if (evt.getSource() == startTimer) {
			play = true;
		} else if (evt.getSource() == pauseTimer) {
			play = false;
		} else if (evt.getSource() == resetTimer) {
			// figure this out later
		} else if (evt.getSource() == workTime) {
			min = 40;
			sec = 0;
			play = false;
		} else if (evt.getSource() == shortBreak) {
			min = 5;
			sec = 0;
			play = false;
		} else if (evt.getSource() == longBreak) {
			min = 15;
			sec = 0;
			play = false;
		}

		repaint();
	}

}
