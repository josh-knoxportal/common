package com.nemustech.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SSHUtil extends AbstractNetUtil {
	public static final int DEFAULT_PORT = 22;

	protected int port = DEFAULT_PORT;

	protected JSch jsch = null;
	protected Session session = null;
	protected ChannelShell channelShell = null;

	public SSHUtil(String server, String userName, String password, String osName) throws JSchException {
		this(server, DEFAULT_PORT, userName, password, osName);
	}

	public SSHUtil(String server, int port, String userName, String password) throws JSchException {
		this(server, port, userName, password, null);
	}

	public SSHUtil(String server, int port, String userName, String password, String osName) throws JSchException {
		this.server = server;
		if (port == -1)
			port = this.port;
		else
			this.port = port;
		this.userName = userName;
		this.password = password;
		this.osName = osName;

		LogUtil.writeLog("[Connect server] " + server + ":" + port, getClass());
		jsch = new JSch();
		session = jsch.getSession(userName, server, port);
		session.setPassword(password);

		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		session.connect();

		if (osName != null) {
			channelShell = (ChannelShell) session.openChannel("shell");

			LogUtil.writeLog("[Login] " + userName, getClass());
			channelShell.connect();

			promptChar = getPromptChar(osName);
		}
	}

	@Override
	public void disconnect() {
		LogUtil.writeLog(Utils.LINE_SEPARATOR + "[Disconnect server] " + server + ":" + port, getClass());
		if (channelShell != null)
			channelShell.disconnect();
		if (session != null)
			session.disconnect();
	}

	@Override
	protected InputStream getInputStream() throws IOException {
		if (channelShell != null)
			return channelShell.getInputStream();
		else
			return null;
	}

	@Override
	protected OutputStream getOutputStream() throws IOException {
		if (channelShell != null)
			return channelShell.getOutputStream();
		else
			return null;
	}

	public JSch getJsch() {
		return jsch;
	}

	public Session getSession() {
		return session;
	}

	public ChannelShell getChannelShell() {
		return channelShell;
	}

	public static void main(String[] arg) throws Exception {
		String server = "112.217.207.164";
		int port = 20022;
		String userName = "oracle";
		String password = "nemustech";
		String os_name = "Linux";

		SSHUtil ssh = new SSHUtil(server, port, userName, password, os_name);
		ssh.excuteCommand("cd /was/aams/bin");
		ssh.excuteCommand("ls --color=no");

		ssh.excuteCommand("exit");
		ssh.disconnect();
	}
}
