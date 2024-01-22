/* Elaine Qian and Shiloh Zheng
 * January 21th, 2024
 * TomoMenu
 * This class creates the control menu of the Tomotasker, with buttons to 
 * open new windows/objects that have different functions as well as controls the Sticky Notes
*/

//import statements
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.*;
import javax.sound.sampled.*;
import javax.swing.*;

// TomoMenu constructor
public class TomoMenu extends JPanel implements ActionListener {
	// size of the rectangle
	public static final int MENU_WIDTH = 300;
	public static final int MENU_HEIGHT = 256;

	// saves the coordinates of the menu
	public int menuX = 850;
	public int menuY = 50;

	public int firstMouseX;
	public int firstMouseY;
	public int firstMenuX;
	public int firstMenuY;

	// buttons on the menu
	JButton timerButton;
	JButton checkListButton;
	JButton newNotesButton;

	// array of buttons for rooms
	JButton[] backGroundButtons = { new JButton("rain"), new JButton("cafe"), new JButton("fire"), new JButton("bird"),
			new JButton("wave") };

	// how much to shift each room button by
	public int buttonRightShift;

	// creates an ArrayList to contain sticky notes used for making new sticky notes
	// to add later
	public ArrayList<StickyNotes> notes = new ArrayList<StickyNotes>();
	public StickyNotes newBlankNote;

	// other variable declarations
	public Container c;
	public TomoFrame frame;
	public String currentBg;
	public static String todaysQuote;
	public int counter = 0;

	// all variables needed to play ambient sound
	File rainClip, cafeClip, fireClip, birdClip, waveClip;
	AudioInputStream rainStream, cafeStream, fireStream, birdStream, waveStream;
	Clip rainPlay, cafePlay, firePlay, birdPlay, wavePlay;

	// all variables needed to change background image
	public ImageIcon rainIcon, cafeIcon, fireIcon, birdIcon, waveIcon, timerSymbol, listSymbol, stickySymbol;
	public Image rainImage, cafeImage, fireImage, birdImage, waveImage;

