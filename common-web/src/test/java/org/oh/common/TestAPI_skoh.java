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
//		arrayNode.add(readFile("src/test/resources/json/com/gateway_put.json"));
//		arrayNode.add(readFile("src/test/resources/json/com/gateway_get.json"));

		arrayNode.add(readFile("src/test/resources/json/com/sample_list_get.json"));
		arrayNode.add(readFile("src/test/resources/json/com/sample_list3_post.json"));
		arrayNode.add(readFile("src/test/resources/json/com/sample_list2_get.json"));
		arrayNode.add(readFile("src/test/resources/json/com/sample_list4_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/com/sample_page_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/com/sample_page2_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/com/sample_update_put.json"));
//		arrayNode.add(readFile("src/test/resources/json/com/sample_update2_put.json"));
//		arrayNode.add(readFile("src/test/resources/json/com/sample_update_json_put.json"));
//		arrayNode.add(readFile("src/test/resources/json/com/sample_insert_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/com/sample_insert_json_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/com/sample_insert_list_json_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/com/sample_test_list_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/com/sample_and_test_list_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/com/sample_and_test2_list_get.json"));

//		arrayNode.add(readFile("src/test/resources/json/com/test_list_get.json"));

		///////////////////////////////////////////////////////////////////////

//		arrayNode.add(readFile("src/test/resources/json/ssg/aams/token_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/aams/group_user_list_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/aams/group_user_list_get2.json"));

//		arrayNode.add(readFile("src/test/resources/json/ssg/admin/gateway_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/admin/gateway.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/admin/login_post.json"));

//		arrayNode.add(readFile("src/test/resources/json/ssg/bms/beacon_put.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/bms/beacon_get.json"));

//		arrayNode.add(readFile("src/test/resources/json/ssg/cbms/branch_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/cbms/company_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/cbms/company_get2.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/cbms/company_put.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/cbms/company_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/cbms/company_get2.json"));

//		arrayNode.add(readFile("src/test/resources/json/ssg/cpgn/event_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/cpgn/event_post.json"));

//		arrayNode.add(readFile("src/test/resources/json/ssg/devms/location_agree_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/devms/location_agree_post.json"));

//		arrayNode.add(readFile("src/test/resources/json/ssg/mms/rssi_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/mms/rssi_get2.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/mms/rssi_put.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/mms/rssi_reset.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/mms/rssi_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/mms/rssi_get2.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/mms/map_datas_put.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/mms/map_reset.json"));

//		arrayNode.add(readFile("src/test/resources/json/ssg/zcms/campaign_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zcms/campaign_get2.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zcms/campaign_get3.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zcms/campaign_put.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zcms/campaign_post.json"));

//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/geozone_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/geozone_around_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/geozone_id_around_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/zone_put.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/reset_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/zone_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/zone_get2.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/zone_get3.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/zone_get4.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/zone_get5.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/zone2_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/search_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/search_get2.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/search_get3.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/search2_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/search2_get2.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/search2_get3.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/search2_get4.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/searchall_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/searchall_get2.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/category_list_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/category_list_get2.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/category_list_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/category_delete_delete.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/category_insert_list_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/zone_category_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/zone_category_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/zone_category_post2.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/zone_category_post3.json"));
	}

	@Test
	public void test01() throws Exception {
		test(arrayNode);
	}
}
