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
		arrayNode.add(readFile("src/test/resources/json/sample_list_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/sample_list2_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/sample_list_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/sample_list2_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/sample_page_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/sample_page2_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/sample_update_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/sample_update2_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/sample_test_list_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/sample_and_test_list_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/sample_and_test2_list_get.json"));

		///////////////////////////////////////////////////////////////////////
//		arrayNode.add(readFile("src/test/resources/json/aams_token_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/aams_group_user_list_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/aams_group_user_list_get2.json"));

//		arrayNode.add(readFile("src/test/resources/json/admin_gateway_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/admin_gateway.json"));
//		arrayNode.add(readFile("src/test/resources/json/admin_login_post.json"));

//		arrayNode.add(readFile("src/test/resources/json/bms_beacon_put.json"));
//		arrayNode.add(readFile("src/test/resources/json/bms_beacon_get.json"));

//		arrayNode.add(readFile("src/test/resources/json/cbms_branch_get.json"));

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
//		arrayNode.add(readFile("src/test/resources/json/zcms_campaign_put.json"));
//		arrayNode.add(readFile("src/test/resources/json/zcms_campaign_post.json"));

//		arrayNode.add(readFile("src/test/resources/json/zms_geozone_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/zms_geozone_around_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/zms_geozone_id_around_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/zms_zone_put.json"));
//		arrayNode.add(readFile("src/test/resources/json/zms_reset_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/zms_zone_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/zms_zone_get2.json"));
//		arrayNode.add(readFile("src/test/resources/json/zms_zone_get3.json"));
//		arrayNode.add(readFile("src/test/resources/json/zms_zone_get4.json"));
//		arrayNode.add(readFile("src/test/resources/json/zms_zone_get5.json"));
//		arrayNode.add(readFile("src/test/resources/json/zms_search_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/zms_search_get2.json"));
//		arrayNode.add(readFile("src/test/resources/json/zms_search_get3.json"));
//		arrayNode.add(readFile("src/test/resources/json/zms_search2_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/zms_search2_get2.json"));
//		arrayNode.add(readFile("src/test/resources/json/zms_search2_get3.json"));
//		arrayNode.add(readFile("src/test/resources/json/zms_search2_get4.json"));
//		arrayNode.add(readFile("src/test/resources/json/zms_searchall_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/zms_searchall_get2.json"));
//		arrayNode.add(readFile("src/test/resources/json/zms_group_user_list_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/zms_category_list_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/zms_category_insert_list_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/zms_zone_category_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/zms_zone_category_get.json"));
	}

	@Test
	public void test01() throws Exception {
		test(arrayNode);
	}
}
