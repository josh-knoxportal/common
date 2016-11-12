package com.nemustech.common.util;

import java.io.IOException;

/**
 * Jeus 유틸
 */
public class JeusUtils extends TelnetUtil {
	public static final String PROMPT_JEUS = ">";

	public JeusUtils(String server, String username, String password, String osname) throws IOException {
		super(server, username, password, osname);
	}

	public JeusUtils(String server, int port, String username, String password, String osname) throws IOException {
		super(server, port, username, password, osname);
	}

	public void excuteJeus(String command) throws IOException {
		write(command);
		if ("exit".equals(command))
			read(promptChar);
		else
			read(PROMPT_JEUS);
	}

	public static void main(String[] args) throws Exception {
		String server = "192.168.3.115";
		String system = "skoh1";
		String username = "skoh";
		String password = "skoh";

		JeusUtils jeusUtils = new JeusUtils(server, username, password, "HP-UX");
		jeusUtils.excuteJeus("ja");
		jeusUtils.excuteJeus("downcon " + system);
		jeusUtils.excuteJeus("startcon " + system);
		jeusUtils.excuteJeus("exit");
		jeusUtils.excuteCommand("exit");
		jeusUtils.disconnect();
	}
}