/* Elaine Qian and Shiloh Zheng
 * January 17th, 2024
 * StreakCounter
 * This class reads and writes dates from an external file to keep track of the user's streak
*/

//import statements
import java.awt.*;
import java.io.*;
import java.util.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.text.SimpleDateFormat;

public class StreakCounter {

	//variables for reading the file
	public static File file;
	public static BufferedWriter bw;
	public static BufferedReader br;

	// formatting the dates
	public static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	//variable declarations
	public Date todaysDate;
	public Date curDate;
	public String str;
	
	public Date dayBefore;
	public static Calendar calendar;

	public int curStreak;
	public int counter = 0;

	// constructor
	public StreakCounter() {
		//gets the file
		file = new File("loginStreak.txt").getAbsoluteFile();
		
		//sets up calendar
		calendar = Calendar.getInstance();

		//gets todays date
		todaysDate = getToday();
		
		//calculates streak length and stores it in curStreak
		curStreak = calculateStreakLength();
		
		//write the dates "on streak" back into the file
		writeDates();
	}

	// get todays date and saves to respective variables
	public Date getToday() {
		
		//formats the local.date.time into usable date format
		try {
			curDate = sdf.parse(dtf.format(LocalDateTime.now()));
		} catch (Exception e) {
			// nothing will happen if crashes
		}
		return curDate;
	}

	// this method calculates the streak length
	public int calculateStreakLength() {
		
		//calendar setup before counting streak
		calendar.setTime(todaysDate);
		calendar.add(Calendar.DATE, -1);
		dayBefore = calendar.getTime();

		try {
			//setup buffered reader to read from the txt file
			br = new BufferedReader(new FileReader(file));
			
			//while the next line has content
			while ((str = br.readLine()) != null) {
				
				//if the line contains the date of the day before
				while (str.contains(dayBefore + "")) {
					//increase counter by 1
					counter++;
					
					//set dayBefore to the day before again
					calendar.add(Calendar.DATE, -1);
					dayBefore = calendar.getTime();
				}
			}
			//closes buffered reader
			br.close();
		} catch (Exception e) {
			// nothing will happen if crashes
		}

		//returns the current streak (+1 bc need to include today/they day they login)
		return counter + 1;
	}

	//this method writes all days logged in on a streak back into the txt file
	//must do this bc cannot just write 1 date into the file, buffered writer will automatically clear the file
	public void writeDates() {
		//reset the calendar
		calendar.setTime(todaysDate);
		
		try {
			//setup buffered writer to write dates back into the txt file
			bw = new BufferedWriter(new FileWriter(file));
			
			//write todays date into the file
			bw.write(todaysDate + "");
			
			//for every day in the streak
			for (int i = 1; i < curStreak; i++) {
				//write the day before back into the txt file
				calendar.add(Calendar.DATE, -1);
				dayBefore = calendar.getTime();
				bw.write(dayBefore + "");
			}
			
			//closes the buffered writer
			bw.close();
		} catch (Exception e) {
			// nothing will happen if crashes
		}
	}

	//this method draws the current streak to screen and recalculates the streak if necessary
	public void draw(Graphics g) {
		
		//if todaysDate is no longer today's date (aka becomes yesterdays date)
		if (!(todaysDate + "").equals(getToday() + "")) {
			//update todays date
			todaysDate = getToday();
			
			//recalculates the streak
			curStreak = calculateStreakLength();
			
			//writes dates back into the txt file
			writeDates();
			
			//reset the quote to a new quote
			TomoMenu.todaysQuote = TomoMenu.getQuote();
		}
		
		//draws the current streak to screen
		g.setColor(Color.BLACK);
		g.drawString("CURRENT STREAK: " + curStreak, 875, 400);
	}

}
