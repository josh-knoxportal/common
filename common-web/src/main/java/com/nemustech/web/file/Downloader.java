package com.nemustech.web.file;

import java.util.Map;

/**
 * Download 서비스를 위한 interface.
 * 서버의 download 프로토콜 및 개발 가이드라인의 구현 방법을 확인한다.
 * 
 * @version 1.0.0
 */
public interface Downloader {
	/**
	 * URI형태로 요청한 정보를 다운로드한다.
	 * 
	 * @param target URI에서 다운로드 target으로 지정한 정보
	 * @param uid 다운로드 할 대상의 UID 또는 direct download 경로 등.
	 * @param params 다운로드를 위한 추가적인 정보
	 * @throws Exception
	 */
	public void download(String target, String uid, Map<String, Object> params) throws Exception;
}
