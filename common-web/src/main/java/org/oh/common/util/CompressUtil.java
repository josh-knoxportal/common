package org.oh.common.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Stack;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.IOUtils;
import org.oh.common.exception.SmartException;

/**
 * 파일 압축 유틸리티 클래스.
 * 
 * @version 2.5
 * @since 1.0.0
 */
public abstract class CompressUtil {
	private static boolean debug = true;
	private static int BUFF_SIZE = 16 * 1024;

	/**
	 * 지정된 압축파일의 압축을 해제한다.
	 * 
	 * <pre>
	 * 예)
	 *    String filePath = "d:/test/zip_test/zipTest.zip"; // 압축 파일
	 *    <br>
	 *    File zipFile = new File(filePath);
	 *    CompressUtil.unzip(zipFile);      // 압축 해제
	 * </pre>
	 *
	 * @param zipFile 압축파일
	 * @throws SmartException
	 */
	public static void unzip(File zipFile) throws SmartException {
		unzip(zipFile, Charset.defaultCharset().name());
	}

	/**
	 * 지정된 압축파일의 압축을 해제한다. (인코딩 지정)
	 * 
	 * <pre>
	 * 예)
	 *    String filePath = "d:/test/zip_test/zipTest.zip"; // 압축파일
	 *    <br>
	 *    File zipFile = new File(filePath);
	 *    CompressUtil.unzip(zipFile, "UTF-8");     // 압축 해제(인코딩 지정)
	 * </pre>
	 *
	 * @param zipFile 압축파일
	 * @param charset 인코딩 타입
	 * @throws SmartException
	 */
	public static void unzip(File zipFile, String charset) throws SmartException {
		unzip(zipFile, zipFile.getParentFile(), charset);
	}

	/**
	 * 지정된 압축파일의 압축을 해제한다. (디렉토리 지정)
	 * 
	 * <pre>
	 * 예)
	 *    String    filePath    = "d:/test/zip_test/zipTest.zip";   // 압축파일
	 *    String    targetPath  = "d:/test/zip_test/zipTest";       // 압축해제 대상 디렉토리
	 *    <br>
	 *    File zipFile      = new File(filePath);
	 *    File targetFolder = new File(targetPath);
	 *    <br>
	 *    CompressUtil.unzip(zipFile, targetFolder);    // 압축해제 (디렉토리지정)
	 * </pre>
	 *
	 * @param zipFile 압축파일
	 * @param toDir 타겟 디렉토리
	 * @throws SmartException
	 */
	public static void unzip(File zipFile, File toDir) throws SmartException {
		try {
			unzip(new FileInputStream(zipFile), toDir, Charset.defaultCharset().name());
		} catch (SmartException e) {
			throw e;
		} catch (Exception e) {
			throw new SmartException(e);
		}
	}

	/**
	 * 지정된 압축파일의 압축을 해제한다. (디렉토리, 인코딩 지정)
	 * 
	 * <pre>
	 * 예)
	 *    String    filePath    = "d:/test/zip_test/zipTest.zip";   // 압축파일
	 *    String    targetPath  = "d:/test/zip_test/zipTest";       // 압축해제 대상 디렉토리
	 *    <br>
	 *    File zipFile      = new File(filePath);
	 *    File targetFolder = new File(targetPath);
	 *    <br>
	 *    CompressUtil.unzip(zipFile, targetFolder, "UTF-8"); // 압축해제 (디렉토리,인코딩 지정)
	 * </pre>
	 *
	 * @param zipFile 압축파일
	 * @param toDir 타겟 디렉토리
	 * @param charset 인코딩 타입
	 * @throws SmartException
	 */
	public static void unzip(File zipFile, File toDir, String charset) throws SmartException {
		try {
			unzip(new FileInputStream(zipFile), toDir, charset);
		} catch (SmartException e) {
			throw e;
		} catch (Exception e) {
			throw new SmartException(e);
		}
	}

