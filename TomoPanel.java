/* Elaine Qian and Shiloh Zheng
 * TomoTasker
 * write more stuff later lol
 */

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class TomoPanel extends JLayeredPane implements Runnable, KeyListener {
	// dimensions of window
	public static final int PANEL_WIDTH = 1200;
	public static final int PANEL_HEIGHT = 650;
	
	public Thread panelThread;
	public Image image;
	public Graphics graphics;
	
	public JButton b;
	
	public TomoMenu menu;
	public Tasklist list;
	public Pomodoro timer;
	public DateTimeDisplay date;
	
	public TomoPanel(Container c, TomoFrame frame) {

		this.setFocusable(true); // make everything in this class appear on the screen
		this.addKeyListener(this); // start listening for keyboard input

		// set the size of this panel
		this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));

		// make this class run at the same time as other classes
		panelThread = new Thread(this);
		panelThread.start();
		
		menu = new TomoMenu(c, frame);
		list = new Tasklist(c, frame);
		timer = new Pomodoro(c);
		date = new DateTimeDisplay();
		
		this.add(menu, 9);
		this.add(list, 10);
		this.add(timer, 9);
		
	    addMouseListener(new MouseAdapter() {
	    		public void mousePressed(MouseEvent e) {
	    			list.mousePressed(e);
	    		}
			});
	    
	    addMouseMotionListener(new MouseAdapter() {
	    		public void mouseDragged(MouseEvent e) {
	    			list.mouseDragged(e);
	    		}
	    });
	}
	
	public void paint(Graphics g) {
		// draws images off screen to move on screen later
		image = createImage(PANEL_WIDTH, PANEL_HEIGHT); // draw off screen
		graphics = image.getGraphics();
		draw(graphics);// update the positions of everything on the screen
		g.drawImage(image, 0, 0, this); // move the image on the screen

	}
	
	public void draw(Graphics g) {
		// deal with this later ig (once we actucally make the other classes for the things we want to draw)
		menu.draw(g);
		list.draw(g);
		timer.draw(g);
		date.draw(g);
	}
	
	// also deal with this keys stuff later vvvvvvvvvvvvvvvv
	
	// checks if a key is pressed
	public void keyPressed(KeyEvent e) {
			
	}

	// checks if a key is released
	public void keyReleased(KeyEvent e) {

	}

	// left empty because we don't need it, but it is required to be overridded by
	// the KeyListener interface
	public void keyTyped(KeyEvent e) {

	}
	
	public void run() {
		// forces computer to slow down in calling methods
		long lastTime = System.nanoTime();
		double amountOfTicks = 60;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long now;

		while (true) { // the infinite game loop
			now = System.nanoTime();
			delta = delta + (now - lastTime) / ns;
			lastTime = now;

			// only move objects around and update screen if enough time has passed
			if (delta >= 1) {
				repaint();
				delta--;
			}
		}
	}
}
