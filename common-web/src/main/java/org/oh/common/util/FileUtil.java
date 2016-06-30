package org.oh.common.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.oh.common.exception.SmartException;

/**
 * 파일/디렉토리 관련 유틸리티 클래스.<br/>
 * - org.apache.commons.io.FileUtils 클래스를 상속받음.
 *
 * @see <a href=http://commons.apache.org/io/api-release/org/apache/commons/io/FileUtils.html>org.apache.commons.io.FileUtils</a>
 */
public abstract class FileUtil extends FileUtils {
	// private static DecimalFormat FILE_SIZE_FORMAT = new java.text.DecimalFormat("#0.0");

	/**
	 * 주어진 디렉토리를 생성한다. 부모디렉토리가 없으면 부모디렉토리까지 생성한다.
	 *
	 * <pre>
	 * 예)
	 *    String  folderPath = "d:/sample/test";
	 *    <br>
	 *    
	 *    FileUtil.forceMkdir(folderPath);    // 디렉토리 생성
	 * </pre>
	 *
	 * @param dirPath 디렉토리 경로
	 * @return 성공시 true, 실패시 false
	 */
	public static boolean forceMkdir(String dirPath) {
		try {
			File dir = new File(dirPath);
			forceMkdir(dir);
			return true;
		} catch (Exception e) {
			LogUtil.writeLog(e, FileUtil.class);
		}

		return false;
	}

	/**
	 * 주어진 파일에 일치하는 파일을 삭제. 파일이 디렉토리라면 서브디렉토리까지 포함하여 디렉토리를 삭제.
	 *
	 * <pre>
	 * 예)
	 *    String  filePath   = "d:/test/code/code.java"; 
	 *    String  folderPath = "d:/sample/test";
	 *    <br>
	 *    
	 *    FileUtil.forceDelete(filePath);    // "code.java" 파일 삭제
	 *    FileUtil.forceDelete(folderPath);  // "d:/sample/test" 디렉토리 삭제
	 * </pre>
	 *
	 * @param filePath 파일 경로
	 * @return 성공시 true, 실패시 false
	 */
	public static boolean forceDelete(String filePath) {
		try {
			File file = new File(filePath);
			forceDelete(file);
			return true;
		} catch (Exception e) {
			LogUtil.writeLog(e, FileUtil.class);
		}

		return false;
	}

	/**
	 * 주어진 파일에 존재여부 확인
	 *
	 * @param filePath 파일 경로
	 * @return 성공시 true, 실패시 false
	 */
	public static boolean forceExist(String filePath) {
		boolean result = false;
		try {
			File file = new File(filePath);
			if (file.exists()) {
				result = true;
			}
		} catch (Exception e) {
			LogUtil.writeLog(e, FileUtil.class);
		}

		return result;
	}

	/**
	 * 지정한 위치의 파일을 대상 위치로 이동한다.
	 *
	 * <pre>
	 * 예)
	 *    String  srcFilePath   = "d:/test/code/code.java"; // 대상파일
	 *    String  destDirPath   = "d:/sample/test";         // 이동할 디렉토리
	 *    <br>
	 *    
	 *    FileUtil.moveFileToDirectory(srcFilePath, destDirPath, true);    // "code.java" 파일 이동 ("d:/sample/test" 디렉토리 없을시 생성) 
	 *    FileUtil.moveFileToDirectory(srcFilePath, destDirPath, false);   // "code.java" 파일 이동 ("d:/sample/test" 디렉토리 없을시 false)
	 * </pre>
	 *
	 * @param srcFilePath 대상 파일
	 * @param destDirPath 이동할 디렉토리
	 * @param createDestDir 디렉토리 없을시 생성 유무 ( true:생성, false:미생성 )
	 * @return 성공시 true, 실패시 false
	 */
	public static boolean moveFileToDirectory(String srcFilePath, String destDirPath, boolean createDestDir) {
		try {
			moveFileToDirectory(new File(srcFilePath), new File(destDirPath), createDestDir);
			return true;
		} catch (Exception e) {
			LogUtil.writeLog(e, FileUtil.class);
		}

		return false;
	}

