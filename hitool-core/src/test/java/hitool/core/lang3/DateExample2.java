package hitool.core.lang3;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import hitool.core.lang3.time.TimeUtils;

public class DateExample2 {
	public static void main(String[] args) {
		
		SimpleDateFormat bartDateFormat = new SimpleDateFormat("dd",Locale.ENGLISH);
		Date date = new Date();
		System.out.println(bartDateFormat.format(date));
		//EEEE-MMMM-dd-yyyy:Monday-July-25-2016
		//EEEE-MM-dd-yyyy:Monday-07-25-2016
		//EE-MMMM-dd-yyyy:Mon-July-25-2016
		//EE-MM-dd-yyyy:Mon-07-25-2016
		System.err.println( "Year:" + TimeUtils.getYear());
		System.err.println( "Month:" + TimeUtils.getMonth());
		System.err.println( "Week:" + TimeUtils.getWeek());
		System.err.println( "Day:" + TimeUtils.getDay());
		
	}
}
