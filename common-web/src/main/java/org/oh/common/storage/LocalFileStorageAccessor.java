package org.oh.common.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oh.common.Constants;
import org.oh.common.download.Attachment;
import org.oh.common.helper.IOHelper;
import org.oh.common.util.FileUtil;
import org.oh.common.util.HTTPUtil;
import org.oh.common.util.LogUtil;
import org.springframework.stereotype.Component;

/**
 * 로컬 파일 시스템을 storage로 이용해서 file을 저장하고 읽어 온다.
 * 
 * @version 1.5.0
 * @since 1.5
 */
@Component
public class LocalFileStorageAccessor implements StorageAccessor {
	protected static Log log = LogFactory.getLog(LocalFileStorageAccessor.class);

	private static final String configFile = Constants.HOME_DIR + File.separator + Constants.CONF_DIR_NAME
			+ File.separator + "storage.properties";
	private static final String TEMP_DIR = Constants.HOME_DIR + File.separator + "temp";

	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	private static final Random RANDOM = new Random();
	private static LocalFileStorageAccessor accessor = null;

	// File Storage (HashMap <UID,FileName>)
//	protected Map<String, String> localFileStorage;
	protected Map<String, Attachment> localFileStorage;

	// Local File Save Folder
	protected String uploadPath = null;

	public LocalFileStorageAccessor() {
	}

	/**
	 * Unique ID를 생성한다.
	 * 
	 * @return 생성한 Unique ID
	 */
	public static String generateUID() {
//		String chars[] = "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z".split(",");
//		long c = System.currentTimeMillis();
//		Random random = new Random();
//		int len = chars.length;
//
//		// 123456789012345678901234567890
//		// AJOGU00000001451290902030 (25)
//		return String.format("%s%s%s%s%s%020d", chars[random.nextInt(len)], chars[random.nextInt(len)],
//				chars[random.nextInt(len)], chars[random.nextInt(len)], chars[random.nextInt(len)], c);

		StringBuilder sb = new StringBuilder();

		// 20151228164243521 (17)
		sb.append(SDF.format(new Date()));

		String chars[] = "1,2,3,4,5,6,7,8,9,0,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z".split(",");
		for (int i = 0; i < 3; i++) {
			sb.append(chars[RANDOM.nextInt(chars.length)]);
		}

		// 20151228171448753Q0F (20)
		return sb.toString();
	}

	@SuppressWarnings("rawtypes")
	public Map getStorage() {
		return this.localFileStorage;
	}

	/**
	 * 생성자
	 * 
	 * @param uploadPath 서버로 전송한 파일을 임시로 저장할 폴더
	 */
	public LocalFileStorageAccessor(String uploadPath) {
		log.info("Create Local File Storage Accessor");
		log.debug("Temporary Storage Path : " + uploadPath);
		this.uploadPath = uploadPath;
//		this.localFileStorage = Collections.synchronizedMap(new HashMap<String, String>());
		this.localFileStorage = Collections.synchronizedMap(new HashMap<String, Attachment>());
	}

	/**
	 * Singleton 객체를 돌려 준다.
	 * 
	 * @return Singleton 객체
	 */
	public static LocalFileStorageAccessor getInstance() {
		String uploadPath = null;

		if (accessor == null) {
			log.info("Create Local File Storage Accessor");

			PropertiesConfiguration prop;
			log.debug("Configuration File : " + configFile);

			try {
				prop = new PropertiesConfiguration();
//				prop = load(configFile);
				prop.load(LocalFileStorageAccessor.class.getClassLoader()
						.getResourceAsStream(FileUtil.getName(configFile)));
				uploadPath = prop.getProperty("uploadfolder").toString();
				log.debug("UploadFolder Path : " + uploadPath);
			} catch (Exception e) {
				uploadPath = TEMP_DIR;
				log.warn("Failed to read " + configFile + "! Default value : " + uploadPath);
			}

			accessor = new LocalFileStorageAccessor(uploadPath);
		}

		return accessor;
	}

	@Deprecated
	public String getFileUID(String fileName) {
//		return this.localFileStorage.get(fileName);
		return null;
	}

	public boolean save(String UID, byte[] data) {
		return save(UID, null, data);
	}