	/**
	 * 주어진 파일에 대한 FileInputStream을 오픈.
	 *
	 * <pre>
	 * 예)
	 *    String  filePath   = "d:/test/code/code.java"; // 대상파일
	 *    <br>
	 *    
	 *    FileInputStream openInputStream = FileUtil.openInputStream(filePath); // 파일의   FileInputStream 리턴
	 * </pre>
	 *
	 * @param filePath 파일 경로
	 * @return 주어진 파일 경로에 대한 FileInputStream 객체
	 * @throws IOException 파일이 존재하지 않거나, 파일이 디렉토리이거나, 파일을 읽을 수 없는 경우
	 */
	public static FileInputStream openInputStream(String filePath) throws IOException {
		return openInputStream(new File(filePath));
	}

	/**
	 * 주어진 문자열 데이타를 지정된 파일로 저장.
	 * 
	 *
	 * <pre>
	 * 예)
	 *    String  filePath    = "d:/test/code/code.txt"; // 저장될 파일
	 *    String  stringData  = "File Util WriteStringToFile Test"; // 저장될 문자열
	 *    <br>
	 *    
	 *    FileUtil.writeStringToFile(filePath,stringData,"UTF-8"); // 문자열을 UTF-8로 인코딩하여 code.text파일에 저장
	 * </pre>
	 *
	 * @param filePath 저장할 파일 경로
	 * @param data 문자열 데이타
	 * @param encoding 인코딩
	 * @return 성공시 true, 실패시 false
	 */
	public static boolean writeStringToFile(String filePath, String data, String encoding) {
		try {
			File file = new File(filePath);
			writeStringToFile(file, data, encoding);
			return true;
		} catch (Exception e) {
			LogUtil.writeLog(e, FileUtil.class);
		}

		return false;
	}

	/**
	 * 주어진 입력스트림으로부터 데이터를 읽어와 지정된 파일로 저장.
	 *
	 * <pre>
	 * 예)
	 *    String  filePath    = "d:/test/code/code.txt"; // 저장될 파일
	 *    InputStream inputStream = new FileInputStream("d:/test/code/code.java"); // 저장할 InputStream 
	 *    <br>
	 *    
	 *   FileUtil.writeInputStreamToFile(filePath, inputStream); //  InputStream을 code.text파일에 저장
	 * </pre>
	 *
	 * @param filePath 저장할 파일 경로
	 * @param in 입력스트림
	 * @return 성공시 true, 실패시 false
	 */
	public static boolean writeInputStreamToFile(String filePath, InputStream in) {
		boolean result = false;
		FileOutputStream out = null;

		try {
			out = new FileOutputStream(filePath);
			HTTPUtil.copyLarge(in, out);
			out.flush();
			result = true;
		} catch (Exception e) {
			LogUtil.writeLog(e, FileUtil.class);
		} finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
		return result;
	}

	/*
	 * public static String toFileSizeFormat(String byteString) {
	 * try {
	 * double dFileSize = Double.parseDouble(byteString); // 바이트
	 * if (dFileSize == 0.0) {
	 * return " (0KB)";
	 * }
	 * 
	 * dFileSize = dFileSize / 1024; // KB
	 * if (dFileSize < 1024) {
	 * return (dFileSize < 1.0) ? " (1KB)" : " (" + (new Double(dFileSize)).intValue() + "KB)";
	 * }
	 * 
	 * dFileSize = dFileSize / 1024; // MB
	 * if (dFileSize < 1024) {
	 * return " (" + FILE_SIZE_FORMAT.format(dFileSize) + "MB)";
	 * }
	 * } catch (Exception e) {
	 * }
	 * 
	 * return " (" + byteString + "btye)";
	 * }
	 */

	/**
	 * 주어진 원본 이미지 파일을 지정된 크기로 리사이징하여 저장한다.
	 *
	 * <pre>
	 * 예)
	 *    String  fromImagePath    = "d:/test/code/Sample.jpg"; // 원본 이미지 파일
	 *    String  toImagePath      = "d:/test/code/Sample_resize.jpg"; // 리사이징 이미지 파일 
	 *    <br>
	 *    
	 *    FileUtil.resizeImage(fromImagePath, toImagePath, 200, 300); // 이미지 리사이징  "d:/test/code/Sample_resize.jpg"에 저장됨
	 * </pre>
	 *
	 * @param fromImagePath 원본 이미지 파일 경로
	 * @param toImagePath 리사이징된 이미지 파일 경로
	 * @param width 리사이징 너비
	 * @param height 리사이징 높이
	 */
	public static void resizeImage(String fromImagePath, String toImagePath, int width, int height)
			throws SmartException {
		try {
			Image in = ImageIO.read(new File(fromImagePath)).getScaledInstance(width, height, Image.SCALE_SMOOTH);

			BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			out.getGraphics().drawImage(in, 0, 0, null);
			File file = new File(toImagePath);
			ImageIO.write(out, getExtension(toImagePath), file);
		} catch (Exception e) {
			LogUtil.writeLog(e, FileUtil.class);
			throw new SmartException(e);
		}
	}

