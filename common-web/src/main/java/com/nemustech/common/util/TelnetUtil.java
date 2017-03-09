package com.nemustech.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.telnet.TelnetClient;

/**
 * Telnet 유틸
 */
public class TelnetUtil extends AbstractNet {
	protected TelnetClient telnet = null;

	public TelnetUtil() {
	}

	public TelnetUtil(String server, String userName, String password) throws IOException {
		this(server, userName, password, null);
	}

	public TelnetUtil(String server, String userName, String password, String osName) throws IOException {
		this(server, userName, password, osName, null);
	}

	public TelnetUtil(String server, String userName, String password, String osName, String charsetName)
			throws IOException {
		this(server, userName, password, osName, null, charsetName);
	}

	public TelnetUtil(String server, String userName, String password, String osName, String terminalType,
			String charsetName) throws IOException {
		this(server, -1, userName, password, osName, null, null);
	}

	public TelnetUtil(String server, int port, String userName, String password, String osName) throws IOException {
		this(server, port, userName, password, osName, null, null);
	}

	public TelnetUtil(String server, int port, String userName, String password, String osName, String terminalType,
			String charsetName) throws IOException {
		this.server = server;
		if (port == -1)
			port = this.port;
		else
			this.port = port;
		this.userName = userName;
		this.password = password;
		this.osName = osName;

		if (osName != null && "Microsoft".equals(osName) && terminalType == null && charsetName == null) {
			terminalType = "dumb";
			charsetName = "EUC-KR";
		}
		this.terminalType = terminalType;
		this.charsetName = charsetName;

		telnet = (terminalType == null) ? new TelnetClient() : new TelnetClient(terminalType);

		LogUtil.writeLog("[Connect server] " + server + ":" + port, getClass());
		telnet.connect(server, port);

		LogUtil.writeLog("[Login] " + userName, getClass());
		read(PROMPT_LOGIN, true);
		if (promptChar == null) {
			disconnect();
			throw new IOException("[ERROR] Unknown system OS.");
		}

		write(userName);
		read(PROMPT_LOGIN);

		write(password);
		read(promptChar);
	}

	@Override
	public void disconnect() throws IOException {
		LogUtil.writeLog(Utils.LINE_SEPARATOR + "[Disconnect server] " + server + ":" + port, getClass());
		telnet.disconnect();
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return telnet.getInputStream();
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		return telnet.getOutputStream();
	}

	public TelnetClient getTelnet() {
		return telnet;
	}

	public static void main(String[] args) throws Exception {
		String server = "192.168.3.115";
		String userName = "skoh";
		String password = "skoh";
		String os_name = "Microsoft";

		TelnetUtil telnet = new TelnetUtil(server, userName, password, os_name);
		telnet.excuteCommand("dir");

		telnet.excuteCommand("exit");
		telnet.disconnect();
	}
}