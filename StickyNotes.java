/* Elaine Qian and Shiloh Zheng
 * January 11th, 2024
 * StickyNotes
 * This class creates a checklist of tasks, which can be edited, removed, or added
*/

import java.awt.*;
import java.awt.event.*;
import java.awt.event.MouseEvent;

import javax.swing.*;

public class StickyNotes extends JPanel implements ActionListener{
	
	public static final int STICKY_LENGTH = 200;
	public int stickyX = 350;
	public int stickyY = 350;
	
	public JTextArea note;
	int notesVis = 1;
	boolean delete = false;
	
	// boolean that saves whether or not the mouse is currently dragging the note
	public boolean mouseDragging = false;
	
	public int firstMouseX;
	public int firstMouseY;
	public int firstNoteX;
	public int firstNoteY;
	
	public JScrollPane scrollPart;
	public JButton closeButton;
	
	public StickyNotes(Container c, TomoFrame frame) {
		
		note = new JTextArea(1, STICKY_LENGTH);
		
		closeButton = new JButton("X");
		closeButton.addActionListener(this);
		

		note.setLineWrap(true);  // wraps the words
		note.setWrapStyleWord(true);
		
		note.setMargin(new Insets(3, 3, 3, 3));
		
		//notePanel.add(note);
		//note.setVisible(true);
		
		scrollPart = new JScrollPane(note);
		
		c.revalidate();
		
		c.add(closeButton, 0);
		
		c.add(scrollPart, 0);
	}
	
	public void draw(Graphics g) {
		// if the sticky note is currently supposed to be visible
		if (notesVis == 1) {
			// draws the rectangle border
			
			// creates the title tab that can be dragged
			g.setColor(Color.PINK);
			g.fillRect(stickyX, stickyY, STICKY_LENGTH, 30);
			
			g.setColor(Color.BLACK);
			g.drawRect(stickyX, stickyY, STICKY_LENGTH, STICKY_LENGTH);
			
			scrollPart.setBounds(stickyX, stickyY + 30 ,  STICKY_LENGTH,  STICKY_LENGTH-30);
			closeButton.setBounds(stickyX +  STICKY_LENGTH - 30, stickyY + 5,  20,  20);
		} else {
			// hides the sticky note
			scrollPart.setVisible(false);
			closeButton.setVisible(false);
			note.setVisible(false);
			
			delete = true;
		}

	}
	
		// called from TomoMenu when the mouse is pressed
		// saves the x and y values of the mouse press and note
		public void mousePressed(MouseEvent e) {
			// gets the x and y of the mouse and list when the mouse is pressed
			firstMouseX = e.getX();
			firstMouseY = e.getY();
			firstNoteX = stickyX;
			firstNoteY = stickyY;
		}

		// called from TomoMenu when the mouse is dragged
		// and uses the x and y values at the original click
		// to see if the note should be dragged
		public void mouseDragged(MouseEvent e) {
			// checks if the x and y values of the mouse fall within the draggable title tab when the mouse
			// was first pressed down
			if (firstMouseX >= firstNoteX && firstMouseX <= firstNoteX + STICKY_LENGTH && firstMouseY >= firstNoteY
					&& firstMouseY <= firstNoteY + 30) {
				// if the mouse was in the title tab, allow for the list to be dragged
				mouseDragging = true;
			} else {
				// otherwise, don't drag the list
				mouseDragging = false;
			}

			if (mouseDragging) {
				// if the list is currently being dragged, make it follow the mouse
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
		
		public void actionPerformed(ActionEvent evt) {
			notesVis = notesVis*-1;
		}
		
}