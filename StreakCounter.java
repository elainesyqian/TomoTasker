/* Elaine Qian and Shiloh ZHeng
 * January 17th, 2024
 * StreakCounter
 * This class reads and writes dates from an external file to keep track of the user's streak
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
	int counter;

	// constructor
	public StreakCounter() {
		file = new File("loginStreak.txt").getAbsoluteFile();
		calendar = Calendar.getInstance();

		todaysDate = getToday();
		curStreak = calculateStreakLength();
		writeDates();
		counter = 0;
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
		if (!(todaysDate + "").equals(getToday() + "")) {
			todaysDate = getToday();
			curStreak = calculateStreakLength();
			writeDates();
			TomoMenu.todaysQuote = TomoMenu.getQuote();
		}
		g.setColor(Color.BLACK);
		g.drawString("CURRENT STREAK: " + curStreak, 875, 400);
	}

}