	/**
	 * 지정된 압축파일의 압축을 해제한다. (InputStream 사용)
	 * 
	 * <pre>
	 * 예)
	 *    String    filePath    = "d:/test/zip_test/zipTest.zip";   // 압축파일
	 *    String    targetPath  = "d:/test/zip_test/zipTest";       // 압축해제 대상 디렉토리
	 *    <br>
	 *    FileInputStream fileInputStream = new FileInputStream(new File(filePath));
	 *    File targetFolder = new File(targetPath);
	 *    <br>
	 *    CompressUtil.unzip(fileInputStream, targetFolder); // 압축해제 (디렉토리 지정)
	 * </pre>
	 *
	 * @param is 압축파일 (InputStream)
	 * @param toDir 타겟 디렉토리
	 * @throws SmartException
	 */
	public static void unzip(InputStream is, File toDir) throws SmartException {
		unzip(is, toDir, Charset.defaultCharset().name());
	}

	/**
	 * 지정된 압축파일의 압축을 해제한다. (InputStream, 타겟, 인코딩 지정)
	 * 
	 * <pre>
	 * 예)
	 *    String    filePath    = "d:/test/zip_test/zipTest.zip";   // 압축파일
	 *    String    targetPath  = "d:/test/zip_test/zipTest";       // 압축해제 대상 디렉토리
	 *    <br>
	 *    FileInputStream fileInputStream = new FileInputStream(new File(filePath));
	 *    File targetFolder = new File(targetPath);
	 *    <br>
	 *    CompressUtil.unzip(fileInputStream, targetFolder, "UTF-8"); // 압축해제 (디렉토리, 인코딩 지정)
	 * </pre>
	 *
	 * @param in 압축파일 (InputStream)
	 * @param toDir 타겟 디렉토리
	 * @param charset 인코딩 타입
	 * @throws SmartException
	 */
	public static void unzip(InputStream in, File toDir, String charset) throws SmartException {
		// 인/아웃 스트림을 강제적으로 close하도록 수정
		ZipArchiveInputStream zipIn = null;

		try {
			zipIn = new ZipArchiveInputStream(in, charset, false);
			ZipArchiveEntry entry = null;
			BufferedOutputStream out = null;
			byte[] buf = new byte[BUFF_SIZE];
			while ((entry = zipIn.getNextZipEntry()) != null) {
				try {
					String name = entry.getName();
					File target = new File(toDir, name);
					if (entry.isDirectory()) {
						target.mkdirs(); /* does it always work? */
						debug("dir: " + name);
					} else {
						String directory = target.getParent();
						if (directory != null) {
							directory += "/";
							File tf = new File(directory);
							if (!tf.exists()) {
								tf.mkdirs();
							}
						}

						if (entry.getSize() != 0) {
							target.createNewFile();
							out = new BufferedOutputStream(new FileOutputStream(target));
							int nWritten = 0;
							while ((nWritten = zipIn.read(buf)) >= 0) {
								out.write(buf, 0, nWritten);
							}
							out.flush();
							IOUtils.closeQuietly(out);
						}
						debug("file: " + name);
					}
				} catch (Exception e) {
				} finally {
					IOUtils.closeQuietly(out);
				}
			}
		} catch (Exception e) {
			LogUtil.writeLog(e, CompressUtil.class);
			throw new SmartException(e);
		} finally {
			IOUtils.closeQuietly(zipIn);
			IOUtils.closeQuietly(in);
		}
	}

	/**
	 * 지정된 파일 또는 디렉토리를 압축한다. (UTF-8 디폴트 인코딩)
	 * 
	 * <pre>
	 * 예)
	 *    String    filePath    = "d:/test/zip/testfile.txt";
	 *    String    folderPath  = "d:/test/zip"; 
	 *    <br>
	 *    File zipFile      = new File(filePath);
	 *    File zipFolder    = new File(folderPath);
	 *    <br>
	 *    CompressUtil.zip(zipFile); // 파일 압축
	 *    CompressUtil.zip(zipFile); // 디렉토리 압축
	 * </pre>
	 *
	 * @param file 압축 대상 파일 또는 디렉토리
	 * @throws SmartException
	 */
	public static void zip(File file) throws SmartException {
		zip(file, Charset.defaultCharset().name(), true);
	}

