package com.nemustech.common.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.JSchException;
import com.nemustech.common.util.PropertyUtils;
import com.nemustech.common.util.SSHUtil;
import com.nemustech.common.util.StringUtil;
import com.nemustech.common.util.Utils;

/**
 * SSH 프로토콜을 사용하여 원격 서버의 다른 포트를 접근하고자 할때 사용
 * 
 * <pre>
 * <사용법>
 * 1. resources/common.properties 에 prefix1.ssh.* 추가
 * 2. java -Dssh.tunnel.prefixs=prefix1,...
 * </pre>
 * 
 * @author skoh
 */
@Service
public class SSHTunnelService {
	protected Log log = LogFactory.getLog(getClass());

	protected List<SSHUtil> sshList = new ArrayList<>();

	@PostConstruct
	public void initSSH_() throws Exception {
		String prefixs = System.getProperty("ssh.tunnel.prefixs");
		if (Utils.isValidate(prefixs)) {
			PropertyUtils property = PropertyUtils.getInstance();
			String[] arrPrefix = StringUtil.split(prefixs, ',');
			for (String prefix : arrPrefix) {
				log.info("ssh.tunnel.prefix: " + prefix);
				SSHUtil sshUtil = new SSHUtil(property.getString(prefix + ".ssh.server"),
						property.getInt(prefix + ".ssh.port"), property.getString(prefix + ".ssh.user"),
						property.getString(prefix + ".ssh.password"));

				try {
					sshUtil.getSession().setPortForwardingL(property.getInt(prefix + ".ssh.local.port"), "localhost",
							property.getInt(prefix + ".ssh.remote.port"));
				} catch (JSchException e) {
					log.warn(e.getMessage() + ": " + sshUtil.toString());
				}

				sshList.add(sshUtil);
			}
		}
	}

	@PreDestroy
	public void close() {
		for (SSHUtil sshUtil : sshList) {
			if (sshUtil != null)
				sshUtil.disconnect();

		}
	}

}
