/* Elaine Qian and Shiloh Zheng
 * January 21st, 2024
 * TomoFrame
 * This class establishes the frame for the program
 */

//import statements
import java.awt.*;
import javax.swing.*;

public class TomoFrame extends JFrame {

	// variable declarations
	TomoPanel panel;
	Container c;

	// TomoFrame constructor called by Main
	public TomoFrame() {
		c = getContentPane();
		c.setLayout(new BorderLayout());

		panel = new TomoPanel(c, this); // run TomoPanel constructor
		this.add(panel); // add the panel to frame

		this.setTitle("TOMOTASKER"); // set title for frame
		this.setResizable(false); // frame can't change size
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // X button will stop program execution
		this.pack();// makes components fit in window
		this.setVisible(true); // makes window visible to user
		this.setLocationRelativeTo(null);// set window in middle of screen

	}

}
