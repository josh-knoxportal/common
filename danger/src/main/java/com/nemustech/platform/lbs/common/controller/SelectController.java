package com.nemustech.platform.lbs.common.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.android.gcm.server.Result;
import com.nemustech.platform.lbs.common.service.GcmService;
import com.nemustech.platform.lbs.common.service.SelectApiService;

@Controller
public class SelectController {

	@Autowired
	public SelectApiService selectApiService;

	@Autowired
	private GcmService gcmService;

	@RequestMapping(value = "test/{system}/{admin}/{view}.do", method = RequestMethod.GET)
	public String test(HttpServletRequest request, Model model, @PathVariable String system, @PathVariable String admin,
			@PathVariable String view) {

		return system + "/" + admin + "/test/" + view;
	}

	@RequestMapping(value = "/json_result.do", method = RequestMethod.GET)
	public String jsonResult(HttpServletRequest request, Model model) {
		model.addAttribute("code", request.getAttribute("cd"));
		System.out.println("j===========================");
		System.out.println("json_result, code:" + request.getAttribute("cd"));
		return "jsonresult";
	}

	@RequestMapping(value = "/gcm.do", method = RequestMethod.GET)
	public String sendGcm(HttpServletRequest request, Model model,
			@PathVariable @RequestParam(value = "reg_id", required = false, defaultValue = "fQQDAFBi5k8:APA91bERpt9bQSUKWy4Bj5BsFDiJ-vVRF_ee8LeFbYWVMZi0Vqo_azn2ccSs-fEUqa3QKHUuCSo_M0pCfXyJ0p4fVOifFAc8OoFP2SY9VEZczeOxpqmglvx8dNmzhLDMq3QLTUE4kqX2") String reg_id,
			@RequestParam(value = "msg", required = false, defaultValue = "test") String msg) {

		List<Result> list = gcmService.pushToGcm(reg_id, msg);

		for (Result r : list) {
			System.out.println(r.getMessageId());
			System.out.println(r.getCanonicalRegistrationId());
			System.out.println(r.getErrorCodeName());
		}

		return "test";
	}

	@RequestMapping(value = "/query.do", method = RequestMethod.GET)
	public String query(HttpServletRequest request, Model model) {
		Map<String, String> queryMap = new HashMap<String, String>();
		String query = "select * from tbvc_account";

		queryMap.put("query", query);

		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		try {
			result = selectApiService.getSelectAnything(queryMap);
		} catch (Exception e) {
			e.printStackTrace();
		}

		model.addAttribute("r_msg", result);

		return "test/htmlResult";
	}

	@RequestMapping(value = "/12345.do", method = RequestMethod.GET)
	public String query1(HttpServletRequest request, Model model) {

		return "12345";
	}

	@RequestMapping(value = "/12345.do", method = RequestMethod.POST)
	public String query2(HttpServletRequest request, Model model) {

		String query = request.getParameter("query");
		Map<String, String> queryMap = new HashMap<String, String>();
		String html = "";

		queryMap.put("query", query);

		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		Map<String, String> temp_th = new HashMap<String, String>();
		Map<String, String> temp_td = null;

		if (query.contains("select") || query.contains("SELECT")) {
			System.out.println("Request select :::::::::" + query);

			try {
				result = selectApiService.getSelectAnything(queryMap);

				System.out.println("Resulted :::::::::" + result);

				html += "<table class='table table-bordered'>";

				temp_th = result.get(0);
				html += "<tr>";
				Iterator<String> keys_th = temp_th.keySet().iterator();
				while (keys_th.hasNext()) {
					String key = keys_th.next();
					html += "<th>" + key + "</th>";
				}
				html += "</tr>";

				for (int i = 0; i < result.size(); i++) {
					html += "<tr>";
					temp_td = new HashMap<String, String>();
					temp_td = result.get(i);
					Iterator<String> keys_td = temp_td.keySet().iterator();

					while (keys_td.hasNext()) {
						String key = keys_td.next();
						String valueName = String.valueOf(temp_td.get(key));
						html += "<td>" + valueName + "</td>";
					}
					html += "</tr>";
				}

				html += "</table>";

				System.out.println(html);

				model.addAttribute("html", html);
			} catch (Exception e) {
				e.printStackTrace();
				return "12345";
			}

			return "12345";

		} else if (query.contains("insert")) {
			System.out.println("Request insert :::::::::" + query);

			try {
				selectApiService.getInsertAnything(queryMap);

				System.out.println("Resulted :::::::::" + result);

				model.addAttribute("html", "insert 성공했습니다");
			} catch (Exception e) {
				e.printStackTrace();
				model.addAttribute("html", "insert 실패했습니다");
				return "12345";

			}

			return "12345";
		} else if (query.contains("update")) {
			System.out.println("Request update :::::::::" + query);

			try {
				int value = selectApiService.getUpdateAnything(queryMap);

				System.out.println("value :::::::::" + value);

				if (value > 0) {
					model.addAttribute("html", "update 성공했습니다");
				} else {
					model.addAttribute("html", "update 항목이 없습니다");
				}

			} catch (Exception e) {
				e.printStackTrace();
				model.addAttribute("html", "update 실패했습니다");
				return "12345";

			}

			return "12345";
		} else if (query.contains("delete")) {
			System.out.println("Request delete :::::::::" + query);

			try {
				int value = selectApiService.getDeleteAnything(queryMap);

				System.out.println("value :::::::::" + value);

				if (value > 0) {
					model.addAttribute("html", "delete 성공했습니다");
				} else {
					model.addAttribute("html", "delete 항목이 없습니다");
				}
			} catch (Exception e) {
				e.printStackTrace();
				model.addAttribute("html", "delete 실패했습니다");
				return "12345";

			}
			return "12345";
		} else {
			System.out.println("Request else  :::::::::" + query);
			model.addAttribute("html", "실행된 결과가 없습니다");
			return "12345";
		}
	}

