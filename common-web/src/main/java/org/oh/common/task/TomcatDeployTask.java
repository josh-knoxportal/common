package org.oh.common.task;

import org.oh.common.exception.CommonException;
import org.oh.common.util.FTPUtil;
import org.oh.common.util.FileUtil;
import org.oh.common.util.LogUtil;
import org.oh.common.util.TelnetUtil;
import org.oh.common.util.ThreadUtils;

/**
 * 톰캣 서버 배포
 */
public class TomcatDeployTask extends AbstractDeployTask {
	@Override
	protected void deploy(AbstractDeployTask deployTask, String title, DeployServer deployServer)
			throws CommonException {
		try {
			log("---------- " + title + " ----------");
			log("Sending the war file to \"" + deployServer.getServer_ip() + "\"");
			FTPUtil ftp = new FTPUtil(deployServer.getServer_ip(), deployServer.getServer_port(),
					deployServer.getUser_id(), deployServer.getUser_pw());
			ftp.backup(target_dir, source_file);
			ftp.upload(source_dir, source_file, target_dir);
			ftp.disconnect();

			log("Restarting the WAS container \"" + deployServer.getSystem_name() + "\"");
			TelnetUtil telnet = new TelnetUtil(deployServer.getServer_ip(), deployServer.getServer_port(),
					deployServer.getUser_id(), deployServer.getUser_pw(), deployServer.getOs_name(),
					deployServer.getTerminal_type(), deployServer.getCharset_name());
			telnet.excuteCommand("cd \\was\\" + deployServer.getSystem_name() + "\\bin");
			telnet.excuteCommand("shutdown");

			telnet.excuteCommand("cd " + target_dir);
			telnet.excuteCommand("rm -r " + FileUtil.getBaseName(deployTask.getSource_file()));

			telnet.excuteCommand("cd \\was\\" + deployServer.getSystem_name() + "\\bin");
			telnet.excuteCommand("startup");

			telnet.excuteCommand("exit");
			telnet.disconnect();
		} catch (Exception e) {
			throw new CommonException(CommonException.ERROR,LogUtil.buildMessage(toString(), e.getMessage()), e);
		}
	}

	public static void main(String[] args) throws Exception {
		String name = "common-web";
		String version = "1.0";

		String os_name = "Microsoft";
		String system_name = "skoh";
		String source_dir = "target";
		String target_dir = "target";
		String source_file = name + "-" + version + ".war";

		String server_ip = "192.168.3.115";
		String user_id = "skoh";
		String user_pw = "skoh";

		try {
			TomcatDeployTask task = new TomcatDeployTask();
			task.setSource_dir(source_dir);
			task.setTarget_dir(target_dir);
			task.setSource_file(source_file);

			DeployServer deployServer = new DeployServer();
			deployServer.setDeploy_yn(true);
			deployServer.setOs_name(os_name);
			deployServer.setServer_ip(server_ip);
			deployServer.setSystem_name(system_name);
			deployServer.setUser_id(user_id);
			deployServer.setUser_pw(user_pw);
			task.addDeployServer(deployServer);

			task.execute();
		} finally {
			ThreadUtils.shutdownThreadPool();
		}
	}
}