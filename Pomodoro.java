import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import javax.swing.Timer;

//import java.util.TimerTask;

public class Pomodoro extends JPanel implements ActionListener {

	public static final int POMO_WIDTH = 250;
	public static final int POMO_HEIGHT = 100;

	public static int POMO_X = 100;
	public static int POMO_Y = 100;

	String jltime;
	
	JButton startTimer;
	JButton pauseTimer;
	JButton changeAlert;
	JButton workTime;
	JButton shortBreak;
	JButton longBreak;

	public Container c;

	public static Timer timer;
	public static int time;
	public static long initial;
    public long remaining;

	public Pomodoro(Container c) {
		this.c = c;
		// t = new Timer();

		time = 25;
		initial = System.currentTimeMillis();

		startTimer = new JButton("START");
		pauseTimer = new JButton("PAUSE");
		changeAlert = new JButton("change");

		workTime = new JButton("work timer");
		shortBreak = new JButton("short break");
		longBreak = new JButton("long break");

		c.add(startTimer);
		startTimer.setBounds(105, 110, 75, 30);

		c.add(pauseTimer);
		pauseTimer.setBounds(105, 140, 75, 30);

		c.add(changeAlert);
		changeAlert.setBounds(170, 165, 70, 30);

		c.add(workTime);
		workTime.setBounds(250, 105, 100, 30);

		c.add(shortBreak);
		shortBreak.setBounds(250, 135, 100, 30);

		c.add(longBreak);
		longBreak.setBounds(250, 165, 100, 30);

		startTimer.addActionListener(this);
		pauseTimer.addActionListener(this);
		changeAlert.addActionListener(this);

		workTime.addActionListener(this);
		shortBreak.addActionListener(this);
		longBreak.addActionListener(this);

	}
	
    
	public void draw(Graphics g) {
		g.drawRect(POMO_X, POMO_Y, POMO_WIDTH, POMO_HEIGHT);
		g.drawString(time+"", 24, 25);
		
		if(System.currentTimeMillis() - initial >= 1000 && time > 0) {
			time--;
			initial = System.currentTimeMillis();
		}

	}

	public void actionPerformed(ActionEvent evt) {
		repaint();
	}

}
