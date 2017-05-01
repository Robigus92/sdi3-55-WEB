package uo.sdi.presentation.util;

import java.util.Calendar;
import java.util.Date;

public class MyDateUtil {

	static public boolean sameDay(Date d1, Date d2){
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		
		c1.setTime(d1);
		c2.setTime(d2);
		
		return (c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH))
				&& (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH))
				&& (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR));
		
		
	}
}
