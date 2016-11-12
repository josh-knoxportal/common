package com.nemustech.common.util;

import java.util.HashMap;

import org.apache.commons.collections.keyvalue.MultiKey;

/**
 * 상태를 체크관리하는 클래스.
 */
public class StatusChecker {
	/** 상태 유효기간. (단위: 초, 기본값 : 180초) */
	private long validTerm = 180;

	/** 상태 최종체크시간. (yyyyMMddHHmmss) */
	private String lastCheckDateTime;

	/** 상태 최종체크시간.(배치스케줄용) (yyyyMMddHHmmss) */
	private String lastBatchCheckDateTime;

	/** 상태 맵. <상태키,상태값> */
	private HashMap<MultiKey, Object> statusMap = new HashMap<MultiKey, Object>();

	/**
	 * 상태 유효기간 getter.
	 *
	 * @return 상태 유효기간. (단위: 초).
	 */
	public long getValidTerm() {
		return this.validTerm;
	}

	/**
	 * 상태 유효기간 setter.
	 *
	 * @param validTerm 상태 유효기간.
	 */
	public void setValidTerm(long validTerm) {
		this.validTerm = validTerm;
	}

	/**
	 * 상태 최종체크시간 getter.
	 *
	 * @return 상태 최종체크시간.
	 */
	synchronized public String getLastCheckDateTime() {
		return this.lastCheckDateTime;
	}

	/**
	 * 상태 최종체크시간 setter.
	 *
	 * @param lastBuildDateTime 상태 최종체크시간.
	 */
	synchronized public void setLastCheckDateTime(String lastBuildDateTime) {
		this.lastCheckDateTime = lastBuildDateTime;
	}

	/**
	 * 상태 최종체크시간(batch스케줄용)
	 *
	 * @return
	 */
	synchronized public String getLastBatchCheckDateTime() {
		return this.lastBatchCheckDateTime;
	}

	/**
	 * 상태 최종체크시간(batch스케줄용)
	 *
	 * @param lastBatchCheckDateTime
	 */
	synchronized public void setLastBatchCheckDateTime(String lastBatchCheckDateTime) {
		this.lastBatchCheckDateTime = lastBatchCheckDateTime;
	}

	/**
	 * 상태 유효기간 만료여부. <br>
	 *
	 * @return 만료시 true, 유효시 false.
	 */
	synchronized public boolean isExpireValidTerm() {
		if (this.lastCheckDateTime == null) {
			return true;
		}

		try {
			long lCurTerm = DateUtil.differenceSeconds(this.lastCheckDateTime, DateUtil.getCurrentDateTime(),
					DateUtil.PATTERN_yyyyMMddHHmmss);
			return lCurTerm > this.validTerm;
		} catch (Exception e) {
		}

		return true;
	}

	/**
	 * 상태 유효기간 만료여부. <br>
	 * Batch 스케줄 용
	 *
	 * @return 만료시 true, 유효시 false.
	 */
	synchronized public boolean isExpireValidTermBatch() {
		if (this.lastBatchCheckDateTime == null) {
			return true;
		}

		try {
			long lCurTerm = DateUtil.differenceSeconds(this.lastBatchCheckDateTime, DateUtil.getCurrentDateTime(),
					DateUtil.PATTERN_yyyyMMddHHmmss);
			return lCurTerm > this.validTerm;
		} catch (Exception e) {
		}

		return true;
	}

	/**
	 * 상태값 getter.
	 *
	 * @param key1 키1.
	 * @return 상태값.
	 */
	public Object getStatusValue(String key1) {
		return this.statusMap.get(new MultiKey(key1, " "));
	}

	/**
	 * 상태값 getter.
	 *
	 * @param key1 키1.
	 * @param key2 키2.
	 * @return 상태값.
	 */
	public Object getStatusValue(String key1, String key2) {
		return this.statusMap.get(new MultiKey(key1, key2));
	}

	/**
	 * 상태값 getter.
	 *
	 * @param key1 키1.
	 * @param key2 키2.
	 * @param key3 키3.
	 * @return 상태값.
	 */
	public Object getStatusValue(String key1, String key2, String key3) {
		return this.statusMap.get(new MultiKey(key1, key2, key3));
	}

	/**
	 * 상태값 getter.
	 *
	 * @param key1 키1.
	 * @param key2 키2.
	 * @param key3 키3.
	 * @param key4 키4.
	 * @return 상태값.
	 */
	public Object getStatusValue(String key1, String key2, String key3, String key4) {
		return this.statusMap.get(new MultiKey(key1, key2, key3, key4));
	}

	/**
	 * 상태값 getter.
	 *
	 * @param key1 키1.
	 * @param key2 키2.
	 * @param key3 키3.
	 * @param key4 키4.
	 * @param key5 키5.
	 * @return 상태값.
	 */
	public Object getStatusValue(String key1, String key2, String key3, String key4, String key5) {
		return this.statusMap.get(new MultiKey(key1, key2, key3, key4, key5));
	}

	/**
	 * 상태값 putter.
	 *
	 * @param key1 키1.
	 * @param statusValue 상태값.
	 */
	public void putStatusValue(String key1, Object statusValue) {
		this.statusMap.put(new MultiKey(key1, " "), statusValue);
	}

	/**
	 * 상태값 putter.
	 *
	 * @param key1 키1.
	 * @param key2 키2.
	 * @param statusValue 상태값.
	 */
	public void putStatusValue(String key1, String key2, Object statusValue) {
		this.statusMap.put(new MultiKey(key1, key2), statusValue);
	}

	/**
	 * 상태값 putter.
	 *
	 * @param key1 키1.
	 * @param key2 키2.
	 * @param key3 키3.
	 * @param statusValue 상태값.
	 */
	public void putStatusValue(String key1, String key2, String key3, Object statusValue) {
		this.statusMap.put(new MultiKey(key1, key2, key3), statusValue);
	}

	/**
	 * 상태값 putter.
	 *
	 * @param key1 키1.
	 * @param key2 키2.
	 * @param key3 키3.
	 * @param key4 키4.
	 * @param statusValue 상태값.
	 */
	public void putStatusValue(String key1, String key2, String key3, String key4, Object statusValue) {
		this.statusMap.put(new MultiKey(key1, key2, key3, key4), statusValue);
	}

	/**
	 * 상태값 putter.
	 *
	 * @param key1 키1.
	 * @param key2 키2.
	 * @param key3 키3.
	 * @param key4 키4.
	 * @param key5 키5.
	 * @param statusValue 상태값.
	 */
	public void putStatusValue(String key1, String key2, String key3, String key4, String key5, Object statusValue) {
		this.statusMap.put(new MultiKey(key1, key2, key3, key4, key5), statusValue);
	}
}
