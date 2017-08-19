package com.nemustech.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/**
 * FTP 유틸
 */
public class FTPUtil {
	public static final int DEFAULT_PORT = 21;

	protected FTPClient ftp = new FTPClient();

	protected String server = null;
	protected int port = DEFAULT_PORT;
	protected String userName = null;
	protected String password = null;

	public FTPUtil(String server, String userName, String password) throws IOException {
		this(server, -1, userName, password);
	}

	public FTPUtil(String server, int port, String userName, String password) throws IOException {
		this.server = server;
		if (port == -1)
			port = this.port;
		else
			this.port = port;
		this.userName = userName;
		this.password = password;

		LogUtil.writeLog("[Connect server] " + server + ":" + port, getClass());
		ftp.connect(server, port);

		int replyCode = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(replyCode)) {
			disconnect();
			throw new IOException("[ERROR] Exception in connecting to FTP Server.");
		}

		LogUtil.writeLog("[Login] " + userName, getClass());
		ftp.login(userName, password);
	}

	public void upload(String sourceDir, String sourceFile, String targetDir) throws IOException {
		upload(sourceDir, sourceFile, targetDir, false);
	}

	public void upload(String sourceDir, String sourceFile, String targetDir, boolean list) throws IOException {
		ftp.changeWorkingDirectory(targetDir);
		ftp.setFileType(FTP.BINARY_FILE_TYPE);

		LogUtil.writeLog("[Upload file] " + targetDir + File.separator + sourceFile, getClass());
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(sourceDir + File.separator + sourceFile));
			ftp.storeFile(sourceFile, bis);
		} finally {
			IOUtils.closeQuietly(bis);
		}
		LogUtil.writeLog("[Upload complete]", getClass());

		if (list) {
			FTPFile[] ftpFiles = ftp.listFiles();
			for (FTPFile ftpFile : ftpFiles)
				LogUtil.writeLog(ftpFile, getClass());
		}
	}

	public void download(String sourceDir, String targetDir, String sourceFile) throws IOException {
		ftp.changeWorkingDirectory(targetDir);
		ftp.setFileType(FTP.BINARY_FILE_TYPE);

		LogUtil.writeLog("[Download file] " + targetDir + File.separator + sourceFile, getClass());
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(sourceDir + File.separator + sourceFile));
			ftp.retrieveFile(sourceFile, bos);
		} finally {
			IOUtils.closeQuietly(bos);
		}
		LogUtil.writeLog(" [Download complete]", getClass());
	}

	public void backup(String targetDir, String sourceFile) throws IOException {
		LogUtil.writeLog("[Backup file] " + targetDir + File.separator + sourceFile, getClass());
		ftp.changeWorkingDirectory(targetDir);
		ftp.rename(sourceFile, sourceFile + "_" + StringUtil.getDateTimeUnderline());
	}

	public void disconnect() throws IOException {
		LogUtil.writeLog("[Logout] " + userName, getClass());
		ftp.logout();

		LogUtil.writeLog("[Disconnect server] " + server + ":" + port, getClass());
		ftp.disconnect();
	}

	public static void main(String[] args) throws Exception {
		String server = "192.168.3.115";
		String userName = "skoh";
		String password = "skoh";

		String sourceDir = "target";
		String targetDir = "was/cbms/webapps";
		String sourceFile = "common-web-1.0.jar";

		FTPUtil ftp = new FTPUtil(server, userName, password);
		ftp.backup(targetDir, sourceFile);
		ftp.upload(sourceDir, sourceFile, targetDir);
		ftp.disconnect();
	}
}