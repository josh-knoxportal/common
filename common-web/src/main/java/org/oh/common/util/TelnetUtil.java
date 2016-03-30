package org.oh.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.net.telnet.TelnetClient;

/**
 * Telnet 유틸
 */
public class TelnetUtil {
	public static final int DEFAULT_PORT = 23;
	public static final int READ_BUFFER_SIZE = 1024;
	public static final String PROMPT_LOGIN = ": ";
	public static final String[][] OS_PROMPTS = { { "Solaris", "]" }, { "HP-UX", "# " }, { "AIX", "$ " },
			{ "Linux", "$ " }, { "Mac", "$ " }, { "Microsoft", ">" } };

	protected String server = null;
	protected int port = DEFAULT_PORT;
	protected String userName = null;
	protected String password = null;
	protected String osName = null;
	protected String terminalType = null;
	protected String charsetName = null;

	protected String promptChar = null;
	protected TelnetClient telnet = null;

	protected static String getPromptChar(String str) {
		if (str == null)
			return null;

		String promptChar = null;
		for (String[] osPrompt : OS_PROMPTS) {
			if (str.indexOf(osPrompt[0]) >= 0) {
				promptChar = osPrompt[1];
			}
		}

		return promptChar;
	}

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

	public void excuteCommand(String command) throws IOException {
		write(command);
		read(promptChar);
	}

	public void disconnect() throws IOException {
		LogUtil.writeLog(Utils.LINE_SEPARATOR + "[Disconnect server] " + server + ":" + port, getClass());
		telnet.disconnect();
	}

	protected void read(String pattern) throws IOException {
		read(pattern, false);
	}

	protected void read(String pattern, boolean isLogin) throws IOException {
		InputStream is = getInputStream();
		byte[] buff = new byte[READ_BUFFER_SIZE];
		int length = 0;
		String str = null;
		boolean end_loop = false;

		do {
			length = is.read(buff);
			if (length > 0) {
				str = (charsetName == null) ? new String(buff, 0, length)
						: new String(new String(buff, 0, length, charsetName).getBytes(), "UTF-8");
				System.out.print(str);
				if (str.endsWith(pattern))
					end_loop = true;

				if (isLogin && promptChar == null) {
					str = (Utils.isValidate(osName)) ? osName : str;
					promptChar = getPromptChar(str);
				}
			}
		} while (length > 0 && end_loop == false);
	}

	protected void write(String value) throws IOException {
		PrintStream ps = new PrintStream(getOutputStream());
		ps.println(value);
		ps.flush();
	}

	protected InputStream getInputStream() throws IOException {
		return telnet.getInputStream();
	}

	protected OutputStream getOutputStream() throws IOException {
		return telnet.getOutputStream();
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
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