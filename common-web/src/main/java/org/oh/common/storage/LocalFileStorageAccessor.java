package org.oh.common.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oh.common.helper.IOHelper;
import org.oh.common.util.FileUtil;
import org.oh.common.util.LogUtil;
import org.oh.common.util.PropertyUtils;
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

//	protected static String CONFIG_FILEPATH = Constants.HOME_DIR + File.separator + Constants.CONF_DIR_NAME
//			+ File.separator + "storage.properties";
//	protected static String TEMP_DIR = Constants.HOME_DIR + File.separator + "temp";
	protected static SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	protected static Random RANDOM = new Random();
	protected static StorageAccessor storageAccessor = null;

	// File Storage (HashMap <UID,FileName>)
//	protected Map<String, String> localFileStorage;
//	protected Map<String, Attachment> localFileStorage;

	// Local file storage save file path
	protected String storageFilePath = null;

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
//		// AJOGU(5) + 00000001451290902030(20)
//		return String.format("%s%s%s%s%s%020d", chars[random.nextInt(len)], chars[random.nextInt(len)],
//				chars[random.nextInt(len)], chars[random.nextInt(len)], chars[random.nextInt(len)], c);

		StringBuilder sb = new StringBuilder();
		sb.append(SDF.format(new Date()));

		String chars[] = "1,2,3,4,5,6,7,8,9,0,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z".split(",");
		for (int i = 0; i < 5; i++) {
			sb.append(chars[RANDOM.nextInt(chars.length)]);
		}

		// 20151228171448753(17) + Q0FAB(5) = 22
		return sb.toString();
	}

	/**
	 * Singleton 객체를 돌려 준다.
	 * 
	 * @return Singleton 객체
	 */
	public static StorageAccessor getInstance() {
		if (storageAccessor == null) {
			log.info("Create Local File Storage Accessor");
			storageAccessor = new LocalFileStorageAccessor(getStorageFilePath());
		}

		return storageAccessor;
	}

	/**
	 * Storage 경로를 구한다.
	 * 
	 * @return
	 */
	protected static String getStorageFilePath() {
		String storageFilePath = null;

//		log.info("Configuration File: " + CONFIG_FILEPATH);
//		try {
//			PropertiesConfiguration prop = new PropertiesConfiguration();
//			prop.setEncoding("UTF-8");
//			prop.setReloadingStrategy(new FileChangedReloadingStrategy());
//			prop.load(new FileInputStream(CONFIG_FILEPATH));
//
//			storageFilePath = prop.getProperty("storage.file.path").toString();
//		} catch (Exception e) {
//			storageFilePath = TEMP_DIR;
//			log.warn("Failed to read " + CONFIG_FILEPATH + "! Default value: " + storageFilePath);
//		}
		storageFilePath = PropertyUtils.getInstance().getString("storage.file.path", "storage");

		return storageFilePath;
	}

	public LocalFileStorageAccessor() {
		this(getStorageFilePath());
	}

	/**
	 * 생성자
	 * 
	 * @param storageFilePath 서버로 전송한 파일을 임시로 저장할 폴더
	 */
	public LocalFileStorageAccessor(String storageFilePath) {
		log.info("Create Local File Storage Accessor");
		log.info("Storage Path: " + storageFilePath);

		this.storageFilePath = storageFilePath;

//		this.localFileStorage = Collections.synchronizedMap(new HashMap<String, String>());
//		this.localFileStorage = Collections.synchronizedMap(new HashMap<String, Attachment>());
	}

//	@SuppressWarnings("rawtypes")
//	public Map getStorage() {
//		return this.localFileStorage;
//	}

	@Deprecated
	public String getFileUID(String fileName) {
//		return this.localFileStorage.get(fileName);
		return null;
	}

	@Override
