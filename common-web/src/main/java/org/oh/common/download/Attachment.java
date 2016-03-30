package org.oh.common.download;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.oh.common.util.Utils;

/**
 * 첨부 파일에 대한 정보를 담고 있는 클래스
 * 
 * 
 * @version 1.0.0
 * 
 */
public class Attachment {
	/**
	 * 파일 이름
	 */
	private String name;

	/**
	 * 파일 크기
	 */
	private long size;

	/**
	 * 파일 data
	 */
	private byte[] rawData;

	/**
	 * 파일 경로
	 */
	private String path;

	/**
	 * 생성자
	 */
	public Attachment() {
		this(null, 0, null);
	}

	/**
	 * 생성자
	 * 
	 * @param fileName 첨부 파일 이름
	 * @param fileSize 첨부 파일 크기
	 * @param rawData 첨부 파일 data
	 */
	public Attachment(String fileName, long fileSize, byte[] rawData) {
		super();
		this.name = fileName;
		this.size = fileSize;
		this.rawData = rawData;
	}

	/**
	 * 첨부 파일 정보
	 *
	 * @param filePath 첨부 파일 경로
	 * @param fileName 첨부 파일 이름
	 * @param rawData 첨부 파일 data
	 */
	public Attachment(String filePath, String fileName, byte[] rawData) {
		this(fileName, (Utils.isValidate(rawData) ? rawData.length : -1), rawData);
		this.path = filePath;
	}

	/**
	 * 첨부 파일 data를 돌려 준다.
	 * 
	 * @return 파일 data
	 */
	public byte[] getBytes() {
		return this.rawData;
	}

	/**
	 * 첨부 파일 이름을 돌려 준다.
	 * 
	 * @return 파일 이름
	 */
	public String getFileName() {
		return this.name;
	}

	/**
	 * 첨부 파일 크기를 돌려 준다.
	 * 
	 * @return 파일 크기
	 */
	public long getSize() {
		return this.size;
	}

	/**
	 * 첨부 파일 data를 설정한다.
	 * 
	 * @param rawData 설정할 data
	 */
	public void setBytes(byte[] rawData) {
		this.rawData = rawData;
	}

	/**
	 * 첨부 파일 크기를 설정한다.
	 * 
	 * @param fileSize 설정할 크기
	 */
	public void setSize(long fileSize) {
		this.size = fileSize;
	}

	/**
	 * 첨부 파일 이름을 설정한다.
	 * 
	 * @param name 설정할 이름
	 */
	public void setName(String name) {
		this.name = name;
	}

	// 확장 ///

	public String getName() {
		return getFileName();
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toStringExclude(this, "rawData");
	}
}