	/**
	 * 지정된 파일 또는 디렉토리를 압축한다. (지정된 디렉토리 포함 여부 지정)
	 * 
	 * <pre>
	 * 예)
	 *    String    folderPath  = "d:/test/zip"; 
	 *    <br>
	 *    File zipFolder    = new File(folderPath);
	 *    <br>
	 *    CompressUtil.zip(zipFile, true); // 디렉토리 압축 ( 디렉토리 포함 압축 )
	 *    CompressUtil.zip(zipFile, false); // 디렉토리 압축( 디렉토리 제외한 파일만 압축 )
	 * </pre>
	 *
	 * @param file 압축 대상 파일 또는 디렉토리
	 * @param includeSrc 디렉토리 포함 여부
	 * @throws SmartException
	 */
	public static void zip(File file, boolean includeSrc) throws SmartException {
		zip(file, Charset.defaultCharset().name(), includeSrc);
	}

	/**
	 * 지정된 파일 또는 디렉토리를 압축한다. (디렉토리 포함여부, 인코딩 지정)
	 * 
	 * <pre>
	 * 예)
	 *    String    folderPath  = "d:/test/zip"; 
	 *    <br>
	 *    File zipFolder    = new File(folderPath);
	 *    <br>
	 *    CompressUtil.zip(zipFile, "UTF-8", true); // 디렉토리 압축 ( UTF-8 인코딩 )
	 *    CompressUtil.zip(zipFile, "EUC-KR", true); // 디렉토리 압축( EUC-KR 인코딩 )
	 * </pre>
	 *
	 * @param file 압축 대상 파일 또는 디렉토리
	 * @param charset 압축 인코딩 타입
	 * @param includeSrc 디렉토리 포함 여부
	 * @throws SmartException
	 */
	public static void zip(File file, String charset, boolean includeSrc) throws SmartException {
		zip(file, file.getParentFile(), charset, includeSrc);
	}

	/**
	 * 지정된 파일 또는 디렉토리를 압축될 파일명을 지정하여 압축한다.
	 * 
	 * <pre>
	 * 예)
	 *    String    folderPath  = "d:/test/zip";        // 압축 대상 경로 
	 *    String    targetPath  = "d:/test/zip_test/zipTest.zip";   // 압축될 타겟 파일
	 *    <br>
	 *    File zipFolder    = new File(folderPath);
	 *    File zipTarget    = new File(targetPath);
	 *    <br>
	 *    OutputStream out = new FileOutputStream(new File(zipTarget));
	 *    <br>
	 *    CompressUtil.zip(zipFile, out); // 지정된 타켓파일로 압축됨
	 * </pre>
	 * 
	 * @param file 압축 대상 파일 또는 디렉토리
	 * @param out 압축될 파일
	 * @throws SmartException
	 */
	public static void zip(File file, OutputStream out) throws SmartException {
		zip(file, out, Charset.defaultCharset().name(), true);
	}

	/**
	 * 지정된 파일 또는 디렉토리를 압축될 디렉토리를 지정하여 압축한다.
	 * 
	 * <pre>
	 * 예)
	 *    String    folderPath  = "d:/test/zip";        // 압축 대상 경로 
	 *    String    targetPath  = "d:/test/zip_test";   // 압축될 타겟 디렉토리
	 *    <br>
	 *    File zipFolder    = new File(folderPath);
	 *    File zipTarget    = new File(targetPath);
	 *    <br>
	 *    
	 *    CompressUtil.zip(zipFile, zipTarget, "UTF-8", true); // 지정된 타켓디렉토리로 압축됨
	 * </pre>
	 *
	 * @param file 압축 대상 파일 또는 디렉토리
	 * @param toDir 압축될 타켓
	 * @param charset 압축 인코딩 타입
	 * @param includeSrc 디렉토리 포함 여부
	 * @throws SmartException
	 */
	public static void zip(File file, File toDir, String charset, boolean includeSrc) throws SmartException {
		try {
			String fileName = file.getName();
			if (!file.isDirectory()) {
				int pos = fileName.lastIndexOf(".");
				if (pos > 0) {
					fileName = fileName.substring(0, pos);
				}
			}
			fileName += ".zip";

			File zipFile = new File(toDir, fileName);
			if (!zipFile.exists()) {
				zipFile.createNewFile();
			}
			zip(file, new FileOutputStream(zipFile), charset, includeSrc);
		} catch (Exception e) {
			LogUtil.writeLog(e, CompressUtil.class);
			throw new SmartException(e);
		}
	}