//	public boolean save(String UID, byte[] data) {
//		return save(UID, null, data);
//	}
	public boolean save(String fileName, byte[] data) {
		return save(generateUID(), fileName, data);
	}

	/**
	 * Local File 저장 후 Storage에 UID와 File명을 저장
	 */
	@Override
	public boolean save(String UID, String fileName, byte[] data) {
		log.debug("Start::save()");
		log.debug("  > UID: " + UID);
		log.debug("  > fileName: " + fileName);
		log.debug("  > data length: " + ((data == null) ? 0 : data.length));

		boolean result = false;
		FileOutputStream fos = null;
		FileChannel channel = null;

		try {
//			fos = new FileOutputStream(storageFilePath + File.separator + UID + "." + "file");
			String filePath = getFilePath(UID);

			// 파일 경로
			File dir = new File(storageFilePath + filePath);
			dir.mkdirs();

			// 파일 이름
			File file = new File(dir.getAbsolutePath() + File.separator + UID + "." + "file");
			log.debug("  > filePath: " + file.getAbsolutePath());
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
//			if (fileName != null && fileName.trim().length() > 0) {
//				Attachment pAttach = new Attachment(filePath, fileName, data);
////				this.localFileStorage.put(UID, fileName);
//				this.localFileStorage.put(UID, pAttach);
//			}

			result = true;
		} catch (Exception e) {
			return false;
		} finally {
			IOUtils.closeQuietly(channel);
			IOUtils.closeQuietly(fos);
		}

		// 파일 권한 추가
//		File file = new File(storageFilePath + File.separator + UID + "." + "file");
//		file.setReadable(true, false);
//		file.setWritable(true, false);

		log.debug("  > RV(result): " + result);
		log.debug("End::save()");

		return result;
	}

	@Override
	public byte[] load(String UID) {
		log.debug("Start::load()");
		log.debug("  > UID: " + UID);

		FileInputStream fin = null;
		byte[] bytes = null;

		try {
//			fin = new FileInputStream(storageFilePath + File.separator + UID + "." + "file");
			// 파일 이름
			File file = new File(storageFilePath + getFilePath(UID) + File.separator + UID + "." + "file");
			log.debug("  > filePath: " + file.getAbsolutePath());
			fin = new FileInputStream(file);

			bytes = IOHelper.readToEnd(fin);
		} catch (IOException e) {
			LogUtil.writeLog(e, getClass());
		} finally {
			IOUtils.closeQuietly(fin);
		}

		log.debug("  > RV(bytes length): " + ((bytes == null) ? 0 : bytes.length));
		log.debug("End::load()");

		return bytes;
	}

	@Override
	public boolean remove(String UID) {
		log.debug("Start::remove()");
		log.debug("  > UID: " + UID);

		boolean result = false;

		try {
//			if (FileUtil.forceDelete(storageFilePath + File.separator + UID + "." + "file")) {
			// 파일 이름
			File file = new File(storageFilePath + getFilePath(UID) + File.separator + UID + "." + "file");
			log.debug("  > filePath: " + file.getAbsolutePath());
			if (FileUtil.forceDelete(file.getAbsolutePath())) {
//				this.localFileStorage.remove(UID);
				log.debug("Deleted!");
				result = true;
			}

			else {
				log.warn("Failed to remove the UID, but the file was removed.");
			}
		} catch (Exception e) {
			log.warn("Failed to remove the file!", e);
		}

		log.debug("  > RV(result): " + result);
		log.debug("End::remove()");

		return result;
	}

//	@Override
//	public String getFileName(String UID) {
////		return this.localFileStorage.get(UID);
//		return this.localFileStorage.get(UID).getFileName();
//	}

//	@Override
//	public Attachment getFileInfo(String UID) {
//		Attachment pAttach = this.localFileStorage.get(UID);
//		if (pAttach != null)
//			return pAttach;
//
//		byte[] bytes = load(UID);
////		this.localFileStorage.put(UID, new Attachment(getFilePath(UID), null, bytes));
//
//		return this.localFileStorage.get(UID);
//	}

	/**
	 * 파일 경로를 가져온다.
	 */
	protected String getFilePath(String UID) {
//		String fileDate = StringUtil.getDate(Long.parseLong(UID.substring(6)));
//		return File.separator + fileDate.substring(0, 4) + File.separator + fileDate.substring(4, 6);
		return File.separator + UID.substring(0, 4) + File.separator + UID.substring(4, 6);
	}

	/**
	 * 저장한 모든 파일을 지운다.
	 */
	public void removeAll() {
		log.debug("Start::removeAll()");

		File fileDir = new File(storageFilePath);

		String[] fileList = fileDir.list();

		for (int i = 0; i < fileDir.length(); i++) {
			File f = new File(storageFilePath + File.separator + fileList[i]);
			log.debug("File Delete: " + f.getName());
			f.delete();
		}

//		this.localFileStorage.clear();

		log.debug("End::removeAll()");
	}

	public static void main(String[] args) throws Exception {
//		System.setProperty("HOME", "C:/dev/workspace/workspace_common/HOME");
//		PropertyConfigurator.configure(System.getProperty("HOME") + "/conf/server/log4j.properties");

		LocalFileStorageAccessor storageAccessor = new LocalFileStorageAccessor();
		for (int i = 0; i < 1; i++) {
			String UID = LocalFileStorageAccessor.generateUID();

			storageAccessor.save(UID, "테스트.txt", "테스트".getBytes());
//			System.out.println("fileInfo: " + storageAccessor.getFileInfo(UID));

			byte[] file = storageAccessor.load(UID);
			System.out.println(new String(file, "UTF-8"));
		}
	}
}
