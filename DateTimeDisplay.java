//important import statements  
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeDisplay {
// main method  
	
	String date;
	String formattedDate;
	String formattedTime;
	long millis;
	
	public DateTimeDisplay() {
		millis = System.currentTimeMillis();
		date = new java.util.Date(millis) + "";
		
		formattedDate = date.substring(0,10) +","+ date.substring(23,28);
		formattedTime = date.substring(11, 16)+date.substring(19, 23);
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.black);
		g.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		
		g.drawString(formattedDate, 10, 30);
		g.drawString(formattedTime, 1050, 30);
		
		millis = System.currentTimeMillis();
		date = new java.util.Date(millis) + "";
		formattedDate = date.substring(0,10) +","+ date.substring(23,28);
		formattedTime = date.substring(11, 16)+date.substring(19, 23);
		
	}
	
}