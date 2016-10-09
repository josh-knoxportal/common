package org.oh.common.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oh.common.exception.CommonException;
import org.oh.common.file.Files;
import org.oh.common.helper.IOHelper;
import org.oh.common.util.FileUtil;
import org.oh.common.util.PropertyUtils;
import org.oh.common.util.Utils;
import org.springframework.stereotype.Component;

/**
 * 로컬 파일 시스템을 storage로 이용해서 file을 저장하고 읽어 온다.
 * 
 * @version 1.5.0
 * @since 1.5
 */
@Component
public class LocalFileStorage implements FileStorage {
	public static String FILE_EXTENSION = "file";;

	protected static Log log = LogFactory.getLog(LocalFileStorage.class);
//	protected static String CONFIG_FILEPATH = Constants.HOME_DIR + File.separator + Constants.CONF_DIR_NAME
//			+ File.separator + "storage.properties";
//	protected static String TEMP_DIR = Constants.HOME_DIR + File.separator + "temp";
	protected static SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	protected static Random RANDOM = new Random();
	protected static FileStorage fileStorage = null;

	// File Storage (HashMap <fileID,FileName>)
//	protected Map<String, String> localFileStorage;
//	protected Map<String, Files> localFileStorage;

	// Local file storage save root path
	protected String storageRootPath = null;

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
	 * Storage 루트 경로를 구한다.
	 * 
	 * @return
	 */
	public static String getStorageRootPath() {
		String storageRootPath = null;

//		log.info("Configuration File: " + CONFIG_FILEPATH);
//		try {
//			PropertiesConfiguration prop = new PropertiesConfiguration();
//			prop.setEncoding("UTF-8");
//			prop.setReloadingStrategy(new FileChangedReloadingStrategy());
//			prop.load(new FileInputStream(CONFIG_FILEPATH));
//
//			storageRootPath = prop.getProperty("storage.file.path").toString();
//		} catch (Exception e) {
//			storageRootPath = TEMP_DIR;
//			log.warn("Failed to read " + CONFIG_FILEPATH + "! Default value: " + storageRootPath);
//		}
		storageRootPath = PropertyUtils.getInstance().getString("storage.root.path", "storage");

		return storageRootPath;
	}

	/**
	 * 파일 경로를 가져온다.
	 */
	public static String getFilePath() {
		return File.separator + Utils.formatCurrentDate(Utils.SDF_YEAR) + File.separator
				+ Utils.formatCurrentDate(Utils.SDF_MONTH);
	}

	/**
	 * 파일 경로를 가져온다.
	 * 
	 * @param fileID
	 * 
	 * @return
	 */
	public static String getFilePath(String fileID) {
//		String fileDate = StringUtil.getDate(Long.parseLong(fileID.substring(6)));
//		return File.separator + fileDate.substring(0, 4) + File.separator + fileDate.substring(4, 6);
		return File.separator + fileID.substring(0, 4) + File.separator + fileID.substring(4, 6);
	}

	/**
	 * Singleton 객체를 돌려 준다.
	 * 
	 * @return Singleton 객체
	 */
	public static FileStorage getInstance() {
		if (fileStorage == null) {
			log.info("Create Local File Storage Accessor");
			fileStorage = new LocalFileStorage(getStorageRootPath());
		}

		return fileStorage;
	}

	public LocalFileStorage() {
		this(getStorageRootPath());
	}

	/**
	 * 생성자
	 * 
	 * @param storageRootPath 서버로 전송한 파일을 임시로 저장할 폴더
	 */
	public LocalFileStorage(String storageRootPath) {
		log.info("Create Local File Storage Accessor");
		log.info("Storage Path: " + storageRootPath);

		this.storageRootPath = storageRootPath;

//		this.localFileStorage = Collections.synchronizedMap(new HashMap<String, String>());
//		this.localFileStorage = Collections.synchronizedMap(new HashMap<String, Files>());
	}

//	@SuppressWarnings("rawtypes")
//	public Map getStorage() {
//		return this.localFileStorage;
//	}

	@Override
	@Deprecated
	public String getFileID(String fileName) {
//		return this.localFileStorage.get(fileName);
		return null;
	}

	@Override
//	public boolean save(String fileID, byte[] bytes) {
//		return save(fileID, null, bytes);
//	}
	public String save(String fileName, byte[] bytes) {
		return save(new Files(fileName, bytes));
	}