	/**
	 * 행정존 DB 입출력 관련
	 */
	@RequestMapping(value = "sigungu.do", method = RequestMethod.GET)
	public ResponseEntity<List<Map<String, String>>> sigungu(HttpServletRequest request, Model model) throws Throwable {

		String query = request.getParameter("query");

		System.out.println("GET !!!!!!!");
		System.out.println("query = " + query);

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> vo = new HashMap<String, String>();
		vo.put("query", query);

		try {
			list = selectApiService.getSigunguList(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<List<Map<String, String>>>(list, HttpStatus.OK);

	}

	@RequestMapping(value = "districtZoneInsert.do", method = RequestMethod.GET)
	public String districtZoneInsert(HttpServletRequest request, Model model) {

		return "/zone/districtZoneInsert";
	}

	@RequestMapping(value = "districtZoneInsertPost.do", method = RequestMethod.POST)
	public ResponseEntity<List<Map<String, Object>>> districtZoneInsertPost(HttpServletRequest request, Model model,
			@RequestParam(value = "sigungu", required = false) String sigungu) throws Throwable {

		String sigunguCd = sigungu;
		String sidoCd = sigungu.substring(0, 2);

		// StringBuilder urlBuilder = new
		// StringBuilder("http://shareapi.ngii.go.kr/openapi/service/rest/CodeInfoService/getAdministzoneInfo?ServiceKey=ahy2jeesQPj%2FU58va0GKMSMp9sK6LpX9sPhgW%2BJJyXD33sQr2s0xcPe7Az3HT1MH4XOo63DywC6RBVR8O1LUgQ%3D%3D&sidoCd="+sidoCd+"&sigunguCd="+sigunguCd);
		StringBuilder urlBuilder = new StringBuilder(
				"http://shareapi.ngii.go.kr/openapi/service/rest/CodeInfoService/getAdministzoneInfo?ServiceKey=UVDN%2BW03rQJnF8yM%2ByCkWR6ELuoR9bNzbyFkN1%2FMJF%2B6z%2FR95oEUEcQesmTiCrvZPMNqaDGvp8Dc%2FcpQnLFH0Q%3D%3D&sidoCd="
						+ sidoCd + "&sigunguCd=" + sigunguCd);

		URL url = new URL(urlBuilder.toString());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-type", "application/json");
		System.out.println("Response code: " + conn.getResponseCode());
		BufferedReader rd;
		if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		} else {
			rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		}
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		rd.close();
		conn.disconnect();

		String xml = sb.toString();
		JSONObject distirct_zones = XML.toJSONObject(xml);

		System.out.println(distirct_zones);

		JSONObject t1 = distirct_zones.getJSONObject("response");
		JSONObject t2 = t1.getJSONObject("body");
		JSONObject t3 = t2.getJSONObject("items");
		JSONArray a1 = t3.getJSONArray("item");

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < a1.length(); i++) {
			JSONObject t4 = a1.getJSONObject(i);

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("name", t4.get("name").toString());
			data.put("code", t4.get("code"));

			list.add(data);
		}

		return new ResponseEntity<List<Map<String, Object>>>(list, HttpStatus.OK);

	}

}
