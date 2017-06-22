package com.nemustech.platform.lbs.wwms.service;

import java.io.File;
import java.nio.charset.Charset;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.apache.commons.configuration.FileConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.nemustech.common.mapper.CommonMapper;
import com.nemustech.common.util.JsonUtil2;
import com.nemustech.common.util.Utils;
import com.nemustech.common.util.XMLJsonUtils2;
import com.nemustech.platform.lbs.common.mapper.ServerConfigMapper;
import com.nemustech.platform.lbs.common.service.CommonService;
import com.nemustech.platform.lbs.common.vo.ServerConfig;

@Service
public class ServerConfigService extends CommonService<ServerConfig> {
	@Autowired
	protected ServletContext context;

	@Autowired
	protected ServerConfigMapper mapper;

//	@Resource(name = "properties")
//	protected Properties properties;

	@Resource(name = "xmlConfiguration")
	protected FileConfiguration fileConfiguration;

	@Override
	public CommonMapper<ServerConfig> getMapper() {
		return mapper;
	}

	@PostConstruct
	public void init() throws Exception {
		String home_dir = System.getProperty("com.nemustech.home");
		if (!Utils.isValidate(home_dir)) {
			log.error(new Exception("java 옵션에 -Dcom.nemustech.home 이 존재하지 않습니다."));
			System.exit(1);
		}

		XMLJsonUtils2 xmlJsonUtils = new XMLJsonUtils2("smartplant-safety", "object", "element");
		String xml = IOUtils.toString(fileConfiguration.getURL(), Charset.defaultCharset());
		log.info("xml: " + xml);
		String json = xmlJsonUtils.convertXmlStringToJsonString(xml);
//		log.info("json: " + JsonUtil2.toStringPretty(json));
//		log.info("setting: " + JsonUtil2.toStringPretty(getSetting(json)));

//		log.info("properties: " + StringUtils.replace(properties.toString(), ", ", System.lineSeparator()));

		JsonNode jsonNode = JsonUtil2.readValue(json);
		validate(jsonNode);

		ServerConfig model = new ServerConfig();
		ServerConfig serverConfig = get(model);

		model.setConfig_uid(serverConfig.getConfig_uid());

		int ar_period = JsonUtil2.getValue(jsonNode, "buildtime.setting.ar-period.text_").asInt();
		model.setAlive_report(Integer.valueOf(ar_period));

		String gps_option = JsonUtil2.getValue(jsonNode, "buildtime.setting.gps-option.text_").asText();
		model.setGps_onoff(gps_option);

		model.setDanger_config(json);
		mapper.update(model);

		context.setAttribute("g_server_config", model);

		boolean access = JsonUtil2.getValue(jsonNode, "buildtime.setting.access.text_").asBoolean();
		context.setAttribute("g_access_config", access);

		String log_level = JsonUtil2.getValue(jsonNode, "runtime.log-level.text_").asText();
		changeLogLevel(log_level);
	}

	/**
	 * 유효성 체크
	 * 
	 * <pre>
	 * 1. ecgi와 beacon on/off
	 *	* ecgi off와 beacon off인 경우는 없어야 함
	 *
	 * 2. alive report 주기
	 * - 옵션 : 0초일 경우 report하지 않음
	 * - 최소 : 60초
	 *	* 이전에 60초로 했을때 배터리 이슈로 인해 문제가 생겨서 6600초로 변경했다는 이슈가 있음.
	 *	* 60초로 해도 되지만 추천하지는 않음
	 *	* 60초 이하로 내려갈 경우, 배터리 광탈 우려 있음
	 *	* 최대 : 6600초
	 *	* 작업자의 in/out체크 시간이 2시간으로 되어 있어서, 10분의 여유를 두고 1시간 50분으로 설정
	 *
	 * 3. gps-period 주기
	 * - 옵션 : 0초일 경우 사용하지 않음
	 * - 최소 : 1초
	 *	* 1초 이하로 내려가는건 일반적인 내비게이션의 목적에 어긋남
	 * - 최대 : 1초 ~ 10초
	 *	* 1초 ~ 3초를 추천.
	 *	* 3초 이상도 가능하지만, 차량은 gps 신호를 빨리 받아야 위치 표시가 가능하므로, 3초 이내를 추천함
	 *	* 최대값은 차량의 위치를 얼마나 자주 표시할 것인가와 배터리 이슈에 달려 있으므로, 명시적으로 결정할 수 없음.
	 * </pre>
	 * 
	 * @param jsonNode Danger config
	 * @throws Exception
	 */
	public void validate(JsonNode jsonNode) throws Exception {
		boolean ecgi = JsonUtil2.getValue(jsonNode, "buildtime.setting.ecgi.text_").asBoolean();
		boolean beacon = JsonUtil2.getValue(jsonNode, "buildtime.setting.beacon.text_").asBoolean();
		if (ecgi == false && beacon == false) {
			log.error(new Exception(
					"XML설정 파일의 플랜트 진입/진출 감지 ECGI(buildtime.setting.ecgi), 비콘(buildtime.setting.beacon) 값은 필수값이며, 모두 false 일수 없습니다."));
			System.exit(1);
		}

		int ar_period = JsonUtil2.getValue(jsonNode, "buildtime.setting.ar-period.text_").asInt();
		if (!(60 <= ar_period && ar_period <= 6600)) {
			log.error(new Exception(
					"XML설정 파일의 Alive Report(buildtime.setting.ar-period) 값은 필수값이며, 60 ~ 6600초(1시간 50분) 사이의 값이어야 합니다."));
			System.exit(1);
		}
	}

	/**
	 * 로그 레벨 변경
	 * 
	 * @param log_level
	 * @throws Exception
	 */
	public void changeLogLevel(String log_level) throws Exception {
		String target_dir = System.getProperty("com.nemustech.home") + "/config/danger/";
		FileUtils.copyFile(new File(target_dir + "logback_" + log_level + ".xml"), new File(target_dir + "logback.xml"),
				false);
	}
}
