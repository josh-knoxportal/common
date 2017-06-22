package com.nemustech.platform.lbs.wwms.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.JsonNode;
import com.nemustech.common.model.Common;
import com.nemustech.common.model.Response;
import com.nemustech.common.service.CommonService;
import com.nemustech.common.util.JsonUtil2;
import com.nemustech.platform.lbs.common.api.AbstractController;
import com.nemustech.platform.lbs.common.controller.DefaultController;
import com.nemustech.platform.lbs.common.vo.ServerConfig;
import com.nemustech.platform.lbs.common.vo.SettingVo;
import com.nemustech.platform.lbs.common.vo.SimpleResponse2;
import com.nemustech.platform.lbs.wwms.service.ServerConfigService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller
@RequestMapping(value = DefaultController.REQUESTMAPPING_DANGER_V1_ROOT
		+ "/server_config", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8;")
public class ServerConfigApiController extends AbstractController<ServerConfig> {
	@Autowired
	protected ServerConfigService service;

	@Override
	public CommonService<ServerConfig> getService() {
		return service;
	}

	@ApiOperation(value = "셋팅 정보 조회", notes = "셋팅 정보를 조회한다.", response = SimpleResponse2.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "setting_list.do", method = { RequestMethod.GET })
	public ResponseEntity<Response<List<SettingVo>>> setting_list(
			@ApiParam(value = "서버 설정", required = false) ServerConfig model, @Valid Common common, BindingResult errors)
			throws Exception {
		if (errors.hasFieldErrors()) {
			return (ResponseEntity) checkValidate(errors);
		}

		ServerConfig serverConfig = service.get(model);
		List<SettingVo> list = ServerConfigService.getSetting(serverConfig.getDanger_config());

		Response<List<SettingVo>> response = getSuccessResponse(list);

		return new ResponseEntity<Response<List<SettingVo>>>(response, HttpStatus.OK);
	}

	@Override
	@RequestMapping(value = "updates.do", method = RequestMethod.PUT)
	public ResponseEntity<Response<Integer>> updates(@RequestBody List<ServerConfig> models, BindingResult errors)
			throws Exception {
		if (models.size() > 0) {
			ServerConfig model = models.get(0);
			JsonNode jsonNode = JsonUtil2.readValue(model.getDanger_config());

			String log_level = JsonUtil2.getValue(jsonNode, "runtime.log-level.text_").asText();
			service.changeLogLevel(log_level);
		}

		return super.updates(models, errors);
	}
}
