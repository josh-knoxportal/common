package org.oh.common;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.oh.common.test.TestAPI;
import org.oh.common.util.HTTPUtils;
import org.oh.common.util.JsonUtil2;
import org.oh.common.util.Utils;
import org.oh.web.common.Response;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestAPI_ssg extends TestAPI {
	public static final String[] DATE_FIELDS = { "REG_DATE", "UPD_DATE", "START_DATE", "END_DATE" };

	@Override
	protected void print(List<Future<Object>> futureList) throws Exception {
		for (Future<Object> future : futureList) {
			Map<String, Object> result = (Map) future.get();

			// 파일로 저장
			if (Utils.isValidate(saveDir) && Utils.isValidate(saveExt)) {
				HTTPUtils.generateFile(
						saveDir + "/" + Utils.formatCurrentDate(Utils.SDF_DATE_MILLI_TIME) + "." + saveExt,
						(byte[]) result.get("content"));
				// 콘솔에 출력
			} else {
				String content = HTTPUtils.getContentString(result);

				if (convertDate) {
					Response<List<Map<String, Object>>> response = JsonUtil2.readValue(content, Response.class);
					List<Map<String, Object>> list = response.getBody();
					for (Map<String, Object> map : list) {
						for (String field : DATE_FIELDS) {
							Long date = (Long) map.get(field);
							if (date != null)
								map.put(field, Utils.convertDateTimeToString(new Date(date)));
						}
					}
					response.setBody(list);
					content = JsonUtil2.toString(response);
				}

				if (Utils.isValidate(responseFormat)) {
					if ("JSON".equalsIgnoreCase(responseFormat)) {
						content = JsonUtil2.toStringPretty(content);
					}
				}
				System.out.println("content: " + content);
			}
		}
	}

	@BeforeClass
	public static void initClass() throws Exception {
//		arrayNode.add(readFile("src/test/resources/json/ssg/aams/group_user_list_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/aams/group_user_list_get2.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/aams/token_post.json"));

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
//		arrayNode.add(readFile("src/test/resources/json/ssg/cbms/reset_get.json"));

//		arrayNode.add(readFile("src/test/resources/json/ssg/cpgn/event_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/cpgn/event_post.json"));

//		arrayNode.add(readFile("src/test/resources/json/ssg/devms/location_agree_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/devms/location_agree_post.json"));

//		arrayNode.add(readFile("src/test/resources/json/ssg/lms/campaign_noti_post.json"));
		arrayNode.add(readFile("src/test/resources/json/ssg/lms/make_route_csv_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/lms/route_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/lms/route_post2.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/lms/verify_inserts_json_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/lms/verify_list_get.json"));

//		arrayNode.add(readFile("src/test/resources/json/ssg/mms/map_datas_put.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/mms/map_reset.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/mms/path_find_by_coords_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/mms/rssi_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/mms/rssi_get2.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/mms/rssi_put.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/mms/rssi_reset.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/mms/rssi_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/mms/rssi_get2.json"));

//		arrayNode.add(readFile("src/test/resources/json/ssg/zcms/campaign_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zcms/campaign_get2.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zcms/campaign_get3.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zcms/campaign_id_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zcms/campaign_put.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zcms/campaign_put2.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zcms/campaign_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zcms/campaign_post2.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zcms/campaign_post3.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zcms/campaign_from_geo_get.json"));

//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/category_list_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/category_list_team_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/category_list_team_get2.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/category_list_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/category_delete_delete.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/category_insert_list_post.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/category_select_get.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/category_select_get2.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/category_select_get3.json"));
//		arrayNode.add(readFile("src/test/resources/json/ssg/zms/category_select_get4.json"));
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
	}
}
