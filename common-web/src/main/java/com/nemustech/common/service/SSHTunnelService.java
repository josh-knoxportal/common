package com.nemustech.common.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.JSchException;
import com.nemustech.common.util.PropertyUtils;
import com.nemustech.common.util.SSHUtil;

/**
 * SSH 프로토콜을 사용하여 원격 서버의 다른 포트를 접근하고자 할때 사용
 * 
 * @author skoh
 */
@Service
public class SSHTunnelService implements InitializingBean, DisposableBean {
	protected Log log = LogFactory.getLog(getClass());

	protected SSHUtil ssh = null;

	@Override
	public void afterPropertiesSet() throws Exception {
		if ("true".equals(System.getProperty("ssh.tunnel", "false"))) {
			PropertyUtils property = PropertyUtils.getInstance();
			ssh = new SSHUtil(property.getString("ssh.server"), property.getInt("ssh.port"),
					property.getString("ssh.user"), property.getString("ssh.password"));

			try {
				ssh.getSession().setPortForwardingL(property.getInt("ssh.local.port"), "localhost",
						property.getInt("ssh.remote.port"));
			} catch (JSchException e) {
				log.warn(e.getMessage() + ": " + ssh.toString());
			}
		}
	}

	@Override
	public void destroy() throws Exception {
		if (ssh != null)
			ssh.disconnect();
	}
}
