package com.nemustech.common;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import com.nemustech.common.test.TestAPI;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestAPI_vertx extends TestAPI {
	@BeforeClass
	public static void initClass() throws Exception {
//		arrayNode.add(readFile("src/test/resources/json/vertx/user_post_json.json"));
//		arrayNode.add(readFile("src/test/resources/json/vertx/user_get.json"));
		arrayNode.add(readFile("src/test/resources/json/vertx/user_get2.json"));
//		arrayNode.add(readFile("src/test/resources/json/vertx/user_put_json.json"));
//		arrayNode.add(readFile("src/test/resources/json/vertx/user_delete.json"));
//		arrayNode.add(readFile("src/test/resources/json/vertx/user_delete2.json"));

		arrayNode.add(readFile("src/test/resources/json/vertx/group_user_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/vertx/group_user_post.json"));
	}
}
