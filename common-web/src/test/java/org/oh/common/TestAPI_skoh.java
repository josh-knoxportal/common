package org.oh.common;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.oh.common.test.TestAPI;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestAPI_skoh extends TestAPI {
	@BeforeClass
	public static void initClass() throws Exception {
//		arrayNode.add(readFile("src/test/resources/json/com_gateway_put.json"));
//		arrayNode.add(readFile("src/test/resources/json/com_gateway_get.json"));

//		arrayNode.add(readFile("src/test/resources/json/test.json"));
//		arrayNode.add(readFile("src/test/resources/json/test_list_get.json"));

		arrayNode.add(readFile("src/test/resources/json/sample_list_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/sample_list2_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/sample_list_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/sample_list2_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/sample_page_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/sample_page2_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/sample_update_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/sample_update2_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/sample_test_list_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/sample&test_list_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/sample&test2_list_get.json"));

		///////////////////////////////////////////////////////////////////////

//		arrayNode.add(readFile("src/test/resources/json/bms_beacon_put.json"));
//		arrayNode.add(readFile("src/test/resources/json/bms_beacon_get.json"));

//		arrayNode.add(readFile("src/test/resources/json/cbms_company_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/cbms_company_get2.json"));
//		arrayNode.add(readFile("src/test/resources/json/cbms_company_put.json"));
//		arrayNode.add(readFile("src/test/resources/json/cbms_company_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/cbms_company_get2.json"));

//		arrayNode.add(readFile("src/test/resources/json/cpgn_event_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/cpgn_event_post.json"));

//		arrayNode.add(readFile("src/test/resources/json/devms_location_agree_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/devms_location_agree_post.json"));

//		arrayNode.add(readFile("src/test/resources/json/mms_rssi_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/mms_rssi_get2.json"));
//		arrayNode.add(readFile("src/test/resources/json/mms_rssi_put.json"));
//		arrayNode.add(readFile("src/test/resources/json/mms_rssi_reset.json"));
//		arrayNode.add(readFile("src/test/resources/json/mms_rssi_get.json"));Preparing
//		arrayNode.add(readFile("src/test/resources/json/mms_rssi_get2.json"));
//		arrayNode.add(readFile("src/test/resources/json/mms_map_datas_put.json"));

//		arrayNode.add(readFile("src/test/resources/json/zcms_campaign_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/zcms_campaign_get2.json"));

//		arrayNode.add(readFile("src/test/resources/json/zms_zone_put.json"));
//		arrayNode.add(readFile("src/test/resources/json/zms_zone_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/zms_zone_get2.json"));
//		arrayNode.add(readFile("src/test/resources/json/zms_reset_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/zms_zone_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/zms_zone_get2.json"));
//		arrayNode.add(readFile("src/test/resources/json/zms_zone_get3.json"));
//		arrayNode.add(readFile("src/test/resources/json/zms_zone_get4.json"));
//		arrayNode.add(readFile("src/test/resources/json/zms_zone_get5.json"));
//		arrayNode.add(readFile("src/test/resources/json/zms_search_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/zms_search_get2.json"));
//		arrayNode.add(readFile("src/test/resources/json/zms_search2_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/zms_search2_get2.json"));
//		arrayNode.add(readFile("src/test/resources/json/zms_search2_get3.json"));
//		arrayNode.add(readFile("src/test/resources/json/zms_search2_get4.json"));
//		arrayNode.add(readFile("src/test/resources/json/zms_searchall_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/zms_searchall_get2.json"));
//		arrayNode.add(readFile("src/test/resources/json/admin_gateway_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/admin_gateway.json"));
	}

	@Test
	public void test01() throws Exception {
		test(arrayNode);
	}

	public static void main(String[] args) throws Exception {
		String str = "1";

//		str = StringUtils.replace(str, " ", "");
//		if (!str.startsWith(",")) {
//			str = "," + str;
//		}
//		if (!str.endsWith(",")) {
//			str = str + ",";
//		}
//
//		System.out.println(str);

//		System.out.println(HttpStatus.INTERNAL_SERVER_ERROR);

		System.out.println(str + "2");
	}
}
