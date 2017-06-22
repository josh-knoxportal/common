package com.nemustech.platform.lbs.common.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.nemustech.common.model.Default;
import com.nemustech.common.service.impl.CommonServiceImpl;
import com.nemustech.common.util.JsonUtil2;
import com.nemustech.common.util.Utils;
import com.nemustech.platform.lbs.common.vo.CommonVo;
import com.nemustech.platform.lbs.common.vo.SettingVo;

@Service
public class CommonService<T extends Default> extends CommonServiceImpl<T> {
//	@Override
//	protected T setDefaultRegisterDate(T model) {
//		if (model instanceof CommonVo) {
//			CommonVo common = (CommonVo) model;
//			if (common.getReg_date() == null) {
//				if (SOURCE_TYPE_MYSQL.equals(getSourceType())) {
//					common.setReg_date(DEFAULT_DATE_MYSQL);
//				} else if (SOURCE_TYPE_ORACLE.equals(getSourceType())) {
//					common.setReg_date(DEFAULT_DATE_ORACLE);
//				} else if (SOURCE_TYPE_SQLSERVER.equals(getSourceType())) {
//					common.setReg_date(DEFAULT_DATE_SQLSERVER);
//				}
//			}
//		}
//
//		return model;
//	}
//
//	@Override
//	protected T setDefaultModifyDate(T model) {
//		if (model instanceof CommonVo) {
//			CommonVo common = (CommonVo) model;
//			if (common.getUpd_date() == null) {
//				if (SOURCE_TYPE_MYSQL.equals(getSourceType())) {
//					common.setUpd_date(DEFAULT_DATE_MYSQL);
//				} else if (SOURCE_TYPE_ORACLE.equals(getSourceType())) {
//					common.setUpd_date(DEFAULT_DATE_ORACLE);
//				} else if (SOURCE_TYPE_SQLSERVER.equals(getSourceType())) {
//					common.setUpd_date(DEFAULT_DATE_SQLSERVER);
//				}
//			}
//		}
//
//		return model;
//	}

	public static List<SettingVo> getSetting(String json) {
		List<SettingVo> list = new ArrayList<>();
		if (!Utils.isValidate(json))
			return list;

		JsonNode jsonNode = JsonUtil2.readValue(json);
		String[] times = new String[] { "buildtime", "runtime" };
		for (String time : times) {
			JsonNode setting = jsonNode.get(time).get("setting");
			if (setting == null)
				continue;

			Iterator<Map.Entry<String, JsonNode>> fields = setting.fields();
			while (fields.hasNext()) {
				Map.Entry<String, JsonNode> field = fields.next();
				JsonNode value = field.getValue().get("text_");
				if (value == null)
					continue;

				list.add(new SettingVo(field.getKey(), value.asText(), time));
			}
		}

		return list;
	}
}
