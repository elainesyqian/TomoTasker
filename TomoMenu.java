/* Elaine Qian and Shiloh ZHeng
 * January 	17th, 2024
 * TomoMenu
 * This class creates the control menu of the Tomotasker, with buttons to 
 * open new windows/objects that have different functions as well as controls the Sticky Notes
*/

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.*;
import javax.sound.sampled.*;
import javax.swing.*;

public class TomoMenu extends JPanel implements ActionListener {
	// size of the rectangle
	public static final int MENU_WIDTH = 300;
	public static final int MENU_HEIGHT = 550;

	JButton timerButton;
	JButton checkListButton;
	JButton newNotesButton;

	// TODO
	JButton[] backGroundButtons = { new JButton("1"), new JButton("2"), new JButton("3"), new JButton("4"),
			new JButton("5") };

	public ArrayList<StickyNotes> notes = new ArrayList<StickyNotes>();
	public StickyNotes newBlankNote;

	public int buttonRightShift;

	public String currentBg;
	public static String todaysQuote;

	public Container c;
	public TomoFrame frame;

	public int rainVis, cafeVis, fireVis, birdVis, waveVis = -1;
	File rainClip, cafeClip, fireClip, birdClip, waveClip;
	AudioInputStream rainStream, cafeStream, fireStream, birdStream, waveStream;
	Clip rainPlay, cafePlay, firePlay, birdPlay, wavePlay;

	public ImageIcon rainIcon, cafeIcon, fireIcon, birdIcon, waveIcon;
	public Image rainImage, cafeImage, fireImage, birdImage, waveImage;

	public TomoMenu(Container c, TomoFrame frame) {
		// super(850, 50, MENU_WIDTH, MENU_HEIGHT);
		this.c = c;
		this.frame = frame;

		currentBg = "Rain";
		rainVis = 1;

		timerButton = new JButton("TIMER");
		checkListButton = new JButton("CHECKLIST");
		newNotesButton = new JButton("ADD A NEW NOTE");

		c.add(timerButton);
		timerButton.setBounds(875, 75, 90, 90);

		c.add(checkListButton);
		checkListButton.setBounds(995, 75, 90, 90);

		c.add(newNotesButton);
		newNotesButton.setBounds(875, 195, 255, 50);

		buttonRightShift = 0;

		for (JButton jB : backGroundButtons) {
			c.add(jB);
			jB.setBounds(870 + buttonRightShift, 320, 45, 45);
			buttonRightShift = buttonRightShift + 55;

			jB.addActionListener(this);
		}

		timerButton.addActionListener(this);
		checkListButton.addActionListener(this);
		newNotesButton.addActionListener(this);

		todaysQuote = getQuote();

		try {
			rainClip = new File("Rain.wav");
			rainStream = AudioSystem.getAudioInputStream(rainClip);
			rainPlay = AudioSystem.getClip();
			rainPlay.open(rainStream);

			cafeClip = new File("Cafe.wav");
			cafeStream = AudioSystem.getAudioInputStream(cafeClip);
			cafePlay = AudioSystem.getClip();
			cafePlay.open(cafeStream);

			fireClip = new File("Fire.wav");
			fireStream = AudioSystem.getAudioInputStream(fireClip);
			firePlay = AudioSystem.getClip();
			firePlay.open(fireStream);

			birdClip = new File("Birds.wav");
			birdStream = AudioSystem.getAudioInputStream(birdClip);
			birdPlay = AudioSystem.getClip();
			birdPlay.open(birdStream);

			waveClip = new File("Waves.wav");
			waveStream = AudioSystem.getAudioInputStream(waveClip);
			wavePlay = AudioSystem.getClip();
			wavePlay.open(waveStream);

		} catch (Exception e) {

		}

		rainIcon = new ImageIcon("RainBG.png");
		rainImage = rainIcon.getImage();

		cafeIcon = new ImageIcon("CafeBG.png");
		cafeImage = cafeIcon.getImage();

		fireIcon = new ImageIcon("FireBG.png");
		fireImage = fireIcon.getImage();

		birdIcon = new ImageIcon("BirdBG.png");
		birdImage = birdIcon.getImage();

		waveIcon = new ImageIcon("WaveBG.png");
		waveImage = waveIcon.getImage();

	}

