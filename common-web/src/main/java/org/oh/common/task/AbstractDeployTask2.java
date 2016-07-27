package org.oh.common.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.tools.ant.BuildException;
import org.oh.common.FunctionCallback;
import org.oh.common.exception.CommonException;
import org.oh.common.util.LogUtil;
import org.oh.common.util.ThreadUtils;
import org.springframework.util.StopWatch;

public abstract class AbstractDeployTask2 extends AbstractDeployTask {
	@Override
	public void execute() throws BuildException {
		super.execute();

		StopWatch sw = new StopWatch("Deploy2 total");
		sw.start();

		String title = "";
		List<Future<Object>> futureList = new ArrayList<Future<Object>>();

		try {
			for (DeployServer deployServer : deployServerList) {
				title = "Target \"" + deployServer.getServer_ip() + "\" server \"" + deployServer.getSystem_name()
						+ "\" system";

				if (!deployServer.getDeploy2_yn())
					continue;

				if (parallel_yn) {
					FunctionCallback<Object[], Object> callback = new FunctionCallback<Object[], Object>() {
						@Override
						public Object executeTemplate(Object[] params) throws Exception {
							DeployServer deployServer = (DeployServer) params[0];
							String title = (String) params[1];

							deployTask2(title, deployServer);

							return new HashMap<String, Object>();
						}
					};
					ThreadUtils.executeThread(futureList, callback, title, deployServer, title);
				} else {
					deployTask(title, deployServer);
				}
			}

			if (parallel_yn) {
				ThreadUtils.resultThread(futureList);
			}
		} catch (Exception e) {
			throw new BuildException(title + " error", e);
		}

		sw.stop();
		LogUtil.writeLog(sw.shortSummary(), getClass());
	}

	protected void deployTask2(String title, DeployServer deployServer) throws Exception {
		deploy2(this, title, deployServer);
	}

	/**
	 * 프로그램 배포2
	 */
	protected abstract void deploy2(AbstractDeployTask deployTask, String title, DeployServer deployServer)
			throws CommonException;
}