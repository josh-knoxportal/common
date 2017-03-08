package com.nemustech.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * Net 유틸
 */
public abstract class AbstractNetUtil {
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

	public abstract void disconnect() throws IOException;

	protected abstract InputStream getInputStream() throws IOException;

	protected abstract OutputStream getOutputStream() throws IOException;

	public void excuteCommand(String command) throws IOException {
		write(command);
		read(promptChar);
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

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}