	public void draw(Graphics g) {

		if (currentBg.equals("Rain")) {
			g.drawImage(rainImage, 0, 0, TomoPanel.PANEL_WIDTH, TomoPanel.PANEL_HEIGHT, null);

		} else if (currentBg.equals("Cafe")) {
			g.drawImage(cafeImage, 0, 0, TomoPanel.PANEL_WIDTH, TomoPanel.PANEL_HEIGHT, null);

		} else if (currentBg.equals("Fire")) {
			g.drawImage(fireImage, 0, 0, TomoPanel.PANEL_WIDTH, TomoPanel.PANEL_HEIGHT, null);

		} else if (currentBg.equals("Bird")) {
			g.drawImage(birdImage, 0, 0, TomoPanel.PANEL_WIDTH, TomoPanel.PANEL_HEIGHT, null);

		} else if (currentBg.equals("Wave")) {
			g.drawImage(waveImage, 0, 0, TomoPanel.PANEL_WIDTH, TomoPanel.PANEL_HEIGHT, null);
		}

		// draws the rectangle
		g.setColor(Color.PINK);
		g.fillRect(850, 50, MENU_WIDTH, MENU_HEIGHT);

		g.setColor(Color.BLACK);
		if (currentBg.equals("Rain")) {

			g.drawString("Rainy Day", 875, 300);

			if (rainVis == 1) {
				try {
					rainPlay.start(); // loop code
				} catch (Exception e) {
				}
			} else {
				try {
					rainPlay.stop();
				} catch (Exception e) {
				}
			}

			try {
				cafePlay.stop();
				firePlay.stop();
				birdPlay.stop();
				wavePlay.stop();
			} catch (Exception e) {

			}

		} else if (currentBg.equals("Cafe")) {
			g.drawString("Cafe", 875, 300);

			if (cafeVis == 1) {
				try {
					cafePlay.start(); // loop code
				} catch (Exception e) {
				}
			} else {
				try {
					cafePlay.stop();
				} catch (Exception e) {
				}
			}

			try {
				rainPlay.stop();
				firePlay.stop();
				birdPlay.stop();
				wavePlay.stop();
			} catch (Exception e) {

			}

		} else if (currentBg.equals("Fire")) {
			g.drawString("Fireplace", 875, 300);

			if (fireVis == 1) {
				try {
					firePlay.start(); // loop code
				} catch (Exception e) {
				}
			} else {
				try {
					firePlay.stop();
				} catch (Exception e) {
				}
			}

			try {
				rainPlay.stop();
				cafePlay.stop();
				birdPlay.stop();
				wavePlay.stop();
			} catch (Exception e) {

			}

		} else if (currentBg.equals("Bird")) {
			g.drawString("Birds Chirping", 875, 300);

			if (birdVis == 1) {
				try {
					birdPlay.start(); // loop code
				} catch (Exception e) {
				}
			} else {
				try {
					birdPlay.stop();
				} catch (Exception e) {
				}
			}

			try {
				rainPlay.stop();
				cafePlay.stop();
				firePlay.stop();
				wavePlay.stop();
			} catch (Exception e) {

			}

		} else if (currentBg.equals("Wave")) {
			g.drawString("Waves at the Beach", 875, 300);

			if (waveVis == 1) {
				try {
					wavePlay.start(); // loop code
				} catch (Exception e) {
				}
			} else {
				try {
					wavePlay.stop();
				} catch (Exception e) {
				}
			}

			try {
				rainPlay.stop();
				cafePlay.stop();
				firePlay.stop();
				birdPlay.stop();
			} catch (Exception e) {

			}

		}

		g.setColor(Color.BLACK);
		g.drawString("CURRENT ROOM:", 875, 275);

		g.setColor(Color.WHITE);
		g.drawString("DAILY QUOTE:", 5, 635);
		g.drawString(todaysQuote, 100, 635);

		// TODO add each note to screen
		for (int i = 0; i < notes.size(); i++) {
			if (!notes.get(i).delete) {
				notes.get(i).draw(g);
			} else {
				notes.remove(i);
			}
		}

	}

	public void actionPerformed(ActionEvent evt) {
		// TODO set up all buttons reactions to being clicked

		if (evt.getSource() == timerButton) {
			Pomodoro.timerVis = Pomodoro.timerVis * -1;
		} else if (evt.getSource() == checkListButton) {
			Tasklist.taskVis = Tasklist.taskVis * -1;
		} else if (evt.getSource() == newNotesButton) {
			// if the add a new note button was clicked, create a new sticky note
			newBlankNote = new StickyNotes(c, frame);
			notes.add(newBlankNote);
		} else if (evt.getSource() == backGroundButtons[0]) {
			rainVis = rainVis * -1;
			currentBg = "Rain";

			cafeVis = -1;
			fireVis = -1;
			birdVis = -1;
			waveVis = -1;
		} else if (evt.getSource() == backGroundButtons[1]) {
			cafeVis = cafeVis * -1;
			currentBg = "Cafe";

			rainVis = -1;
			fireVis = -1;
			birdVis = -1;
			waveVis = -1;
		} else if (evt.getSource() == backGroundButtons[2]) {
			fireVis = fireVis * -1;
			currentBg = "Fire";

			rainVis = -1;
			cafeVis = -1;
			birdVis = -1;
			waveVis = -1;
		} else if (evt.getSource() == backGroundButtons[3]) {
			birdVis = birdVis * -1;
			currentBg = "Bird";

			rainVis = -1;
			cafeVis = -1;
			fireVis = -1;
			waveVis = -1;
		} else if (evt.getSource() == backGroundButtons[4]) {
			waveVis = waveVis * -1;
			currentBg = "Wave";

			rainVis = -1;
			cafeVis = -1;
			birdVis = -1;
			fireVis = -1;
		}

		repaint(); // update screen to show changes
	}

	public void mousePressed(MouseEvent e) {
		// gets the x and y of the mouse and list when the mouse is pressed
		for (int i = 0; i < notes.size(); i++) {
			notes.get(i).mousePressed(e);
		}
	}

	// called from TomoPanel when the mouse is dragged
	// and uses the x and y values at the original click
	// to see if the list should be dragged
	public void mouseDragged(MouseEvent e) {
		for (int i = 0; i < notes.size(); i++) {
			notes.get(i).mouseDragged(e);
		}
	}

	public static String getQuote() {

		String inputLine;
		String quote = "";
		int counter = 0;

		try {
			URL url = new URL("https://zenquotes.io/");
			BufferedReader quoteReader = new BufferedReader(new InputStreamReader(url.openStream()));

			while ((inputLine = quoteReader.readLine()) != null) {

				if (counter == 173) {
					quote = inputLine.replaceFirst("        <h4 class=\"display-5 font-italic\"><blockquote>&ldquo;",
							"");
					quote = "\"" + quote.substring(0, quote.indexOf("<br"));
					quote = quote.replaceAll(".&rdquo; &mdash; <footer>", "\" — ");
				}
				counter++;
			}
			quoteReader.close();

		} catch (Exception e) {

		}
		return quote;
	}

}
