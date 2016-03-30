package org.oh.common;

import org.apache.commons.lang.StringUtils;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestAPI_skoh extends TestAPI {
	protected static ArrayNode arrayNode = new ArrayNode(JsonNodeFactory.instance);

	@BeforeClass
	public static void initClass() throws Exception {
//		arrayNode.add(readFile("src/test/resources/json/cbms_company_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/cbms_company_get2.json"));
//		arrayNode.add(readFile("src/test/resources/json/cbms_company_put.json"));
//		arrayNode.add(readFile("src/test/resources/json/cbms_company_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/cbms_company_get2.json"));
//		arrayNode.add(readFile("src/test/resources/json/test.json"));
//		arrayNode.add(readFile("src/test/resources/json/sample_page_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/sample_page2_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/mms_rssi_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/mms_rssi_get2.json"));
//		arrayNode.add(readFile("src/test/resources/json/mms_rssi_put.json"));
//		arrayNode.add(readFile("src/test/resources/json/mms_rssi_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/mms_rssi_get2.json"));
//		arrayNode.add(readFile("src/test/resources/json/mms_rssi_reset.json"));
//		arrayNode.add(readFile("src/test/resources/json/zcms_campaign_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/zms_zone_put.json"));
		arrayNode.add(readFile("src/test/resources/json/zms_zone_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/zms_zone_get2.json"));
//		arrayNode.add(readFile("src/test/resources/json/bms_beacon_put.json"));
//		arrayNode.add(readFile("src/test/resources/json/bms_beacon_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/mms_map_datas_put.json"));
//		arrayNode.add(readFile("src/test/resources/json/devms_location_agree_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/devms_location_agree_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/admin_gateway_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/admin_gateway.json"));
//		arrayNode.add(readFile("src/test/resources/json/cpgn_event_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/cpgn_event_post.json"));
	}

	@Test
	public void test01() throws Exception {
		for (JsonNode jsonNode : arrayNode) {
			test(jsonNode);
		}
	}

	public static void main(String[] args) throws Exception {
		String str = null;

		str = StringUtils.replace(str, " ", "");
//		if (!str.startsWith(",")) {
//			str = "," + str;
//		}
//		if (!str.endsWith(",")) {
//			str = str + ",";
//		}

		System.out.println(str);
	}
}
