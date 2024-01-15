/* Elaine Qian and Shiloh ZHeng
 * January 11nd, 2024
 * StreakCounter
 * This class reads and writes dates from an external file to keep track of the user's streak
 * Still a work in progress as this is does not work right now
*/

//import statements
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.*;
import java.util.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.text.SimpleDateFormat;

public class StreakCounter {

	public static File file;
	public static BufferedWriter bw;
	public static BufferedReader br;

	public static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public Date todaysDate;
	public Date curDate;
	public String str;

	public Date dayBefore;
	public static Calendar calendar;

	public int curStreak;

	// constructor
	public StreakCounter() {
		file = new File("loginStreak.txt").getAbsoluteFile();
		calendar = Calendar.getInstance();
	}

	// get todays date and saves to variables
	public Date getToday() {
		try {
			curDate = sdf.parse(dtf.format(LocalDateTime.now()));
		} catch (Exception e) {

		}
		return curDate;
	}

	// this method calculates the streak length
	public int calculateStreakLength() {
		int counter = 0;

		calendar.setTime(todaysDate);
		calendar.add(Calendar.DATE, -1);
		dayBefore = calendar.getTime();

		try {
			br = new BufferedReader(new FileReader(file));
			while ((str = br.readLine()) != null) {
				while (str.contains(dayBefore + "")) {
					counter++;
					calendar.add(Calendar.DATE, -1);
					dayBefore = calendar.getTime();
				}
			}
			br.close();
		} catch (Exception e) {

		}

		return counter + 1;
	}

	public void writeDates() {
		calendar.setTime(todaysDate);

		try {
			bw = new BufferedWriter(new FileWriter(file));
			bw.write(todaysDate + "");
			for (int i = 0; i < (curStreak - 1); i++) {
				calendar.add(Calendar.DATE, -1);
				dayBefore = calendar.getTime();
				bw.write(dayBefore + "");
			}
			bw.close();
		} catch (Exception e) {

		}
	}
	
	public void draw(Graphics g) {
		if (todaysDate != getToday()) {
			todaysDate = getToday();
			curStreak = calculateStreakLength();
			writeDates();
		} 
		g.setColor(Color.pink);
		g.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		g.drawString("Current Streak: "+curStreak, 10, 635);
	}

}
