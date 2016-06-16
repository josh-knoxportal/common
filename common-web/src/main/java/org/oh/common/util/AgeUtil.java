package org.oh.common.util;

import egovframework.rte.fdl.string.EgovDateUtil;

/**
 * 나이 관련 처리하는 utility.<br/>
 * - egovframework.rte.fdl.string.EgovDateUtil 클래스를 상속받음(전자정부프레임워크).
 * 
 * @version 2.5
 *
 */
public abstract class AgeUtil extends EgovDateUtil {
	/**
	 * 주민번호를 기준으로 현재 시점의 만 나이를 구한다.
	 * 
	 * <pre>
	 * 예)
	 *      String socialNo = "0205113111111";    //주민번호
	 *      <br>
	 *      AgeUtil.getCurrentFullAge(socialNo)   //현재 날짜 기준으로 만나이 리턴
	 * </pre>
	 * 
	 * @param socialNo "-"가 없는 주민등록번호
	 * @return 만 나이
	 */
	public static int getCurrentFullAge(String socialNo) {
		return EgovDateUtil.getCurrentFullAge(socialNo);
	}

	/**
	 * 기준 일자에서의 만 나이를 구한다.
	 * 
	 * <pre>
	 * 예)
	 *      String socialNo = "0205113111111";    //주민번호
	 *      String keyDate  = "20120505"          //기준날짜  
	 *      <br>
	 *      AgeUtil.getFullAge(socialNo,keyDate)   //입력된 날짜 기준으로 만나이 리턴
	 * </pre>
	 * 
	 * @param socialNo "-"가 없는 주민등록번호
	 * @param keyDate 기준일자 (yyyymmdd 포맷)
	 * @return 만 나이
	 */
	public static int getFullAge(String socialNo, String keyDate) {
		return EgovDateUtil.getFullAge(socialNo, keyDate);
	}
}
