package org.oh.common.storage;

import org.oh.common.file.Files;

/**
 * 파일을 특정 storage에 저장하고, 읽어 온다.
 * 
 * @version 1.5
 * @since 1.5
 */
public interface FileStorage {
	/**
	 * file에 대한 Unique ID를 돌려 준다.
	 * 
	 * @param fileName Unique ID가 필요한 파일 이름
	 * @return Unique ID
	 */
	@Deprecated
	public String getFileID(String fileName);

	/**
	 * 파일을 저장한다.
	 * 
	 * @param fileName 파일 이름
	 * @param data 저장할 파일 data의 byte 배열
	 * 
	 * @return 저장된 파일경로
	 */
	// 저장에 성공하면 <code>true</code>
//	public boolean save(String fileID, byte[] data);
	public String save(String fileName, byte[] data);

	/**
	 * 지정한 fileID에 지정한 파일 이름을 매핑하고 파일을 저장한다.
	 * 
	 * @param fileID Unique ID
	 * @param fileName 파일 이름
	 * @param data 저장할 파일 data의 byte 배열
	 * 
	 * @return 저장된 파일경로
	 */
	public String save(Files files);

	/**
	 * 파일을 읽어 온다.
	 * 
	 * @param fileID Unique ID
	 * @return 읽어 온 파일 data
	 */
	public byte[] load(String fileID);

	/**
	 * 파일을 지운다.
	 * 
	 * @param fileID Unique ID
	 * @return 파일을 정상적으로 지웠으면 <code>true</code>
	 */
	public boolean remove(String fileID);

	/**
	 * 저장한 모든 파일을 지운다.
	 */
	public void removeAll();

	/**
	 * 파일 이름을 돌려 준다.
	 * 
	 * @param fileID Unique ID
	 * @return 해당 Unique ID에 대한 파일 이름
	 */
//	public String getFileName(String fileID);

	/**
	 * 파일 정보를 가져온다.
	 */
//	public Files getFileInfo(String fileID);
}
