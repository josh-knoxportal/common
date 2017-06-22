package com.nemustech.platform.lbs.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.mozilla.universalchardet.UniversalDetector;
import org.springframework.web.multipart.MultipartFile;

public final class FileUtil {

	public static boolean exists(final String str) {
		try {
			final File fp = new File(str);
			return fp.exists();
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean save(final String str, final byte[] by) {
		try {
			File file = new File(str);
			File dir = file.getParentFile();
			if(!dir.exists()) {
				dir.mkdirs();
			}
			final FileOutputStream fos = new FileOutputStream(file);
			fos.write(by);
			fos.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean save(final String path, final String fname, final byte[] by) {
		try {
			File dir = new File(path);
			if(!dir.exists()) {
				dir.mkdirs();
			}
			File file = new File(dir, fname);
			final FileOutputStream fos = new FileOutputStream(file);
			fos.write(by);
			fos.close();
			return true;
		} catch(Exception e) {
			return false;
		}
	}

	public static byte[] load(final String str) {
		byte[] by;

		try {
			final File fp = new File(str);
			final FileInputStream fis = new FileInputStream(fp);

			by = new byte[(int) fp.length()];
			fis.read(by);
			fis.close();
		} catch (Exception e) {
			by = null;
		}
		return by;
	}

	public static boolean append(final String str, final byte[] by) {
		return append(str, by, true);
	}

	/**
	 * bPos : if true, then bytes will be written to the end of the file rather than the beginning
	 */
	public static boolean append(final String str, final byte[] by, final boolean bPos) {
		try {
			final FileOutputStream fos = new FileOutputStream(str, bPos);
			fos.write(by);
			fos.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean copy(final String strSource, final String strTarget) {
		final File fileSource = new File(strSource);
		File fileTarget = new File(strTarget);

		if (!fileSource.exists()) {
			return false;
		}

		if (!fileSource.isFile()) {
			return false;
		}

		if (!fileSource.canRead()) {
			return false;
		}

		if (fileTarget.isDirectory()) {
			fileTarget = new File(fileTarget, fileSource.getName());
		}

		if (fileTarget.exists()) {
			return false;
		}

		FileInputStream in = null;
		FileOutputStream out = null;
		boolean bResult = true;

		try {
			in = new FileInputStream(fileSource);
			out = new FileOutputStream(fileTarget);

			final byte[] buffer = new byte[1024];
			int nBytes;

			while ((nBytes = in.read(buffer)) != -1) {
				out.write(buffer, 0, nBytes);
			}
		} catch (FileNotFoundException e) {
			bResult = false;
		} catch (IOException e) {
			bResult = false;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException ioe) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException ioe) {
				}
			}
		}

		return bResult;
	}
	
	public static String getFileExt(File f) {
		String fname = f.getName();
		return getFileExt(fname);
	}
	
	public static String getFileExt(String fname) {
		String ext = null;
		int idx = fname.lastIndexOf(".");
		if(idx != -1) {
			ext = fname.substring(idx+1);
		}
		return ext;
	}
	
	public static String setFileExt(String fname, String ext) {
		String newName = "";
		int idx = fname.lastIndexOf(".");
		if( idx != -1 ) {
			newName = fname.substring(0, idx+1) + ext;
		}
		return newName;
	}

	public static String findCharSet(MultipartFile attachFile) throws Exception {
		InputStream is = attachFile.getInputStream();
		byte[] buf = new byte[4096];

		UniversalDetector ud = new UniversalDetector(null);
		
		int nread;
		
		while ((nread = is.read(buf)) > 0 && !ud.isDone()) {
			ud.handleData(buf, 0, nread);
		}
		
		ud.dataEnd();
		
		String encoding = ud.getDetectedCharset();
		
		ud.reset();
		return encoding;
	}
	
	public static String convertToUTF8(String str, String charset) throws Exception {
		return new String(str.getBytes(charset), charset);
	}
}
