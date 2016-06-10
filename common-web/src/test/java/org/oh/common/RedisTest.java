package org.oh.common;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.oh.common.util.CollectionUtil;
import org.oh.common.util.LogUtil;
import org.oh.common.util.MapUtil;
import org.oh.sample.model.Sample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:config-spring.xml")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RedisTest {
	@Autowired
	public RedisTemplate<String, Object> redisTemplate;

//	@Autowired
//	public RedisTemplate<String, Map<String, Object>> redisTemplateMap;
//
//	@Autowired
//	public RedisTemplate<String, Sample> redisTemplateSample;

	@Resource(name = "redisTemplate")
	public ValueOperations<String, Object> valueOps;

	@Resource(name = "redisTemplate")
	public ListOperations<String, Object> listOps;

	@Resource(name = "redisTemplate")
	public HashOperations<String, String, Object> hashOps;

	@Resource(name = "redisTemplate")
	public ListOperations<String, Map<String, Object>> mapListOps;

	@Resource(name = "redisTemplate")
	public ValueOperations<String, Sample> sampleOps;

	@Resource(name = "redisTemplate")
	public ListOperations<String, Sample> sampleListOps;

	public void flushDb() throws Exception {
		LogUtil.writeLog("flushDb");
		redisTemplate.execute(new RedisCallback<Object>() {
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				connection.flushDb();
				return null;
			}
		});
	}

	@Test
	public void test00Init() throws Exception {
		LogUtil.writeLog("========== Init ================================================================");

//		valueOps = redisTemplate.opsForValue();
//		listOps = redisTemplate.opsForList();
//		hashOps = redisTemplate.opsForHash();
//		mapListOps = redisTemplateMap.opsForList();
//		sampleOps = redisTemplateSample.opsForValue();
//		sampleListOps = redisTemplateSample.opsForList();

		flushDb();
	}

	@Test
	public void test10Value() throws Exception {
		LogUtil.writeLog("========== Value ===============================================================");

		valueOps.set("value", "1");

		Object value = valueOps.get("value");
		LogUtil.writeLog("get:" + value);

		value = valueOps.getAndSet("value", 2);
		LogUtil.writeLog("getAndSet:" + value);

		long size = valueOps.size("value");
		LogUtil.writeLog("size:" + size);
	}

//	@Test
	public void test11Sample() throws Exception {
		LogUtil.writeLog("========== Sample =============================================================");

		Sample model = new Sample();
		model.setId(1L);
		model.setName("a");

		sampleOps.set("model", model);

		long size = sampleOps.size("model");
		LogUtil.writeLog("size:" + size);

		// 주의) 가져온 객체의 결과값은 Map 이다.
		Map<String, Object> map = (Map) sampleOps.get("model");
		model = CollectionUtil.mapToObject(map, Sample.class);
		LogUtil.writeLog("get:" + model);

		map = (Map) sampleOps.getAndSet("model", model);
		model = CollectionUtil.mapToObject(map, Sample.class);
		LogUtil.writeLog("getAndSet:" + model);
	}

//	@Test
	public void test20Map() throws Exception {
		LogUtil.writeLog("========== Map =================================================================");

		hashOps.putAll("map", MapUtil.convertArrayToMap(new Object[] { "1", "11" }, new Object[] { "2", "22" }));
		hashOps.putAll("map", MapUtil.convertArrayToMap(new Object[] { "3", 33 }));

		long size = hashOps.size("map");
		LogUtil.writeLog("size:" + size);

		Map<String, Object> map = hashOps.entries("map");
		LogUtil.writeLog("entries:" + map);

		Set<String> set = hashOps.keys("map");
		LogUtil.writeLog("keys:" + set);

		List<Object> list = hashOps.values("map");
		LogUtil.writeLog("values:" + list);

		hashOps.delete("map", "1", "2");

		map = hashOps.entries("map");
		LogUtil.writeLog("entries" + map);
	}

