/* Elaine Qian and Shiloh Zheng
 * January 17th, 2024
 * TomoPanel
 * This class creates the main program loop, constantly running the program and calling all its methods.
 * The main sections of the program include the Pomodoro timer, the tasklist, the control menu, and the streak counter
*/

//import statements
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TomoPanel extends JLayeredPane implements Runnable {
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
	public StreakCounter streak;

	public TomoPanel(Container c, TomoFrame frame) {
		// constructor for this class
		
		this.setFocusable(true); // make everything in this class appear on the screen

		// set the size of this panel
		this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));

		// make this class run at the same time as other classes
		panelThread = new Thread(this);
		panelThread.start();

		// creates the other objects in the TomoTasker
		menu = new TomoMenu(c, frame);
		list = new Tasklist(c, frame);
		timer = new Pomodoro(c);
		date = new DateTimeDisplay();
		streak = new StreakCounter();

		// adds the other objects to this panel
		this.add(menu, 9);
		this.add(list, 10);
		this.add(timer, 9);

		// starts listening for mouse input
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				// if the mouse is pressed, run functions in the list and timer
				list.mousePressed(e);
				timer.mousePressed(e);
				menu.mousePressed(e);
			}
		});

		// starts listening for mouse motion events
		addMouseMotionListener(new MouseAdapter() {
			// if the mouse is dragged, run functions in the list and timer
			public void mouseDragged(MouseEvent e) {
				list.mouseDragged(e);
				timer.mouseDragged(e);
				menu.mouseDragged(e);
			}
		});
	}

	// draws images off screen to move on screen later
	public void paint(Graphics g) {
		image = createImage(PANEL_WIDTH, PANEL_HEIGHT); // draw off screen
		graphics = image.getGraphics();
		draw(graphics);// update the positions of everything on the screen
		g.drawImage(image, 0, 0, this); // move the image on the screen
	}

	// calls draw method in the other classes
	public void draw(Graphics g) {
		menu.draw(g);
		streak.draw(g, menu.menuX, menu.menuY);
		list.draw(g);
		timer.draw(g);
		date.draw(g);
	}

	// makes the program continue endlessly, constantly updating the screen
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

			// only update screen if enough time has passed
			if (delta >= 1) {
				repaint();
				delta--;
			}
		}
	}
}
