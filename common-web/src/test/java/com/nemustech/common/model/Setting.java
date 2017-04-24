package com.nemustech.common.model;

import org.mybatisorm.annotation.Table;

import com.nemustech.common.util.JsonUtil2;
import com.nemustech.common.util.XMLJsonUtils2;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@Table("tbda_setting")
public class Setting {
	protected Long setting_uid;
	protected String name;
	protected Integer type;
	protected String value;
	protected Integer is_use;
//	protected String creator_id;
//	protected String editor_id;
//	protected String reg_date;
//	protected String upd_date;

	public Setting() {
		setting_uid = 1L;
		name = "email";
		type = 1;
		value = "admin@lguplus.com";
		is_use = 0;
//		creator_id = "system";
//		editor_id = "system";
//		reg_date = "2017-04-01 13:01:01";
//		upd_date = "2017-04-01 13:01:01";
	}

	public static void main(String[] args) throws Exception {
		XMLJsonUtils2 xmlJsonUtils = new XMLJsonUtils2("openapi", "item");

		String json = JsonUtil2.toStringPretty(new Setting());
		System.out.println(json);
		String xml = xmlJsonUtils.convertJsonStringToXmlString(json);
		System.out.println(xml);
	}
}