//	@Test
	public void test30List() throws Exception {
		LogUtil.writeLog("========== List ================================================================");

		listOps.leftPushAll("list", (Collection) Arrays.asList("1", "2"));
		listOps.leftPushAll("list", (Collection) Arrays.asList("2"));
		listOps.leftPushAll("list", (Collection) Arrays.asList(3));

		long size = listOps.size("list");
		LogUtil.writeLog("size:" + size);

		List<Object> list = listOps.range("list", 0, size - 1);
		LogUtil.writeLog("range:" + list);

		Object value = listOps.rightPop("list");
		LogUtil.writeLog("rightPop:" + value);

		list = listOps.range("list", 0, listOps.size("list") - 1);
		LogUtil.writeLog("range:" + list);

		value = listOps.index("list", 1);
		LogUtil.writeLog("index:" + value);

		size = listOps.remove("list", 0, value);
		LogUtil.writeLog("remove:" + size);

		list = listOps.range("list", 0, listOps.size("list") - 1);
		LogUtil.writeLog("range:" + list);
	}

//	@Test
	public void test31MapList() throws Exception {
		LogUtil.writeLog("========== MapList =============================================================");

		mapListOps.leftPushAll("mapList",
				(Collection) Arrays.asList(MapUtil.convertArrayToMap(new Object[] { "1", "11" }),
						MapUtil.convertArrayToMap(new Object[] { "2", "22" })));
		mapListOps.leftPushAll("mapList",
				(Collection) Arrays.asList(MapUtil.convertArrayToMap(new Object[] { "2", "22" })));
		mapListOps.leftPushAll("mapList", (Collection) Arrays
				.asList(MapUtil.convertArrayToMap(new Object[] { "2", "22" }, new Object[] { "3", 33 })));

		long size = mapListOps.size("mapList");
		LogUtil.writeLog("size:" + size);

		List<Map<String, Object>> list = mapListOps.range("mapList", 0, size - 1);
		LogUtil.writeLog("range:" + list);

		Map<String, Object> map = mapListOps.rightPop("mapList");
		LogUtil.writeLog("rightPop:" + map);

		list = mapListOps.range("mapList", 0, mapListOps.size("mapList") - 1);
		LogUtil.writeLog("range:" + list);

		map = mapListOps.index("mapList", 1);
		LogUtil.writeLog("index:" + map);

		size = mapListOps.remove("mapList", 0, map);
		LogUtil.writeLog("remove:" + size);

		list = mapListOps.range("mapList", 0, mapListOps.size("mapList") - 1);
		LogUtil.writeLog("range:" + list);
	}

//	@Test
	public void test32SampleList() throws Exception {
		LogUtil.writeLog("========== SampleList =========================================================");

		Sample model = new Sample();
		model.setId(1L);
		model.setName("11");

		Sample sample2 = new Sample();
		sample2.setId(2L);
		sample2.setName("22");

		sampleListOps.leftPushAll("sampleList", (Collection) Arrays.asList(model, sample2));

		sampleListOps.leftPushAll("sampleList", (Collection) Arrays.asList(sample2));

		Sample sample3 = new Sample();
		sample3.setId(3L);
		sample3.setName("33");

		sampleListOps.leftPushAll("sampleList", Arrays.asList(sample3));

		long size = sampleListOps.size("sampleList");
		LogUtil.writeLog("size:" + size);

		List<Sample> list = (List) sampleListOps.range("sampleList", 0, size - 1);
		LogUtil.writeLog("range:" + list);

		Map<String, Object> map = (Map) sampleListOps.rightPop("sampleList");
		model = CollectionUtil.mapToObject(map, Sample.class);
		LogUtil.writeLog("rightPop:" + map);

		list = (List) sampleListOps.range("sampleList", 0, sampleListOps.size("sampleList") - 1);
		LogUtil.writeLog("range:" + list);

		// 주의) 가져온 객체의 결과값은 Map 이다.
		map = (Map) sampleListOps.index("sampleList", 1);
		model = CollectionUtil.mapToObject(map, Sample.class);
		LogUtil.writeLog("index:" + map);

		size = sampleListOps.remove("sampleList", 0, model);
		LogUtil.writeLog("remove:" + size);

		list = (List) sampleListOps.range("sampleList", 0, sampleListOps.size("sampleList") - 1);
		LogUtil.writeLog("range:" + list);
	}

//	@Test
	public void test90Template() throws Exception {
		LogUtil.writeLog("========== Template ============================================================");

		Set<String> set = redisTemplate.keys("*");
		LogUtil.writeLog("keys:" + set);
	}

}
