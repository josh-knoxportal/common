package org.oh.common.task;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.tools.ant.BuildException;
import org.oh.common.util.Utils;

/**
 * 배포서버
 */
public class DeployServer {
	/**
	 * 배포여부
	 */
	protected Boolean deploy_yn = false;

	/**
	 * 배포2여부
	 */
	protected Boolean deploy2_yn = true;

	/**
	 * 서버IP
	 */
	protected String server_ip = null;

	/**
	 * 서버 Port
	 */
	protected int server_port = -1;

	/**
	 * 사용자ID
	 */
	protected String user_id = null;

	/**
	 * 사용자암호
	 */
	protected String user_pw = null;

	/**
	 * 시스템명
	 */
	protected String system_name = null;

	/**
	 * OS명 (Solaris, HP-UX, AIX, Linux, Mac, Microsoft)
	 */
	protected String os_name = null;

	/**
	 * 터미널 타입
	 */
	protected String terminal_type = null;

	/**
	 * 문자 집합
	 */
	protected String charset_name = null;

	public Boolean getDeploy_yn() {
		return this.deploy_yn;
	}

	public void setDeploy_yn(Boolean deploy_yn) {
		this.deploy_yn = deploy_yn;
	}

	public Boolean getDeploy2_yn() {
		return deploy2_yn;
	}

	public void setDeploy2_yn(Boolean deploy2_yn) {
		this.deploy2_yn = deploy2_yn;
	}

	public String getServer_ip() {
		return this.server_ip;
	}

	public void setServer_ip(String server_ip) {
		this.server_ip = server_ip;
	}

	public int getServer_port() {
		return server_port;
	}

	public void setServer_port(int server_port) {
		this.server_port = server_port;
	}

	public String getUser_id() {
		return this.user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_pw() {
		return this.user_pw;
	}

	public void setUser_pw(String user_pw) {
		this.user_pw = user_pw;
	}

	public String getSystem_name() {
		return this.system_name;
	}

	public void setSystem_name(String system_name) {
		this.system_name = system_name;
	}

	public String getOs_name() {
		return this.os_name;
	}

	public void setOs_name(String os_name) {
		this.os_name = os_name;
	}

	public String getTerminal_type() {
		return terminal_type;
	}

	public void setTerminal_type(String terminal_type) {
		this.terminal_type = terminal_type;
	}

	public String getCharset_name() {
		return charset_name;
	}

	public void setCharset_name(String charset_name) {
		this.charset_name = charset_name;
	}

	/**
	 * 유효성 검사
	 */
	protected void validate() throws BuildException {
		if (!Utils.isValidate(deploy_yn))
			throw new BuildException("Required deploy_yn attribute");
		if (!Utils.isValidate(server_ip))
			throw new BuildException("Required server_ip attribute");
		if (!Utils.isValidate(user_id))
			throw new BuildException("Required user_id attribute");
		if (!Utils.isValidate(user_pw))
			throw new BuildException("Required user_pw attribute");
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}