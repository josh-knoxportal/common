package com.nemustech.common.task;

import java.io.File;

import com.nemustech.common.exception.CommonException;
import com.nemustech.common.util.FTPUtil;
import com.nemustech.common.util.JeusUtils;

/**
 * 제우스 서버 배포
 */
public class JeusDeployTask extends AbstractDeployTask {
	@Override
	protected void upload(AbstractDeployTask deployTask, String title, DeployServer deployServer)
			throws CommonException {
		try {
			log("---------- " + title + " ----------");

			log("--- Sending the war file to \"" + deployServer.getServer_ip() + "\"");
			FTPUtil ftp = new FTPUtil(deployServer.getServer_ip(), deployServer.getServer_port(),
					deployServer.getUser_id(), deployServer.getUser_pw());

			String target_full_dir = target_dir + File.separator + system_name + File.separator + target_path;
			ftp.backup(target_full_dir, source_file);
			ftp.upload(source_dir, source_file, target_full_dir);
			ftp.disconnect();
		} catch (Exception e) {
			throw new CommonException(CommonException.ERROR, e.getMessage(), e);
		}
	}

	@Override
	protected void restart(AbstractDeployTask deployTask, String title, DeployServer deployServer)
			throws CommonException {
		try {
			log("--- Restarting the WAS container \"" + system_name + "\"");
			JeusUtils jeus = new JeusUtils(deployServer.getServer_ip(), deployServer.getServer_port(),
					deployServer.getUser_id(), deployServer.getUser_pw(), deployServer.getOs_name());
			jeus.excuteJeus("ja");
			jeus.excuteJeus("downcon " + system_name);
			jeus.excuteJeus("startcon " + system_name);
			jeus.excuteJeus("exit");
			jeus.excuteCommand("exit");
			jeus.disconnect();
		} catch (Exception e) {
			throw new CommonException(CommonException.ERROR, e.getMessage(), e);
		}
	}
}