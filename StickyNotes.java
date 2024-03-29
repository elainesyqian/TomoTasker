/* Elaine Qian and Shiloh Zheng
 * January 21st, 2024
 * StickyNotes
 * This class creates a sticky note, which can be typed on, dragged around, and deleted
*/

//import statements
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class StickyNotes extends JPanel implements ActionListener {

	// creates the dimensions of the sticky note
	public static final int STICKY_LENGTH = 200;

	// start location of sticky notes
	public int stickyX = 475;
	public int stickyY = 100;

	public JTextArea note;
	public int notesVis = 1;
	public boolean delete = false;

	// variables for mouse dragging
	public int firstMouseX;
	public int firstMouseY;
	public int firstNoteX;
	public int firstNoteY;

	public JScrollPane scrollPart;
	public JButton closeButton;

	public ImageIcon x;

	public StickyNotes(Container c, TomoFrame frame) {
		// constructor of StickyNotes

		// creates a new JTextArea, which is the typeable area of the stickynote
		note = new JTextArea(1, STICKY_LENGTH);

		// creates an X close button for the sticky note

		// gets the x icon for the button
		x = new ImageIcon(new ImageIcon("x.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		// creates a close button
		closeButton = new JButton("X");
		closeButton.setFocusable(false);
		closeButton.setMargin(new Insets(20, 33, 21, 20));
		// adds an ActionListener to see when it's clicked
		closeButton.addActionListener(this);
		// sets the icon of the button
		closeButton.setIcon(x);
		c.add(closeButton, 0);

		// sets up the JTextArea so that words wrap
		// around to a new line when exceeding the edge
		note.setLineWrap(true);
		note.setWrapStyleWord(true);

		// sets the font for the text area
		note.setFont(new Font("Tahoma", Font.PLAIN, 12));

		// creates insets for the JTextArea so the text doesn't touch the edges
		note.setMargin(new Insets(3, 3, 3, 3));

		// makes the note scrollable and adds it to the screen
		scrollPart = new JScrollPane(note);
		c.add(scrollPart, 0);

		// refreshes and updates the container
		c.revalidate();
		c.repaint();

		// sets the note to visible when created
		notesVis = 1;
	}

	// draws the parts of the sticky note
	public void draw(Graphics g) {
		// if the sticky note is currently supposed to be visible
		if (notesVis == 1) {
			// creates the title tab that can be dragged
			g.setColor(Color.PINK);
			g.fillRect(stickyX, stickyY, STICKY_LENGTH, 30);

			// draws the rectangle border
			g.setColor(Color.BLACK);
			g.drawRect(stickyX, stickyY, STICKY_LENGTH, STICKY_LENGTH);

			// adds the JScrollPane with the text area
			scrollPart.setBounds(stickyX, stickyY + 30, STICKY_LENGTH, STICKY_LENGTH - 30);
			// adds the close button
			closeButton.setBounds(stickyX + STICKY_LENGTH - 27, stickyY + 5, 21, 21);
		} else {
			// hides the parts of the sticky note
			scrollPart.setVisible(false);
			closeButton.setVisible(false);
			note.setVisible(false);

			// marks the sticky note for deletion by TomoMenu
			delete = true;
		}

	}

	// called from TomoMenu when the mouse is pressed
	// saves the x and y values of the mouse press and note
	public void mousePressed(MouseEvent e) {
		// gets the x and y of the mouse and note when the mouse is pressed
		firstMouseX = e.getX();
		firstMouseY = e.getY();
		firstNoteX = stickyX;
		firstNoteY = stickyY;
	}

	// called from TomoMenu when the mouse is dragged
	// and uses the x and y values at the original click
	// to see if the note should be dragged
	public void mouseDragged(MouseEvent e) {
		// checks if the x and y values of the mouse fall within the draggable title tab
		// when the mouse
		// was first pressed down
		if (firstMouseX >= firstNoteX && firstMouseX <= firstNoteX + STICKY_LENGTH && firstMouseY >= firstNoteY
				&& firstMouseY <= firstNoteY + 30) {
			// if the mouse was in the title tab, allow for the note to be dragged
			// since the note is currently being dragged, make it follow the mouse
			stickyX = e.getX() - (firstMouseX - firstNoteX);
			stickyY = e.getY() - (firstMouseY - firstNoteY);

			// if the mouse drags the title tab offscreen, move it back onscreen
			if (stickyX < 0) {
				stickyX = 0;
			} else if (stickyX > TomoPanel.PANEL_WIDTH - STICKY_LENGTH) {
				stickyX = TomoPanel.PANEL_WIDTH - STICKY_LENGTH;
			}

			if (stickyY < 0) {
				stickyY = 0;
			} else if (stickyY + 30 > TomoPanel.PANEL_HEIGHT) {
				stickyY = TomoPanel.PANEL_HEIGHT - 30;
			}

			repaint(); // updates screen to show changes
		}
	}

	// detects when a button was pressed, and gets the ActionEvent
	public void actionPerformed(ActionEvent evt) {
		// inverts the current visibility
		notesVis = notesVis * -1;
	}

}
