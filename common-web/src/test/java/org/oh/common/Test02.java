package org.oh.common;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.io.FileUtils;
import org.oh.common.util.JsonUtil2;
import org.oh.sample.model.Sample;

import com.fasterxml.jackson.core.type.TypeReference;

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

//		System.out.println(new Date(1469782800000L));
//		System.out.println(Utils.convertDateTimeToString(new Date(1469782800000L)));

//		List<String> list = new ArrayList<String>();
//		list.add("a");
//		List<String> list2 = new ArrayList<String>();
////		list2.add("a");
//		list2.add("b");
//		System.out.println(list.retainAll(list2));
//		System.out.println(list);
//		System.out.println(list2);

//		System.out.println(5 % 2);
//		System.out.println((int) Math.ceil(10 * 1.0));
//		List<Integer> selectRouteLogList = new ArrayList<Integer>();
//		for (int i = 0; i < 4; i++) {
//			selectRouteLogList.add(i);
//		}
//		System.out.println(Utils.toString(selectRouteLogList));
//
//		int INSERT_LOG_REPEAT_COUNT = 2;
//		int size = selectRouteLogList.size();
////		System.out.println(size / INSERT_LOG_REPEAT_COUNT);
//		for (int i = 0; i < (size / INSERT_LOG_REPEAT_COUNT) + 1; i++) {
//			List<Integer> selectRouteLogList_temp = null;
//
//			if (i == (size / INSERT_LOG_REPEAT_COUNT)) {
//				if ((size % INSERT_LOG_REPEAT_COUNT) == 0)
//					break;
//				else
//					selectRouteLogList_temp = selectRouteLogList.subList(INSERT_LOG_REPEAT_COUNT * i, size);
//			} else {
//				selectRouteLogList_temp = selectRouteLogList.subList(INSERT_LOG_REPEAT_COUNT * i,
//						INSERT_LOG_REPEAT_COUNT * (i + 1));
//			}
//
//			System.out.println(i + " " + Utils.toString(selectRouteLogList_temp));
//		}

//		for (int i = -10; i <= 0; i++) {
//			System.out.println(i);
//		}

//		ExpressionParser parser = new SpelExpressionParser();
//		Expression exp = parser.parseExpression(
//				"T(org.oh.common.util.PropertyUtils).getInstance().getString('web.requestmapping.postfix', '')");
//		System.out.println(exp.getValue());

//		System.out.println(getClass().getResource("").getPath());
//		System.out.println(getClass().getClassLoader().getResource("common.properties").getPath());
//		System.out.println("A20160920".compareTo("A20160919") > 0);
//		System.out.println("A20160920".compareTo("A20160920") > 0);
//		System.out.println(FilenameUtils.getPathNoEndSeparator("/a/b/c.xtx"));

//		Files files = new Files("p1", "n1", "1".getBytes());
//		Files2 files2 = new Files2(files, "1");
//		System.out.println(files2);

//		File file = new File("./a/b/c.txt");
//		// 절대경로
//		System.out.println(file.getAbsolutePath()); // /Users/skoh/git/skoh/common/common-web/./a/b/c.txt
//		System.out.println(file.getCanonicalPath()); // /Users/skoh/git/skoh/common/common-web/a/b/c.txt
//		// 상대경로
//		System.out.println(file.getPath()); // ./a/b/c.txt
//		System.out.println(file.getParent()); // ./a/b
//		System.out.println(file.getName()); // c.txt

//		Set<Test> set = new LinkedHashSet<Test>();
//		set.add(new Test(2L, "t", 2L));
//		set.add(new Test(2L, "t", 2L));
//		set.add(new Test(6L, "t", 2L));
//		set.add(new Test(4L, "t", 2L));
//		set.add(new Test(1L, "t", 2L));
//		set.add(new Test(1L, "t", 2L));
//		set.add(new Test(5L, "t", 2L));
//		set.add(new Test(3L, "t", 2L));
//		System.out.println(JsonUtil2.toStringPretty(set));

//		List<SampleAndTest> list = Arrays.asList(new SampleAndTest[] {
//				new SampleAndTest(new Sample(1L, "s1", 2L), new Test(1L, "t1", 2L),
//						new Files2(new Files("f1", null), null)),
//				new SampleAndTest(new Sample(2L, "s2", 2L), new Test(2L, "t1", 2L),
//						new Files2(new Files("f2", null), null)) });
//
//		List<Default> list2 = MapperUtils.convertModels(list, "testSet", "filesSet");
//		System.out.println("list: " + list2);
//		System.out.println("json: " + JsonUtil2.toStringPretty(list2));

//		ArrayList<LinkedHashSet<Default>> list = new ArrayList<LinkedHashSet<Default>>();
//		list.get(0).add(new Sample(1L, "s1", 2L));
//		list.get(0).add(new Sample(1L, "s1", 2L));
//
//		System.out.println("json: " + JsonUtil2.toStringPretty(list));

		List<List<Sample>> list = Arrays.asList(Arrays.asList(new Sample[] { new Sample(), new Sample(), }));
		String json = JsonUtil2.toString(list);
		list = JsonUtil2.getObjectMapper().readValue(json, new TypeReference<List<List<Sample>>>() {
		});
		System.out.println("list: " + JsonUtil2.toStringPretty(list));
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
