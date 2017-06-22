package com.nemustech.platform.lbs.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SftpUploader {
	/**
	 * 서버와 연결하여 파일을 업로드하고, 다운로드한다.
	 *
	 * @author haneulnoon
	 * @since 2009-09-10
	 *
	 */
	private Session session = null;

	private Channel channel = null;

	private ChannelSftp channelSftp = null;

	/**
	 * 서버와 연결에 필요한 값들을 가져와 초기화 시킴
	 *
	 * @param host
	 *            서버 주소
	 * @param userName
	 *            접속에 사용될 아이디
	 * @param password
	 *            비밀번호
	 * @param port
	 *            포트번호
	 */
	public void init(String host, String userName, String password, int port) {
		JSch jsch = new JSch();
		try {
			session = jsch.getSession(userName, host, port);
			session.setPassword(password);

			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();

			channel = session.openChannel("sftp");
			channel.connect();
		} catch (JSchException e) {
			e.printStackTrace();
		}

		channelSftp = (ChannelSftp) channel;

	}

	/**
	 * 하나의 파일을 업로드 한다.
	 *
	 * @param dir
	 *            저장시킬 주소(서버)
	 * @param file
	 *            저장할 파일
	 */
	public void upload(String dir, File file) {

		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			channelSftp.cd(dir);
			channelSftp.put(in, file.getName());
		} catch (SftpException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 하나의 파일을 다운로드 한다.
	 *
	 * @param dir
	 *            저장할 경로(서버)
	 * @param downloadFileName
	 *            다운로드할 파일
	 * @param path
	 *            저장될 공간
	 */
	public void download(String dir, String downloadFileName, String path, String download_uri) {
		InputStream in = null;
		FileOutputStream out = null;
		try {
			channelSftp.cd(dir);
			in = channelSftp.get(downloadFileName);
		} catch (SftpException e) {

			e.printStackTrace();
		}

		try {
			File file = new File(download_uri);
			if (!file.exists() || file.isFile()) {
				file.mkdirs();
			}
			File destFile = new File(file, downloadFileName);

			System.out.println("savepath = " + dir);
			System.out.println("dstName = " + downloadFileName);
			System.out.println("destFile = " + destFile.getAbsolutePath());

			out = new FileOutputStream(destFile);
			int i;

			while ((i = in.read()) != -1) {
				out.write(i);
			}
		} catch (IOException e) {

			e.printStackTrace();
		} finally {
			try {
				out.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * 하나의 파일을 삭제 한다.
	 *
	 * @param dir
	 *            저장할 경로(서버)
	 * @param downloadFileName
	 *            다운로드할 파일
	 * @param path
	 *            저장될 공간
	 */
	public boolean delete(String dir, String fileName) {
		boolean result = true;
		try {

			System.out.println("[delete] dir >>>>> " + dir);
			System.out.println("[delete] fileName >>>>> " + fileName);

			channelSftp.cd(dir);
			channelSftp.rm(fileName);
		} catch (SftpException e) {

			e.printStackTrace();
			result = false;
		}

		return result;
	}

	/**
	 * 서버와의 연결을 끊는다.
	 */
	public void disconnection() {
		channelSftp.quit();

	}
}