	public static void bmpToPng(String fromImagePath, String toImagePath) throws SmartException {
		try {
			BufferedImage inBuf = ImageIO.read(new File(fromImagePath));
			int width = inBuf.getWidth();
			int height = inBuf.getHeight();
			Image in = inBuf.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			;

			BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			out.getGraphics().drawImage(in, 0, 0, null);
			File file = new File(toImagePath);
			ImageIO.write(out, getExtension(toImagePath), file);
		} catch (Exception e) {
			LogUtil.writeLog(e, FileUtil.class);
			throw new SmartException(e);
		}
	}

	/**
	 * 주어진 파일 경로에서 파일 이름을 가져온다.
	 *
	 * <pre>
	 * 예)
	 *    String  filePath1    = "d:/test/code/code1.txt"; // 대상 파일1
	 *    String  filePath2    = "d:/test/code/code2.xml"; // 대상 파일2
	 *    <br>
	 *    
	 *    FileUtil.getExtension(filePath1); // "code1" 리턴
	 *    FileUtil.getExtension(filePath2); // "code2" 리턴
	 * </pre>
	 *
	 * @param filePath 파일 경로
	 * @return 파일 이름
	 */
	public static String getBaseName(String filePath) {
		return FilenameUtils.getBaseName(filePath);
	}

	/**
	 * 주어진 파일 경로에서 파일 확장자를 가져온다.
	 *
	 * <pre>
	 * 예)
	 *    String  filePath1    = "d:/test/code/code.txt"; // 대상 파일1
	 *    String  filePath2    = "d:/test/code/code.xml"; // 대상 파일2
	 *    <br>
	 *    
	 *    FileUtil.getExtension(filePath1); // "txt" 리턴
	 *    FileUtil.getExtension(filePath2); // "xml" 리턴
	 * </pre>
	 *
	 * @param filePath 파일 경로
	 * @return 파일 확장자.
	 */
	public static String getExtension(String filePath) {
		return FilenameUtils.getExtension(filePath);
	}

	/**
	 * 주어진 파일 경로에서 파일명을 가져온다.
	 *
	 * <pre>
	 * 예)
	 *    String  filePath1    = "d:/test/code/code.txt"; // 대상 파일1
	 *    String  filePath2    = "d:/test/code/sample.xml"; // 대상 파일2
	 *    <br>
	 *    
	 *    FileUtil.getName(filePath1); // "code.txt" 리턴
	 *    FileUtil.getName(filePath2); // "sample.xml" 리턴
	 * </pre>
	 *
	 * @param filePath 파일명
	 * @return 파일명.
	 */
	public static String getName(String filePath) {
		return FilenameUtils.getName(filePath);
	}

	/**
	 * 주어진 파일 경로에서 경로를 가져온다.
	 *
	 * <pre>
	 * 예)
	 *    String  filePath1    = "d:/test/code/code.txt"; // 대상 파일1
	 *    String  filePath2    = "d:/sample/test/code/sample.xml"; // 대상 파일2
	 *    <br>
	 *    
	 *    FileUtil.getPath(filePath1); // "test\code\" 리턴 
	 *    FileUtil.getPath(filePath2); // "sample\test\code" 리턴
	 * </pre>
	 *
	 * @param filePath 파일 경로
	 * @return 경로.
	 */
	public static String getPath(String filePath) {
		return FilenameUtils.getPath(filePath);
	}

	/**
	 * 주어진 경로내 모든 구분자(/, \)를 UINX 구분자(/)로 변환.
	 *
	 * <pre>
	 * 예)
	 *    String  filePath1    = "d:\\test\\code\\code.txt"; // 대상 파일1
	 *    String  filePath2    = "d:\\sample\\test\\code\\sample.xml"; // 대상 파일2
	 *    <br>
	 *    
	 *    FileUtil.separatorsToUnix(filePath1); // "d:/test/code/code.txt" 리턴 
	 *    FileUtil.separatorsToUnix(filePath2); // "d:/sample/test/code/sample.xml" 리턴
	 * </pre>
	 *
	 * @param path 경로
	 * @return UINX 구분자(/)로 변환된 경로
	 */
	public static String separatorsToUnix(String path) {
		return FilenameUtils.separatorsToUnix(path);
	}

