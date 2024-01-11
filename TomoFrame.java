/* Elaine Qian and Shiloh Zheng
 * January 11th, 2024
 * TomoFrame
 * This class establishes the frame for the program
*/

import java.awt.*;
import javax.swing.*;

public class TomoFrame extends JFrame{

	TomoPanel panel;
	Container c;
	
	public TomoFrame() {
		// creates a container to put things in later
		c = getContentPane();
		c.setLayout(new BorderLayout());
		
		panel = new TomoPanel(c, this); // run TomoPanel constructor

		// adds the panel into the frame
		this.add(panel);
		
		this.setTitle("TOMOTASKER"); // set title for frame
		this.setResizable(false); // frame can't change size
		this.setBackground(Color.white); // sets background color for the frame
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // X button will stop program execution
		this.pack();// makes components fit in window
		this.setVisible(true); // makes window visible to user
		this.setLocationRelativeTo(null);// set window in middle of screen

	}

	// method that changes the background colour of the frame
	// it takes in a Color, and changes it's background to it
	public void frameBgChange(Color color) {
		// sets the background colour to the given colour
		this.setBackground(color);
	}
	
}
