package org.oh.common;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.io.FileUtils;
import org.oh.common.util.Utils;

public class Test02 {
	public static void calTest() throws Exception {
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

	public static String formatTime(Calendar cal) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX zzz");
		sdf.setCalendar(cal);
		return sdf.format(cal.getTime());
	}

	public static Calendar parseTime(String formattedDateTime, String ID) throws Exception {
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

	public static void readFile(String path) throws Exception {
		List<String> list = FileUtils.readLines(new File(path), "UTF-8");
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
	}

	String s = "1";

	public void test01() throws Exception {
//		String s = "2";
//		System.out.println(s);
//		s = this.s;
//		System.out.println(s);
//		System.out.println(HttpStatus.INTERNAL_SERVER_ERROR.toString());
//		System.out.println("123456789012345678901234567890");
//		String staticSql = "  SELECT      ABC   FROM ";
//		int idxSelect = staticSql.indexOf("SELECT");
//		int idxFrom = staticSql.indexOf("FROM");
//		String select = staticSql.substring(0, idxSelect) + staticSql.substring(idxSelect, idxSelect + 6);
//		staticSql = select + " fields " + staticSql.substring(idxFrom);
//		System.out.println(staticSql);
//		staticSql = select + " DISTINCT " + staticSql.substring(idxSelect + 7);
//		System.out.println(staticSql);
		
//		Test01 test01 = new Test01();
//		System.out.println(ReflectionUtil.getValue(test01, "id"));
//		ReflectionUtil.setValue(test01, "id", 2);
//		System.out.println(ReflectionUtil.getValue(test01, "id"));

//		System.out.println(ReflectionUtil.findField(test01.getClass(), "id", long.class));
//		System.out.println(ReflectionUtil.existField(test01, "name"));
		
//		System.out.println(JsonUtil2.toStringPretty("a"));
		
		System.out.println(new Date(1469782800000L));
		System.out.println(Utils.convertDateTimeToString(new Date(1469782800000L)));
	}
	
	class Test01 {
		private long id = 1;
	}

	public static void main(String[] args) throws Exception {
		new Test02().test01();
//		ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("config-spring.xml");
//		String[] beanNames = context.getBeanDefinitionNames();
//		for (String beanName : beanNames) {
//			try {
//				System.out.println(beanName + "\t");
//				System.out.println(context.getBean(beanName).getClass().getName());
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		context.close();

//		String[] timezoneIdArr = TimeZone.getAvailableIDs();
//		for (String tzId : timezoneIdArr) {
//			System.out.println(tzId);
//		}
//
//		calTest();

//		readFile("src/test/resources/json/admin_gateway_post.json");
//		System.out.println(MessageFormat.format("1{0}3{1}", new Object[] { "2", "4" }));
//		System.out.println(MessageFormat.format("13", new Object[] { "2", "4" }));

//		System.out.println(new Date());
	}

}
