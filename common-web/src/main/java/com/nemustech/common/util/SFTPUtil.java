package com.nemustech.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Vector;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SFTPUtil {
	public static final int DEFAULT_PORT = 22;

	protected JSch jsch = new JSch();

	protected Session session = null;
	protected ChannelSftp channelSftp = null;

	protected String server = null;
	protected int port = DEFAULT_PORT;
	protected String userName = null;
	protected String password = null;

	public SFTPUtil(String server, String userName, String password) throws JSchException {
		this(server, -1, userName, password);
	}

	public SFTPUtil(String server, int port, String userName, String password) throws JSchException {
		this.server = server;
		if (port == -1)
			port = this.port;
		else
			this.port = port;
		this.userName = userName;
		this.password = password;

		LogUtil.writeLog("[Connect server] " + server + ":" + port, getClass());
		session = jsch.getSession(userName, server, port);
		session.setPassword(password);

		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		session.connect();

		channelSftp = (ChannelSftp) session.openChannel("sftp");

		LogUtil.writeLog("[Login] " + userName, getClass());
		channelSftp.connect();
	}

	public void upload(String sourceDir, String sourceFile, String targetDir)
			throws FileNotFoundException, IOException, SftpException {
		upload(sourceDir, sourceFile, targetDir, false);
	}

	public void upload(String sourceDir, String sourceFile, String targetDir, boolean list)
			throws FileNotFoundException, IOException, SftpException {
		String pwd = channelSftp.pwd();
		channelSftp.cd(targetDir);

		LogUtil.writeLog("[Upload file] " + targetDir + File.separator + sourceFile, getClass());
		InputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(sourceDir + File.separator + sourceFile));
			channelSftp.put(in, sourceFile);
		} finally {
			if (in != null)
				in.close();
		}
		LogUtil.writeLog("[Upload complete]", getClass());

		if (list) {
			Vector<ChannelSftp.LsEntry> files = channelSftp.ls(".");
			for (ChannelSftp.LsEntry file : files) {
				LogUtil.writeLog(file, getClass());
			}
		}

		channelSftp.cd(pwd);
	}

	public void download(String sourceDir, String sourceFile, String targetDir) throws IOException, SftpException {
		String pwd = channelSftp.pwd();
		channelSftp.cd(targetDir);

		LogUtil.writeLog("[Download file] " + targetDir + File.separator + sourceFile, getClass());
		InputStream in = channelSftp.get(sourceFile);
		OutputStream out = null;
		try {
			out = new BufferedOutputStream(new FileOutputStream(sourceDir + File.separator + sourceFile));
			int i;
			while ((i = in.read()) != -1) {
				out.write(i);
			}
		} finally {
			if (out != null)
				out.close();
			if (in != null)
				in.close();
		}
		LogUtil.writeLog(" [Download complete]", getClass());

		channelSftp.cd(pwd);
	}

	public void backup(String targetDir, String sourceFile) throws SftpException {
		String pwd = channelSftp.pwd();
		channelSftp.cd(targetDir);

		LogUtil.writeLog("[Backup file] " + targetDir + File.separator + sourceFile, getClass());
		try {
			channelSftp.rename(sourceFile, sourceFile + "_" + StringUtil.getDateTimeHypen());
		} catch (SftpException e) {
			LogUtil.writeLog(e, getClass());
		}

		channelSftp.cd(pwd);
	}

	public void disconnect() {
		LogUtil.writeLog("[Disconnect server] " + server + ":" + port, getClass());
		channelSftp.disconnect();
		session.disconnect();
	}

	public static void main(String[] args) throws Exception {
		String server = "112.217.207.164";
		String userName = "oracle";
		String password = "nemustech";

		String sourceDir = "target";
		String targetDir = "/was/cbms/webapps";
		String sourceFile = "CompanyBrandManageService-1.0.1-SNAPSHOT.war";

		SFTPUtil ftp = new SFTPUtil(server, 20022, userName, password);
		ftp.backup(targetDir, sourceFile);
		ftp.upload(sourceDir, sourceFile, targetDir);
		ftp.disconnect();
	}
}