	/**
	 * Local File 저장 후 Storage에 UID와 File명을 저장
	 */
	public boolean save(String UID, String fileName, byte[] data) {
		log.trace("Start::save()");
		log.trace("  > UID : " + UID);
		log.trace("  > fileName : " + fileName);
		log.trace("  > data length: " + ((data == null) ? 0 : data.length));

		boolean result = false;
		FileOutputStream fos = null;
		FileChannel channel = null;

		try {
//			fos = new FileOutputStream(uploadPath + File.separator + UID + "." + "file");
			String filePath = getFilePath(UID);

			// 파일 경로
			File dir = new File(uploadPath + filePath);
			dir.mkdirs();

			// 파일 이름
			File file = new File(dir.getAbsolutePath() + File.separator + UID + "." + "file");
			log.trace("  > filePath : " + file.getAbsolutePath());
			fos = new FileOutputStream(file);

			channel = fos.getChannel();
			ByteBuffer bytebuffer = ByteBuffer.allocate(IOHelper.READ_BLOCK);
			int offset = 0;
			int length = data.length;

			// Save File
			while (offset < length) {
				int chunkSize = IOHelper.READ_BLOCK > (length - offset) ? length - offset : IOHelper.READ_BLOCK;
				bytebuffer.put(data, offset, chunkSize);
				bytebuffer.flip();
				offset += chunkSize;
				channel.write(bytebuffer);
				bytebuffer.clear();
			}

			// Save Storage
			if (fileName != null && fileName.trim().length() > 0) {
				Attachment pAttach = new Attachment(filePath, fileName, data);
//				this.localFileStorage.put(UID, fileName);
				this.localFileStorage.put(UID, pAttach);
			}

			result = true;
		} catch (Exception e) {
			return false;
		} finally {
			HTTPUtil.closeQuietly(channel);
			HTTPUtil.closeQuietly(fos);
		}

		// 파일 권한 추가
//		File file = new File(uploadPath + File.separator + UID + "." + "file");
//		file.setReadable(true, false);
//		file.setWritable(true, false);

		log.trace("  > RV(result) : " + result);
		log.trace("End::save()");

		return result;
	}

	public byte[] load(String UID) {
		log.trace("Start::load()");
		log.trace("  > UID : " + UID);

		FileInputStream fin = null;
		byte[] bytes = null;

		try {
//			fin = new FileInputStream(uploadPath + File.separator + UID + "." + "file");
			// 파일 이름
			File file = new File(uploadPath + getFilePath(UID) + File.separator + UID + "." + "file");
			log.trace("  > filePath : " + file.getAbsolutePath());
			fin = new FileInputStream(file);

			bytes = IOHelper.readToEnd(fin);
		} catch (IOException e) {
			LogUtil.writeLog(e, getClass());
		} finally {
			HTTPUtil.closeQuietly(fin);
		}

		log.trace("  > RV(bytes length) : " + ((bytes == null) ? 0 : bytes.length));
		log.trace("End::load()");

		return bytes;
	}

	public boolean remove(String UID) {
		log.trace("Start::remove()");
		log.trace("  > UID : " + UID);

		boolean result = false;

		try {
//			if (FileUtil.forceDelete(uploadPath + File.separator + UID + "." + "file")) {
			// 파일 이름
			File file = new File(uploadPath + getFilePath(UID) + File.separator + UID + "." + "file");
			log.trace("  > filePath : " + file.getAbsolutePath());
			if (FileUtil.forceDelete(file.getAbsolutePath())) {
				this.localFileStorage.remove(UID);
				log.trace("Deleted!");
				result = true;
			}

			else {
				log.warn("Failed to remove the UID, but the file was removed.");
			}
		} catch (Exception e) {
			log.warn("Failed to remove the file!", e);
		}

		log.trace("  > RV(result) : " + result);
		log.trace("End::remove()");

		return result;
	}

	public String getFileName(String UID) {
//		return this.localFileStorage.get(UID);
		return this.localFileStorage.get(UID).getFileName();
	}

	/**
	 * 저장한 모든 파일을 지운다.
	 */
	public void removeAll() {
		log.trace("Start::removeAll()");

		File fileDir = new File(uploadPath);

		String[] fileList = fileDir.list();

		for (int i = 0; i < fileDir.length(); i++) {
			File f = new File(uploadPath + File.separator + fileList[i]);
			log.trace("File Delete : " + f.getName());
			f.delete();
		}

		this.localFileStorage.clear();

		log.trace("End::removeAll()");
	}

	public Attachment getFileInfo(String UID) {
		Attachment pAttach = this.localFileStorage.get(UID);
		if (pAttach != null)
			return pAttach;

		byte[] bytes = load(UID);
		this.localFileStorage.put(UID, new Attachment(getFilePath(UID), null, bytes));
		return this.localFileStorage.get(UID);
	}

	/**
	 * 파일 경로를 가져온다.
	 */
	protected String getFilePath(String UID) {
//		String fileDate = StringUtil.getDate(Long.parseLong(UID.substring(6)));
//		return File.separator + fileDate.substring(0, 4) + File.separator + fileDate.substring(4, 6);
		return File.separator + UID.substring(0, 4) + File.separator + UID.substring(4, 6);
	}

	public static void main(String[] args) throws Exception {
//		System.setProperty("HOME", "C:/dev/workspace/workspace_common/HOME");
//		PropertyConfigurator.configure(System.getProperty("HOME") + "/conf/server/log4j.properties");

		LocalFileStorageAccessor storageAccessor = new LocalFileStorageAccessor("storage");
		for (int i = 0; i < 1; i++) {
			String UID = generateUID();

			storageAccessor.save(UID, "테스트.txt", "테스트".getBytes());
			LogUtil.writeLog("fileInfo:" + storageAccessor.getFileInfo(UID));

			byte[] file = storageAccessor.load(UID);
			LogUtil.writeLog(new String(file, "UTF-8"));
		}
	}
}
