package org.oh.common.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.oh.common.FunctionCallback;
import org.oh.common.exception.CommonException;
import org.oh.common.util.LogUtil;
import org.oh.common.util.ThreadUtils;
import org.oh.common.util.Utils;
import org.springframework.util.StopWatch;

/**
 * 배포작업
 */
public abstract class AbstractDeployTask extends Task {
	/**
	 * 소스경로
	 */
	protected String source_dir = null;

	/**
	 * 소스파일
	 */
	protected String source_file = null;

	/**
	 * 대상경로
	 */
	protected String target_dir = null;

	/**
	 * 병렬여부
	 */
	protected Boolean parallel_yn = false;

	/**
	 * 배포서버 목록
	 */
	protected List<DeployServer> deployServerList = new ArrayList<DeployServer>();

	public String getSource_dir() {
		return this.source_dir;
	}

	public void setSource_dir(String source_dir) {
		this.source_dir = source_dir;
	}

	public String getTarget_dir() {
		return this.target_dir;
	}

	public void setTarget_dir(String target_dir) {
		this.target_dir = target_dir;
	}

	public String getSource_file() {
		return this.source_file;
	}

	public void setSource_file(String source_file) {
		this.source_file = source_file;
	}

	public DeployServer getDeployServer(int index) {
		return deployServerList.get(index);
	}

	public void addDeployServer(DeployServer deployServer) {
		deployServerList.add(deployServer);
	}

	public Boolean getParallel_yn() {
		return parallel_yn;
	}

	public void setParallel_yn(Boolean parallel_yn) {
		this.parallel_yn = parallel_yn;
	}

	/**
	 * 작업 실행
	 */
	@Override
	public void execute() throws BuildException {
		StopWatch sw = new StopWatch("Deploy total");
		sw.start();

		String title = "";
		List<Future<Object>> futureList = new ArrayList<Future<Object>>();

		try {
			validate();

			for (DeployServer deployServer : deployServerList) {
				title = "Target \"" + deployServer.getServer_ip() + "\" server \"" + deployServer.getSystem_name()
						+ "\" system";

				if (!deployServer.getDeploy_yn())
					continue;

				if (parallel_yn) {
					FunctionCallback<Object[], Object> callback = new FunctionCallback<Object[], Object>() {
						@Override
						public Object executeTemplate(Object[] params) throws Exception {
							DeployServer deployServer = (DeployServer) params[0];
							String title = (String) params[1];

							deployTask(title, deployServer);

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

	protected void deployTask(String title, DeployServer deployServer) throws Exception {
		deployServer.validate();

		deploy(this, title, deployServer);
	}

	/**
	 * 유효성 검사
	 */
	protected void validate() throws BuildException {
		if (!Utils.isValidate(source_dir))
			throw new BuildException("Required source_dir attribute");
		else if (!Utils.isValidate(source_file))
			throw new BuildException("Required source_file attribute");
		else if (!Utils.isValidate(target_dir))
			throw new BuildException("Required target_dir attribute");
	}

	/**
	 * 프로그램 배포
	 */
	protected abstract void deploy(AbstractDeployTask deployTask, String title, DeployServer deployServer)
			throws CommonException;

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}