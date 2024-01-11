import java.io.*;
import java.util.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class StreakCounter {

	public static File file;
	public static BufferedWriter bw;
	public static BufferedReader br;

	public static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public static Date todaysDate;
	public static String today;
	public String str;

	public Date dayBefore;
	public Calendar calendar;

	public int curStreak;
	public StreakCounter streak;

	public StreakCounter() throws ParseException, IOException {

		file = new File("loginStreak.txt").getAbsoluteFile();
		bw = new BufferedWriter(new FileWriter(file));
		br = new BufferedReader(new FileReader(file));

//		getToday();
//
//		curStreak = calculateStreakLength();
//
//		if (!checkToday()) {
//			bw.write(today);
//		}
	}

	public void getToday() throws ParseException {
		today = dtf.format(LocalDateTime.now());
		todaysDate = sdf.parse(today);
		today = todaysDate + "";
	}

	public void clear() {
		// search up how to clear a text file
	}

	public int calculateStreakLength() throws IOException {
		calendar = Calendar.getInstance();
		calendar.setTime(todaysDate);

		calendar.add(Calendar.DATE, -1);
		dayBefore = calendar.getTime();

		int counter = 1;

		while ((str = br.readLine()) != null) {
			System.out.println(dayBefore);
			while (str.contains(dayBefore + "")) {
				counter++;
				calendar.add(Calendar.DATE, -1);
				dayBefore = calendar.getTime();
				System.out.println(dayBefore);
			}
		}

		
//		if (counter == 1) {
//			clear();
//			bw = new BufferedWriter(new FileWriter(file));
//			bw.write(today);
//			bw.close();
//		}

		return counter;
	}

	public boolean checkToday() throws IOException {

		while ((str = br.readLine()) != null) {
			if (str.contains(today)) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) throws ParseException, IOException {
		
		StreakCounter streak = new StreakCounter();
		
		streak.getToday();
		
		System.out.println(streak.calculateStreakLength());
		
		//Thu Jan 11 00:00:00 EST 2024Wed Jan 10 00:00:00 EST 2024Tue Jan 09 00:00:00 EST 2024
		
//		
//		file = new File("loginStreak.txt").getAbsoluteFile();
//		bw = new BufferedWriter(new FileWriter(file));
//		br = new BufferedReader(new FileReader(file));
//		
//		today = dtf.format(LocalDateTime.now());
//		todaysDate = sdf.parse(today);
//		today = todaysDate + "";
//		
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(todaysDate);
//		bw.write(todaysDate+"");
//		
//		calendar.add(Calendar.DATE, -1);
//		Date oneday = calendar.getTime();
//		bw.write(oneday+"");
//		
//		calendar.add(Calendar.DATE, -1);
//		Date twoday = calendar.getTime();
//		bw.write(twoday+"");
//		
//		bw.close();
		
		String st;

		while ((st = br.readLine()) != null) {
			System.out.println(st);
		}
//		
//		
//		Date date = sdf.parse(today);
//		
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(todaysDate);
//		
//		calendar.add(Calendar.DATE, -1);
//		Date oneday = calendar.getTime();
//		calendar.add(Calendar.DATE, -1);
//		Date twoday = calendar.getTime();
//		System.out.println(todaysDate);
//		System.out.println(oneday);
//		System.out.println(twoday);
//		
//		file = new File("loginStreak.txt").getAbsoluteFile();
//		BufferedReader br = new BufferedReader(new FileReader(file));
//
//		String st;
//
//		while ((st = br.readLine()) != null) {
//			System.out.println(st);
//		}
//		
//		br.close();
	}
}