	@Override
	public String save(Files files) throws CommonException {
		log.debug("Start::save()");
		log.debug("  > files: " + files);

//		boolean result = false;
		File file = null;
		FileOutputStream fos = null;
		FileChannel channel = null;

		try {
//			fos = new FileOutputStream(storageRootPath + File.separator + fileID + "." + FILE_EXTENSION);
			String filePath = getFilePath(files.getId());

			// 파일 경로
			File dir = new File(storageRootPath + filePath);
			dir.mkdirs();

			// 파일 이름
			file = new File(dir.getAbsolutePath() + File.separator + files.getId() + "." + FILE_EXTENSION);
			log.debug("  > filePath: " + file.getAbsolutePath());
			fos = new FileOutputStream(file);

			channel = fos.getChannel();
			ByteBuffer bytebuffer = ByteBuffer.allocate(IOHelper.READ_BLOCK);
			int offset = 0;
			int length = files.getFile_bytes().length;

			// Save File
			while (offset < length) {
				int chunkSize = IOHelper.READ_BLOCK > (length - offset) ? length - offset : IOHelper.READ_BLOCK;
				bytebuffer.put(files.getFile_bytes(), offset, chunkSize);
				bytebuffer.flip();
				offset += chunkSize;
				channel.write(bytebuffer);
				bytebuffer.clear();
			}

			// Save Storage
//			if (fileName != null && fileName.trim().length() > 0) {
////			this.localFileStorage.put(fileID, fileName);
//				this.localFileStorage.put(fileID, files);
//			}

//			result = true;
		} catch (Exception e) {
			throw new CommonException("Failed to save the file(" + files.getId() + ")", e);
//			return false;
		} finally {
			IOUtils.closeQuietly(channel);
			IOUtils.closeQuietly(fos);
		}

		// 파일 권한 추가
//		File file = new File(storageRootPath + File.separator + fileID + "." + FILE_EXTENSION);
//		file.setReadable(true, false);
//		file.setWritable(true, false);

		log.debug("  > RV(result): " + file.getAbsolutePath());
		log.debug("End::save()");

//		return result;
		return file.getAbsolutePath();
	}

	@Override
	public byte[] load(String fileID) {
		log.debug("Start::load()");
		log.debug("  > fileID: " + fileID);

		FileInputStream fin = null;
		byte[] bytes = null;

		try {
//			fin = new FileInputStream(storageRootPath + File.separator + fileID + "." + FILE_EXTENSION);
			// 파일 이름
			File file = new File(storageRootPath + getFilePath(fileID) + File.separator + fileID + "." + FILE_EXTENSION);
			log.debug("  > filePath: " + file.getAbsolutePath());
			fin = new FileInputStream(file);

			bytes = IOHelper.readToEnd(fin);
		} catch (Exception e) {
			log.error("Failed to load the file(" + fileID + ")", e);
		} finally {
			IOUtils.closeQuietly(fin);
		}

		log.debug("  > RV(bytes length): " + ((bytes == null) ? 0 : bytes.length));
		log.debug("End::load()");

		return bytes;
	}

	@Override
	public boolean remove(String fileID) {
		log.debug("Start::remove()");
		log.debug("  > fileID: " + fileID);

		boolean result = false;

		try {
//			if (FileUtil.forceDelete(storageRootPath + File.separator + fileID + "." + FILE_EXTENSION)) {
			// 파일 이름
			File file = new File(storageRootPath + getFilePath(fileID) + File.separator + fileID + "." + FILE_EXTENSION);
			log.debug("  > filePath: " + file.getAbsolutePath());
			if (FileUtil.forceDelete(file.getAbsolutePath())) {
//				this.localFileStorage.remove(fileID);
				log.debug("Deleted!");
				result = true;
			} else {
				log.warn("Failed to remove the file(" + fileID + ")");
			}
		} catch (Exception e) {
			log.error("Failed to remove the file(" + fileID + ")", e);
		}

		log.debug("  > RV(result): " + result);
		log.debug("End::remove()");

		return result;
	}

	@Override
	public void removeAll() {
		log.debug("Start::removeAll()");

		File fileDir = new File(storageRootPath);

		String[] fileList = fileDir.list();

		for (int i = 0; i < fileDir.length(); i++) {
			File f = new File(storageRootPath + File.separator + fileList[i]);
			log.debug("File Delete: " + f.getName());
			f.delete();
		}

//		this.localFileStorage.clear();

		log.debug("End::removeAll()");
	}

//	@Override
//	public String getFileName(String fileID) {
////		return this.localFileStorage.get(fileID);
//		return this.localFileStorage.get(fileID).getFileName();
//	}

//	@Override
//	public Files getFileInfo(String fileID) {
//		Files files = this.localFileStorage.get(fileID);
//		if (files != null)
//			return files;
//
//		byte[] bytes = load(fileID);
////		this.localFileStorage.put(fileID, new Files(fileID, null, null, bytes));
//
//		return this.localFileStorage.get(fileID);
//	}

	public static void main(String[] args) throws Exception {
//		System.setProperty("HOME", "C:/dev/workspace/workspace_common/HOME");
//		PropertyConfigurator.configure(System.getProperty("HOME") + "/conf/server/log4j.properties");

		LocalFileStorage fileStorage = new LocalFileStorage();
		for (int i = 0; i < 1; i++) {
			Files files = new Files("테스트.txt", "테스트".getBytes());

			fileStorage.save(files);
//			System.out.println("fileInfo: " + fileStorage.getFileInfo(fileID));

			byte[] file = fileStorage.load(files.getId());
			System.out.println(new String(file, "UTF-8"));
		}
	}
}
