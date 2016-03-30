package org.oh.common.download;

/**
 * 첨부 파일을 download할 수 있는 downloader에 대한 interface
 * 
 * 
 * @version 1.0.0
 * 
 */
public interface AttachmentDownloader {
	/**
	 * 실제 data를 download하여 {@link org.oh.common.download.Attachment} 객체로 돌려 준다.
	 * 
	 * @param fileName download할 파일 이름
	 * @param fileID download할 파일의 고유한 ID
	 * @return 파일을 download하여 data를 가지고 있는 <code>Attachment</code> 객체
	 * @throws Exception 예외 발생 시에 해당 예외를 발생시킨다.
	 */
	abstract public Attachment download(String fileName, String fileID) throws Exception;
}
