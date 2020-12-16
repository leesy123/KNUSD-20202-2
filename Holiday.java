package swproject;

import java.util.ArrayList;
import java.util.Calendar;
import com.ibm.icu.util.ChineseCalendar;

public class Holiday {

	public static boolean isLunar(String dt) {
		boolean result = false;
		ArrayList<String> arrLunar = new ArrayList<String>();
		
		arrLunar.add("0101");
		arrLunar.add("0102");
		arrLunar.add("0408");
		arrLunar.add("0814");
		arrLunar.add("0815");
		arrLunar.add("0816");
		arrLunar.add("1230");
		
		ChineseCalendar chinaCal = new ChineseCalendar();
		Calendar cal = Calendar.getInstance();
		
		cal.set(Calendar.YEAR, Integer.parseInt(dt.substring(0, 4)));
		cal.set(Calendar.MONTH, Integer.parseInt(dt.substring(4, 6)) - 1);
		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dt.substring(6)));
		chinaCal.setTimeInMillis(cal.getTimeInMillis());
		
		int chinaMM = chinaCal.get(ChineseCalendar.MONTH) + 1;
		int chinaDD = chinaCal.get(ChineseCalendar.DAY_OF_MONTH);
		
		String chinaDate = "";
		
		if (chinaMM < 10)
			chinaDate += "0" + Integer.toString(chinaMM);
		else
			chinaDate += Integer.toString(chinaMM);
		
		if (chinaDD < 10)
			chinaDate += "0" + Integer.toString(chinaDD);
		else
			chinaDate += Integer.toString(chinaDD);
		
		for (int i = 0; i < arrLunar.size(); i++) {
			String tmpLunar = arrLunar.get(i);
			
			if (tmpLunar.equals(chinaDate))
				result = true;
		}
		
		return result;
	}
	
	public static boolean LiftHoliday(String dt) {
		boolean result = false;
		ArrayList<String> arrLift = new ArrayList<String>();
		
		arrLift.add("0101");
		arrLift.add("0127"); //올해 설연휴 대체공휴일
		arrLift.add("0301");
		arrLift.add("0415");
		arrLift.add("0505");
		arrLift.add("0606");
		arrLift.add("0815");
		arrLift.add("0817"); //올해 광복절 대체공휴일
		arrLift.add("1003");
		arrLift.add("1009");
		arrLift.add("1225");

		int yy = Integer.parseInt(dt.substring(0, 4));
		int mm = Integer.parseInt(dt.substring(4, 6));
		int dd = Integer.parseInt(dt.substring(6));
		
		String date = "";
		
		if (mm < 10)
			date += "0" + Integer.toString(mm);
		else
			date += Integer.toString(mm);
		
		if (dd < 10)
			date += "0" + Integer.toString(dd);
		else
			date += Integer.toString(dd);
		
		for (int i = 0; i < arrLift.size(); i++) {
			String tmpLunar = arrLift.get(i);
			
			if (tmpLunar.equals(date))
				result = true;
		}
		
		return result;
	}
	
}
