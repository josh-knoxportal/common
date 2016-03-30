package org.oh.adapter.soap;

import javax.xml.soap.SOAPMessage;

import org.apache.log4j.PropertyConfigurator;
import org.oh.adapter.exception.AdapterException;
import org.oh.common.util.JsonUtil2;
import org.oh.common.util.PropertyUtils;
import org.oh.common.util.SoapUtils;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * AbstractSOAPMessageMapper 클래스 확장
 */
public class SoapJsonMapper extends AbstractSOAPMessageMapper<JsonNode, JsonNode> {
	public SoapJsonMapper(String trcode) {
		super(trcode);
	}

	public SoapJsonMapper(String trcode, String charset) {
		super(trcode, charset);
	}

	@Override
	public SOAPMessage mappingRequestParamToSOAPMessage(JsonNode params) throws AdapterException {
		return mappingRequestParamToSOAPMessage(PropertyUtils.getInstance().getString("GOODSEN_GW_ACTION_URL"),
				PropertyUtils.getInstance().getString("GOODSEN_GW_REQUEST_NAME"), params.toString());
	}

	@Override
	public JsonNode mappingSOAPMessageToResponse(SOAPMessage soapMessage) throws AdapterException {
		return JsonUtil2.readValue(mappingSOAPMessageToResponse2(soapMessage));
	}

	public static void main(String[] args) throws Exception {
		System.setProperty("HOME", "C:/dev/workspace/workspace_common/HOME");
		PropertyConfigurator.configure(System.getProperty("HOME") + "/conf/server/log4j.properties");

		SoapJsonMapper mapper = new SoapJsonMapper("DWG000");
		SOAPMessage soapMessage = mapper.mappingRequestParamToSOAPMessage(JsonUtil2.readValue(
				"{\"header\":{\"interface_id\":\"\",\"result\":\"\",\"error_code\":\"\",\"error_text\":\"\",\"user_id\":\"D4000737\",\"gmt\":\"9\",\"lang\":\"ko\"}, \"body\":{} }"));
		mapper.mappingSOAPMessageToResponse(
				SoapUtils.sendMessage(soapMessage, "https://m.dongwha-mh.com/SmartServiceApp/OrgService.asmx"));
	}
}