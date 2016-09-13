package org.oh.common.storage;

import org.oh.common.download.Attachment;

/**
 * 파일을 특정 storage에 저장하고, 읽어 온다.
 * 
 * @version 1.5
 * @since 1.5
 */
public interface StorageAccessor {
	/**
	 * file에 대한 Unique ID를 돌려 준다.
	 * 
	 * @param fileName Unique ID가 필요한 파일 이름
	 * @return Unique ID
	 */
	public String getFileUID(String fileName);

	/**
	 * 파일을 저장한다.
	 * 
	 * @param fileName 파일 이름
	 * @param data 저장할 파일 data의 byte 배열
	 * @return 저장에 성공하면 <code>true</code>
	 */
//	public boolean save(String UID, byte[] data);
	public boolean save(String fileName, byte[] data);

	/**
	 * 지정한 UID에 지정한 파일 이름을 매핑하고 파일을 저장한다.
	 * 
	 * @param UID Unique ID
	 * @param fileName 파일 이름
	 * @param data 저장할 파일 data의 byte 배열
	 * @return 저장에 성공하면 <code>true</code>
	 */
	public boolean save(String UID, String fileName, byte[] data);

	/**
	 * 파일을 읽어 온다.
	 * 
	 * @param UID Unique ID
	 * @return 읽어 온 파일 data
	 */
	public byte[] load(String UID);

	/**
	 * 파일을 지운다.
	 * 
	 * @param UID Unique ID
	 * @return 파일을 정상적으로 지웠으면 <code>true</code>
	 */
	public boolean remove(String UID);

	/**
	 * 파일 이름을 돌려 준다.
	 * 
	 * @param UID Unique ID
	 * @return 해당 Unique ID에 대한 파일 이름
	 */
//	public String getFileName(String UID);
	
	/**
	 * 파일 정보를 가져온다.
	 */
//	public Attachment getFileInfo(String UID);
}