	/**
	 * 주어진 경로내 모든 구분자(/, \)를 UINX 구분자(/)로 변환한 후, 주어진 경로를 UINX 구분자(/)로 시작하는 경로로 변환.
	 *
	 * <pre>
	 * 예)
	 *    String  filePath1    = "test\\code\\code.txt"; // 대상 파일1
	 *    String  filePath2    = "sample\\test\\code\\sample.xml"; // 대상 파일2
	 *    <br>
	 *    
	 *    FileUtil.toStartsWithUnixSeparator(filePath1); // "/test/code/code.txt" 리턴 
	 *    FileUtil.toStartsWithUnixSeparator(filePath2); // "/sample/test/code/sample.xml" 리턴
	 * </pre>
	 *
	 * @param path 경로
	 * @return UINX 구분자(/)로 변환된 UINX 구분자(/)로 시작하는 경로.<br/>
	 *         주어진 경로가 화이트문자이면 빈문자열("")을 반환
	 */
	public static String toStartsWithUnixSeparator(String path) {
		if (StringUtil.isBlank(path)) {
			return "";
		} else {
			path = FilenameUtils.separatorsToUnix(path.trim());
			return (path.startsWith("/")) ? path : "/" + path;
		}
	}

	/**
	 * 와일드카드(*, ?)를 사용해서 파일을 찾는다.
	 * 
	 * @param dir 디렉토리
	 * @param fileName 파일명
	 * 
	 * @return File[]
	 */
	public static File[] findFiles(String dir, String fileName) {
		File file = new File(dir);
		FileFilter fileFilter = new WildcardFileFilter(fileName);

		return file.listFiles(fileFilter);
	}

	/**
	 * 파일들을 지운다.(예외는 무시)
	 * 
	 * @param files
	 */
	public static void forceDelete(File... files) {
		for (File file : files) {
			try {
				forceDelete(file);
			} catch (Exception e) {
				LogUtil.writeLog(e, FileUtil.class);
			}
		}
	}

	public static String getAttachFileSize(String fileSize) {
		if (fileSize == null || fileSize.equals("") == true) {
			return fileSize;
		}

		long size = Long.parseLong(fileSize);

		return getFileSize(size);
	}

	public static String getFileSize(long fileSize) {
		String attacheSize;

		if (fileSize >= 1024) {
			fileSize = fileSize / 1024;
			if (fileSize >= 1024) {
				BigDecimal size = new BigDecimal(fileSize);
				BigDecimal dev = new BigDecimal("1024");
				BigDecimal file = size.divide(dev, 1, BigDecimal.ROUND_HALF_UP);

				attacheSize = file.doubleValue() + "M";
			} else {
				attacheSize = String.valueOf(fileSize) + "K";
			}
		} else {
			attacheSize = String.valueOf(fileSize) + "b";
		}

		return attacheSize;
	}

	public static String getFileSize(final String fileSize) {
		return getFileSize(Long.valueOf(fileSize));
	}

	public static String toKB(final long fileSize) {
		String result = processRoundAndUnitName(fileSize / 1024.0, " KB");
		if (result.equalsIgnoreCase("0 KB")) {
			result = "1 KB";
		}
		return result;
	}

	public static String KbToKb(final long fileSize) {
		String result = processRoundAndUnitName(fileSize, " KB");
		if (result.equalsIgnoreCase("0 KB")) {
			result = "1 KB미만";
		}

		return result;
	}

	public static String toKB(final String fileSize) {
		String result = processRoundAndUnitName(Double.valueOf(fileSize) / 1024.0, " KB");

		if (result.equalsIgnoreCase("0 KB")) {
			result = "1 KB";
		}
		return result;
	}

	public static final String processRoundAndUnitName(double size, String unitName) {
		return Math.round(size) + unitName;
	}

	public static void main(String[] args) {
		LogUtil.writeLog(
				Arrays.toString(findFiles("D:/home/smosuser/was_docs/Daekyo/SmartMOS/member/member/", "*_mb.db")));
	}
}
