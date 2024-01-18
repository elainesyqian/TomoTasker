/* Elaine Qian and Shiloh Zheng
 * January 17th, 2024
 * DateTimeDisplay
 * This class handles displaying the current date and time that the user is in to the screen
*/

//import necessary classes
import java.awt.*;

public class DateTimeDisplay {
	
	//variable declarations
	public String currentDate;
	public String formattedDate;
	public String formattedTime;
	
	//constructor
	public DateTimeDisplay() {
		
		//variable initializations
		
		//set new date using java system's current time
		currentDate = new java.util.Date(System.currentTimeMillis()) + "";
		
		//get the formatted date and time
		formattedDate = currentDate.substring(0,10) +","+ currentDate.substring(23,28);
		formattedTime = currentDate.substring(11, 16)+currentDate.substring(19, 23);
	}
	
	//this method takes care of drawing the date and time to screen
	public void draw(Graphics g) {
		
		//set font
		g.setColor(Color.WHITE);
		g.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		
		//draws the necessary information to screen
		g.drawString(formattedDate, 10, 30);
		g.drawString(formattedTime, 1050, 30);

		//updates the necessary information
		currentDate = new java.util.Date(System.currentTimeMillis()) + "";
		formattedDate = currentDate.substring(0,10) +","+ currentDate.substring(23,28);
		formattedTime = currentDate.substring(11, 16)+currentDate.substring(19, 23);
	}
	
}