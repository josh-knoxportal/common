package com.nemustech.common;

import com.nemustech.common.model.Test05;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.apachecommons.CommonsLog;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data

@CommonsLog
public class Test04 extends Test05 {
	@NonNull
	protected String s;

	public Test04(int i, String s) {
		this.i = i;
		this.s = s;
	}

	public static void main(String[] args) {
		Test04 test04 = new Test04(1, "a");
		Test04 test04_ = new Test04(2, "a");
		log.info(test04);
		log.info(test04_);
		log.info(test04.equals(test04_));

		test04 = new Test04(null);
		test04.setS(null);

	}
}