	// array of room symbol for buttons
	public ImageIcon[] backGroundSymbols = {
			new ImageIcon(new ImageIcon("rainSymbol.png").getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH)),
			new ImageIcon(new ImageIcon("cafeSymbol.png").getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH)),
			new ImageIcon(new ImageIcon("fireSymbol.png").getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH)),
			new ImageIcon(new ImageIcon("birdsSymbol.png").getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH)),
			new ImageIcon(new ImageIcon("wavesSymbol.png").getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH)) };

	// variables for reading file
	public static File file;
	public static BufferedWriter bw;
	public static BufferedReader br;

	// variable declarations for daily quote
	public static String inputLine;
	public static String quote = "";
	public static int count;

	// constructor of TomoMenu
	public TomoMenu(Container c, TomoFrame frame) {

		this.c = c;
		this.frame = frame;

		// setup variables
		currentBg = "Rain";

		// getting the file with the saved sticky notes
		file = new File("notes.txt").getAbsoluteFile();
		// reading all the saved notes
		readSaved();

		// creates all the images for the timer, tasklist, and stickyNote icons
		timerSymbol = new ImageIcon(
				new ImageIcon("timerSymbol.png").getImage().getScaledInstance(73, 73, Image.SCALE_SMOOTH));
		listSymbol = new ImageIcon(
				new ImageIcon("listSymbol.png").getImage().getScaledInstance(73, 73, Image.SCALE_SMOOTH));
		stickySymbol = new ImageIcon(
				new ImageIcon("stickySymbol.png").getImage().getScaledInstance(73, 73, Image.SCALE_SMOOTH));

		// creates, and adds the timer button on the screen and adds an action
		// listener
		timerButton = new JButton("TIMER");
		c.add(timerButton);
		timerButton.addActionListener(this);
		timerButton.setFocusable(false);
		timerButton.setMargin(new Insets(20, 32, 20, 20));
		// adds the timer icon to the button
		timerButton.setIcon(timerSymbol);

		// creates, and adds the checklist button on the screen and adds an
		// action listener
		checkListButton = new JButton("CHECKLIST");
		checkListButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		c.add(checkListButton);
		checkListButton.addActionListener(this);
		checkListButton.setFocusable(false);
		checkListButton.setMargin(new Insets(20, 32, 20, 20));
		// adds the checklist icon to the button
		checkListButton.setIcon(listSymbol);

		// creates, and adds the newNotes button on the screen and adds an action
		// listener
		newNotesButton = new JButton("ADD A NEW NOTE");
		newNotesButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		c.add(newNotesButton);
		newNotesButton.addActionListener(this);
		newNotesButton.setFocusable(false);
		newNotesButton.setMargin(new Insets(20, 32, 20, 20));
		// adds the sticky note icon to the button
		newNotesButton.setIcon(stickySymbol);

		// adds buttons for different rooms
		for (JButton jB : backGroundButtons) {
			c.add(jB);
			// adds an action listener to each background button
			jB.addActionListener(this);
			jB.setFocusable(false);
			jB.setMargin(new Insets(20, 30, 20, 18));
			// adds the corresponding background icon to the button
			jB.setIcon(backGroundSymbols[counter]);
			counter++;
		}

		// gets todays quote
		todaysQuote = getQuote();

		// sets up all the audio files
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
			// nothing will happen if crashes
		}

		// sets up all the background images
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

	// draws parts of the menu, the background, and controls ambient sound
	public void draw(Graphics g) {

		// positions timer, checklist, and new note button on the main menu
		timerButton.setBounds(menuX + 25, menuY + 55, 73, 73);
		checkListButton.setBounds(menuX + 115, menuY + 55, 73, 73);
		newNotesButton.setBounds(menuX + 207, menuY + 55, 73, 73);

		// positions the background changing buttons so that they are all beside each
		// other
		buttonRightShift = 0;

		for (JButton jB : backGroundButtons) {
			// positions the each backGroundButton onto the main menu
			jB.setBounds(menuX + 20 + buttonRightShift, menuY + 178, 45, 45);
			// increases the x-position of the next background button to place
			buttonRightShift = buttonRightShift + 55;
		}

		// draws the respective backgrounds based off of which room the user is in
		// must draw the background images first so that the other stuff appears on top
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

		// draws the draggable tab rectangle
		g.setColor(Color.PINK);
		g.fillRect(menuX, menuY, MENU_WIDTH, 30);

		// sets the font for the title
		g.setFont(new Font("Tahoma", Font.PLAIN, 17));

		// adds the title
		g.setColor(Color.BLACK);
		g.drawString("MENU", menuX + 10, menuY + 21);

		// draws the main rectangle
		g.setColor(Color.WHITE);
		g.fillRect(menuX, menuY + 30, MENU_WIDTH, MENU_HEIGHT);

		// sets the font for the rest of the text
		g.setFont(new Font("Tahoma", Font.PLAIN, 15));

		// adds the daily quote
		g.setColor(Color.WHITE);
		g.drawString("DAILY QUOTE:", 5, 635);
		g.drawString(todaysQuote, 111, 635);

		// adds the text that says the current room
		g.setColor(Color.BLACK);
		g.drawString("CURRENT ROOM:", menuX + 25, menuY + 167);

		// draws the respective room text to screen
		// plays corresponding audio while stopping all other audio
		try {

			if (currentBg.equals("Rain")) {
				g.drawString("Rainy Day", menuX + 147, menuY + 167);

				rainPlay.start();
				rainPlay.loop(Clip.LOOP_CONTINUOUSLY);

				// stops all other audio
				cafePlay.stop();
				firePlay.stop();
				birdPlay.stop();
				wavePlay.stop();

			} else if (currentBg.equals("Cafe")) {
				g.drawString("Cafe", menuX + 147, menuY + 167);

				cafePlay.start();
				cafePlay.loop(Clip.LOOP_CONTINUOUSLY);

				// stops all other audio
				rainPlay.stop();
				firePlay.stop();
				birdPlay.stop();
				wavePlay.stop();

			} else if (currentBg.equals("Fire")) {
				g.drawString("Fireplace", menuX + 147, menuY + 167);

				firePlay.start();
				firePlay.loop(Clip.LOOP_CONTINUOUSLY);

				// stops all other audio
				rainPlay.stop();
				cafePlay.stop();
				birdPlay.stop();
				wavePlay.stop();

			} else if (currentBg.equals("Bird")) {
				g.drawString("Birds Chirping", menuX + 147, menuY + 167);

				birdPlay.start();
				birdPlay.loop(Clip.LOOP_CONTINUOUSLY);

				// stops all other audio
				rainPlay.stop();
				cafePlay.stop();
				firePlay.stop();
				wavePlay.stop();

			} else if (currentBg.equals("Wave")) {
				g.drawString("Waves at the Beach", menuX + 147, menuY + 167);

				wavePlay.start();
				wavePlay.loop(Clip.LOOP_CONTINUOUSLY);

				// stops all other audio
				rainPlay.stop();
				cafePlay.stop();
				firePlay.stop();
				birdPlay.stop();

			}

		} catch (Exception e) {
			// nothing will happen if crashes
		}

		// draw each note in the array on screen if it isn't marked for deletion
		// otherwise remove it from the array of notes
		for (int i = 0; i < notes.size(); i++) {
			if (!notes.get(i).delete) {
				notes.get(i).draw(g);
			} else {
				notes.remove(i);
			}
		}

		// save the sticky notes
		writeToSave();

	}

	// this method takes care all logistics when a button is clicked
	public void actionPerformed(ActionEvent evt) {

		// if the timer button is clicked, toggle timer visibility
		if (evt.getSource() == timerButton) {
			Pomodoro.timerVis = Pomodoro.timerVis * -1;

			// if the checklist button is clicked, toggle checklist visibility
		} else if (evt.getSource() == checkListButton) {
			Tasklist.taskVis = Tasklist.taskVis * -1;

			// if the add a new note button was clicked, create a new sticky note
		} else if (evt.getSource() == newNotesButton) {
			newBlankNote = new StickyNotes(c, frame);
			notes.add(newBlankNote);

			// if a background button was clicked, set the currentBg to
			// the corresponding background
		} else if (evt.getSource() == backGroundButtons[0]) {
			currentBg = "Rain";

		} else if (evt.getSource() == backGroundButtons[1]) {
			currentBg = "Cafe";

		} else if (evt.getSource() == backGroundButtons[2]) {
			currentBg = "Fire";

		} else if (evt.getSource() == backGroundButtons[3]) {
			currentBg = "Bird";

		} else if (evt.getSource() == backGroundButtons[4]) {
			currentBg = "Wave";

		}

		repaint(); // update screen to show changes
	}

	// called from TomoPanel when the mouse is pressed
	// and calls mousePressed for all the sticky notes
	public void mousePressed(MouseEvent e) {
		// gets the x and y of the mouse and menu when the mouse is pressed
		for (int i = 0; i < notes.size(); i++) {
			notes.get(i).mousePressed(e);
		}

		firstMouseX = e.getX();
		firstMouseY = e.getY();
		firstMenuX = menuX;
		firstMenuY = menuY;
	}

	// called from TomoPanel when the mouse is dragged
	// and calls mouseDragged for all the sticky notes
	// and drags the menu if applicable
	public void mouseDragged(MouseEvent e) {
		for (int i = 0; i < notes.size(); i++) {
			notes.get(i).mouseDragged(e);
		}

		if (firstMouseX >= firstMenuX && firstMouseX <= firstMenuX + MENU_WIDTH && firstMouseY >= firstMenuY
				&& firstMouseY <= firstMenuY + 30) {
			// if the mouse was in the title tab, allow for the menu to be dragged
			menuX = e.getX() - (firstMouseX - firstMenuX);
			menuY = e.getY() - (firstMouseY - firstMenuY);

			// if the mouse drags the title tab offscreen, move it back onscreen
			if (menuX < 0) {
				menuX = 0;
			} else if (menuX > TomoPanel.PANEL_WIDTH - MENU_WIDTH) {
				menuX = TomoPanel.PANEL_WIDTH - MENU_WIDTH;
			}

			if (menuY < 0) {
				menuY = 0;
			} else if (menuY + 30 > TomoPanel.PANEL_HEIGHT) {
				menuY = TomoPanel.PANEL_HEIGHT - 30;
			}

			repaint();
		}
	}

	// this method uses webscraping get the daily quote from a website
	public static String getQuote() {

		// reset count to 0
		count = 0;

		try {
			// import link into bufferedReader to read HTML code
			URL url = new URL("https://zenquotes.io/");
			BufferedReader quoteReader = new BufferedReader(new InputStreamReader(url.openStream()));

			// while next line is available
			while ((inputLine = quoteReader.readLine()) != null) {

				// increase counter by 1
				count++;

				// if counter is at line 174
				// aka where the quote is stored in the code
				if (count == 174) {
					// edit the line of code so that we get a formatted quote
					quote = inputLine.replaceFirst("        <h4 class=\"display-5 font-italic\"><blockquote>&ldquo;",
							"");
					quote = "\"" + quote.substring(0, quote.indexOf("<br"));
					quote = quote.replaceAll(".&rdquo; &mdash; <footer>", "\" — ");
					
					//exits the while loop
					break;
				}

			}
			// closes the quote reader
			quoteReader.close();

		} catch (Exception e) {
			// nothing will happen if crashes
		}
		// returns the quote
		return quote;
	}

	// reads the saved information about the sticky notes
	// and adds the saved notes to the array of sticky notes
	public void readSaved() {
		String str;
		String allText = "";
		StickyNotes newNote;

		try {
			// setup buffered reader to read from the txt file
			br = new BufferedReader(new FileReader(file));

			// while the next line has content
			while ((str = br.readLine()) != null) {
				// if the line isn't a dividing character
				// which splits the content of the sticky notes from it's coordinates
				if (!str.equals("÷")) {
					if (!allText.equals("")) {
						// if the current text for the note isn't empty
						// move to the next line and add the next part of the note
						allText = allText + "\n" + str;
					} else {
						// otherwise, stay on the current line
						// and add the next part of the note
						allText = allText + str;
					}
				} else {
					// if the dividing character is reached
					// the next two lines are the coordinates of the sticky note

					// create a new note with the text read in
					newNote = new StickyNotes(c, frame);
					newNote.note.setText(allText);

					// use the next two lines, which contain the coordinates of the note
					// to position it on screen
					str = br.readLine();
					newNote.stickyX = Integer.parseInt(str);
					str = br.readLine();
					newNote.stickyY = Integer.parseInt(str);

					// add the newly creates note to the array list of sticky notes
					notes.add(newNote);

					// clear the text to add to the next note
					allText = "";
				}
			}

			// closes buffered reader
			br.close();
		} catch (Exception e) {
			// nothing will happen if crashes
		}
	}

	// writes the content of the sticky notes and it's coordinates to a file
	public void writeToSave() {
		try {
			// creates a BufferedWriter to write content to the file
			bw = new BufferedWriter(new FileWriter(file));
			// for every sticky note in the array of notes
			for (int i = 0; i < notes.size(); i++) {
				// write it's contents into a file
				// and end it with line with an untypeable character
				bw.write(notes.get(i).note.getText() + "\n÷\n");
				// save the x and y coordinates of the sticky note on the next two lines
				bw.write(notes.get(i).stickyX + "\n" + notes.get(i).stickyY + "\n");
			}
			bw.close();
			// closes the buffered reader
		} catch (Exception e) {
			// nothing will happen if crashes
		}
	}

}
