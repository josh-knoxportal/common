package com.nemustech.common;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import com.nemustech.common.model.Response;
import com.nemustech.common.test.TestAPI;
import com.nemustech.common.util.JsonUtil2;
import com.nemustech.common.util.Utils;
import com.fasterxml.jackson.core.type.TypeReference;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestAPI_ssg extends TestAPI {
	public static final String[] DATE_FIELDS = { "REG_DATE", "UPD_DATE", "START_DATE", "END_DATE", "LOG_TIME", "LOG_DATE" };

	@Override
	protected String convertbody(String body) throws Exception {
		Response<List<Map<String, Object>>> response = JsonUtil2.readValue(body,
				new TypeReference<Response<List<Map<String, Object>>>>() {
				});
		for (Map<String, Object> map : response.getBody()) {
			for (String field : DATE_FIELDS) {
				Long date = (Long) map.get(field);
				if (date != null)
					map.put(field, Utils.convertDateTimeToString(new Date(date)));
			}
		}
		response.setBody(response.getBody());

		return JsonUtil2.toString(response);
	}

	@BeforeClass
	public static void initClass() throws Exception {
		// AAMS
//		arrayNode.add(readFile("src/test/resources/json/ssg/aams/group_user_list_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/aams/group_user_list_get2.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/aams/token_post.json"));

		// Admin
//		arrayNode.add(readFile("src/test/resources/json/ssg/admin/check_session_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/admin/gateway_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/admin/gateway.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/admin/login_post.json"));

		// BMS
//		arrayNode.add(readFile("src/test/resources/json/ssg/bms/beacon_put.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/bms/beacon_get.json"));

		// CBMS
//		arrayNode.add(readFile("src/test/resources/json/ssg/cbms/branch_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/cbms/company_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/cbms/company_get2.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/cbms/company_put.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/cbms/company_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/cbms/company_get2.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/cbms/names_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/cbms/prev_branch_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/cbms/reset_get.json"));

		// CPGN
//		arrayNode.add(readFile("src/test/resources/json/ssg/cpgn/event_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/cpgn/event_post.json"));

		// DEVMS
//		arrayNode.add(readFile("src/test/resources/json/ssg/devms/policy_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/devms/app_policy_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/devms/location_agree_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/devms/location_agree_post.json"));

		// LMS
//		arrayNode.add(readFile("src/test/resources/json/ssg/lms/daily_visit_log_sum_list_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/lms/anal_result_flow_list_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/lms/anal_result_visit_list_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/lms/campaign_noti_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/lms/campaign_popup_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/lms/daily_device_visit_log_device_count_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/lms/getHourlyZoneVisitStats_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/lms/daily_zone_visit_log_group_list_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/lms/daily_zone_visit_log_dashboard_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/lms/hourly_zone_visit_log_group_list_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/lms/make_route_csv_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/lms/route_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/lms/route_post2.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/lms/route_post3.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/lms/verify_insert_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/lms/verify_inserts_json_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/lms/verify_updates_json_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/lms/verify_deletes_json_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/lms/verify_list_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/lms/schedule_log_result_select_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/lms/rount_log_count_select_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/lms/verify_select_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/lms/verify_select_post2.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/lms/verify_select_post21.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/lms/verify_select_post22.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/lms/verify_select_post23.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/lms/getDailyZoneVisitStats_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/lms/verify_select_post3.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/lms/verify_select_post4.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/lms/verify_select_post5.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/lms/verify_select_post51.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/lms/verify_select_post52.json"));

		// MMS
//		arrayNode.add(readFile("src/test/resources/json/ssg/mms/map_data_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/mms/map_data_put.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/mms/map_reset.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/mms/path_find_by_coords_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/mms/rssi_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/mms/rssi_get2.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/mms/rssi_put.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/mms/rssi_reset.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/mms/rssi_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/mms/rssi_get2.json"));

		// ZCMS
//		arrayNode.add(readFile("src/test/resources/json/ssg/zcms/campaign_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zcms/campaign_get2.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zcms/campaign_get3.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zcms/campaign_get4.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zcms/campaign_id_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zcms/campaign_put.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zcms/campaign_put2.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zcms/campaign_put3.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zcms/campaign_put4.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zcms/campaign_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zcms/campaign_post2.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zcms/campaign_post3.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zcms/campaign_post4.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zcms/campaign_delete.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zcms/campaign_from_geo_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zcms/company_campaign_get.json"));

		// ZMS
		arrayNode.add(readFile("src/test/resources/json/ssg/zms/category_list_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/category_list_team_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/category_list_team_get2.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/category_list_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/category_delete_delete.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/category_insert_list_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/category_select_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/category_select_campaign_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/geozone_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/geozone_around_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/geozone_id_around_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/search_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/search_get2.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/search_get3.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/search2_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/search2_get2.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/search2_get3.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/search2_get4.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/searchall_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/searchall_get2.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/reset_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/upload_image_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/zone_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/zone_get2.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/zone_get3.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/zone_get4.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/zone_get5.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/zone_id_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/zone_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/zone_put.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/zone2_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/zone_category_list_category_gcode_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/zone_category_list_category_gcode_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/zone_category_list_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/zone_category_list_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/zone_category_list_post2.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/zone_category_list_post3.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/zone_category_list_post4.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/zone_category_select_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/zone_coords_list_zone_get.json"));
	}
}
