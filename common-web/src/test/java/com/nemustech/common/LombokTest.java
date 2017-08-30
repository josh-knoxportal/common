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
public class LombokTest extends Test05 {
	@NonNull
	protected String s;

	public LombokTest(int i, String s) {
		this.i = i;
		this.s = s;
	}

	public static void main(String[] args) {
		LombokTest test04 = new LombokTest(1, "a");
		LombokTest test04_ = new LombokTest(2, "a");
		log.info(test04);
		log.info(test04_);
		log.info(test04.equals(test04_));

		test04 = new LombokTest(null);
		test04.setS(null);

	}
}
