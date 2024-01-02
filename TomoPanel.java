/* Elaine Qian and Shiloh Zheng
 * TomoTasker
 * write more stuff later lol
 */

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

public class TomoPanel extends JPanel implements Runnable, KeyListener {
	// dimensions of window
	public static final int GAME_WIDTH = 1200;
	public static final int GAME_HEIGHT = 650;
	
	public Thread panelThread;
	public Image image;
	public Graphics graphics;
	
	public TomoPanel() {

		this.setFocusable(true); // make everything in this class appear on the screen
		this.addKeyListener(this); // start listening for keyboard input

		// set the size of this panel
		this.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));

		// make this class run at the same time as other classes
		panelThread = new Thread(this);
		panelThread.start();
	}
	
	public void paint(Graphics g) {
		// draws images off screen to move on screen later
		image = createImage(GAME_WIDTH, GAME_HEIGHT); // draw off screen
		graphics = image.getGraphics();
		draw(graphics);// update the positions of everything on the screen
		g.drawImage(image, 0, 0, this); // move the image on the screen

	}
	
	public void draw(Graphics g) {
		// deal with this later ig (once we actucally make the other classes for the things we want to draw)
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