	/**
	 * 지정된 파일 또는 디렉토리를 압축한다.
	 *
	 * <pre>
	 * 예)
	 *    String    folderPath  = "d:/test/zip";        // 압축 대상 경로 
	 *    String    targetPath  = "d:/test/zip_test/zipTest.zip";   // 압축될 타겟 파일
	 *    <br>
	 *    File zipFolder    = new File(folderPath);
	 *    File zipTarget    = new File(targetPath);
	 *    <br>
	 *    OutputStream out = new FileOutputStream(new File(zipTarget));
	 *    <br>
	 *    CompressUtil.zip(zipFile, out, "UTF-8", true); // 지정된 타켓파일로 압축됨
	 * </pre>
	 *
	 * @param file 압축 대상 파일 또는 디렉토리
	 * @param out 압축될 타켓
	 * @param charset 압축 인코딩 타입
	 * @param includeSrc 디렉토리 포함 여부
	 * @throws SmartException
	 */
	public static void zip(File file, OutputStream out, String charset, boolean includeSrc) throws SmartException {
		ZipArchiveOutputStream zipOut = null;

		try {
			zipOut = new ZipArchiveOutputStream(out);
			zipOut.setEncoding(charset);

			Stack<File> stack = new Stack<File>();
			File root = null;
			if (file.isDirectory()) {
				if (includeSrc) {
					stack.push(file);
					root = file.getParentFile();
				} else {
					File[] fs = file.listFiles();
					for (int i = 0; i < fs.length; i++) {
						stack.push(fs[i]);
					}
					root = file;
				}
			} else {
				stack.push(file);
				root = file.getParentFile();
			}

			FileInputStream in = null;
			byte[] buf = new byte[BUFF_SIZE];
			while (!stack.isEmpty()) {
				File f = stack.pop();
				String name = toPath(root, f);
				if (f.isDirectory()) {
					debug("dir: " + name);
					File[] fs = f.listFiles();
					for (int i = 0; i < fs.length; i++) {
						if (fs[i].isDirectory()) {
							stack.push(fs[i]);
						} else {
							stack.add(0, fs[i]);
						}
					}
				} else {
					debug("file: " + name);
					ZipArchiveEntry entry = new ZipArchiveEntry(name);
					zipOut.putArchiveEntry(entry);
					in = new FileInputStream(f);
					int length;
					while ((length = in.read(buf, 0, buf.length)) >= 0) {
						zipOut.write(buf, 0, length);
					}
					zipOut.flush();
					IOUtils.closeQuietly(in);
					zipOut.closeArchiveEntry();
				}
			}
		} catch (Exception e) {
			LogUtil.writeLog(e, CompressUtil.class);
			throw new SmartException(e);
		} finally {
			IOUtils.closeQuietly(zipOut);
			IOUtils.closeQuietly(out);
		}
	}

	private static String toPath(File root, File dir) {
		String path = dir.getAbsolutePath();
		path = path.substring(root.getAbsolutePath().length()).replace(File.separatorChar, '/');

		if (path.startsWith("/")) {
			path = path.substring(1);
		}

		if (dir.isDirectory() && !path.endsWith("/")) {
			path += "/";
		}

		return path;
	}

	private static void debug(String msg) {
		if (debug) {
			LogUtil.writeLog(msg, CompressUtil.class);
		}
	}
}
