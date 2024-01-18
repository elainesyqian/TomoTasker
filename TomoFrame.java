/* Elaine Qian and Shiloh Zheng
 * TomoFrame
 * This class establishes the frame for the program
 */

import java.awt.*;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

public class TomoFrame extends JFrame {

	TomoPanel panel;
	Container c;
	
	public TomoFrame() {
		c = getContentPane();
		c.setLayout(new BorderLayout());
		
		panel = new TomoPanel(c, this); // run TomoPanel constructor
		
		this.add(panel);
		
		this.setTitle("TOMOTASKER"); // set title for frame
		//this.setResizable(false); // frame can't change size
		//this.setBackground(Color.white); // sets background color for the frame
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // X button will stop program execution
		this.pack();// makes components fit in window
		this.setVisible(true); // makes window visible to user
		this.setLocationRelativeTo(null);// set window in middle of screen

	}
	
	public void frameBgChange(Color color) {
		this.setBackground(color);
	}
	
}
