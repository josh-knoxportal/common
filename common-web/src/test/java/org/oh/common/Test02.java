package org.oh.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import org.oh.common.util.LogUtil;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test02 {
	private static void calTest() {
		String zone = "KST";
//		String zone = "Asia/Seoul";
//		String zone = "PST";
//		String zone = "GMT";

		long millis = System.currentTimeMillis();

		// get calendar of Korea time zone.
		Calendar kst = Calendar.getInstance(TimeZone.getTimeZone(zone));

		// set its time to a UTC millisecond value. probably redundant, but just to demonstrate
		kst.setTimeInMillis(millis);

		String formattedKst = formatTime(kst);
		System.out.println(" Original - " + formattedKst);

		// now we convert the formatted string back to a Calendar .
		Calendar parsedKst = parseTime(formattedKst, zone);
		System.out.print(" Parsed   - ");
		System.out.println("" + parsedKst.get(Calendar.YEAR) + "-" + (parsedKst.get(Calendar.MONTH) + 1) + "-"
				+ parsedKst.get(Calendar.DATE) + " " + parsedKst.get(Calendar.HOUR_OF_DAY) + ":"
				+ parsedKst.get(Calendar.MINUTE) + ":" + parsedKst.get(Calendar.SECOND) + "."
				+ parsedKst.get(Calendar.MILLISECOND) + " " + parsedKst.getTimeZone().getID() + " "
				+ parsedKst.getTimeZone().getDisplayName() + " ");

	}

	private static String formatTime(Calendar cal) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX zzz");
		sdf.setCalendar(cal);
		return sdf.format(cal.getTime());
	}

	private static Calendar parseTime(String formattedDateTime, String ID) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX zzz");
		sdf.setTimeZone(TimeZone.getTimeZone(ID));
		sdf.setLenient(false);
		try {
			sdf.parse(formattedDateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sdf.getCalendar();
	}

	public static void main(String[] args) throws Exception {
//		ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("config-spring.xml");
//		String[] beanNames = context.getBeanDefinitionNames();
//		for (String beanName : beanNames) {
//			try {
//				LogUtil.writeLog(beanName + "\t");
//				LogUtil.writeLog(context.getBean(beanName).getClass().getName());
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		context.close();

		String[] timezoneIdArr = TimeZone.getAvailableIDs();
		for (String tzId : timezoneIdArr) {
			System.out.println(tzId);
		}

		calTest();
	}

}
