package com.nemustech.platform.lbs.common.util;

public class Const {
	public final static int NEMUS_REAL_FLAG = 1;
	public final static int SHA_512 = 512;

	public static String returnMessage(int messageCode) {
		String returnMessage = "SUCCESS";

		if (messageCode == -999) {
			returnMessage = "이미 등록된 차량입니다.";
		} else if (messageCode == -888) {
			returnMessage = "이미 운행중인 단말기 입니다.";
		} else if (messageCode == -998) {
			returnMessage = "차량번호를 입력하세요";
		} else if (messageCode == -100) {
			returnMessage = "이용차단된 단말기 입니다.";
		}

		return returnMessage;
	}
}
