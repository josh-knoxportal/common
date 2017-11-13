package com.nemustech.common.service.impl;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import com.nemustech.common.model.Server;
import com.nemustech.common.service.CacheService;
import com.nemustech.common.util.NetUtil;
import com.nemustech.common.util.Utils;

/**
 * 스케쥴러 이중화
 * 
 * @author skoh
 */
public class SchedulerDualServiceImpl extends CacheServiceImpl {
	/**
	 * 이중화 사용 여부
	 */
	@Value("${scheduler.dual.enable}")
	protected boolean scheduler_dual_enable = false;

	/**
	 * 이중화 시간 간격(초)
	 */
	@Value("${scheduler.dual.interval}")
	protected int scheduler_dual_interval = 10;

	/**
	 * 로컬 IP
	 */
	protected String localIp = NetUtil.getLocalIp();

	/**
	 * 로컬 Port
	 */
	protected int localPort = NetUtil.getTomcatPort();

	/**
	 * Active Profile
	 */
	@Value("${spring.profiles.active:default}")
	protected String activeProfile;

	@Override
	public String getCacheName() {
		return CacheService.CACHE_NAME_SYNC;
	}

	/**
	 * 이중화 초기화
	 * 
	 * @throws Exception
	 */
	@PostConstruct
	public void initDual_() throws Exception {
		if (!isActiveCache())
			return;

		String cacheKey = makeClassCacheKey();
		Server server = getCache(cacheKey, Server.class);
		if (server == null) {
			server = new Server(localIp, localPort, activeProfile, new Date());
			log.debug("server: " + server);
			putCache(cacheKey, server);
		}
	}

	/**
	 * 이중화 스케쥴링
	 * 
	 * @throws Exception
	 */
	@Scheduled(fixedRateString = "${scheduler.dual.period}")
	public void schedulingDual() throws Exception {
		if (!scheduler_dual_enable)
			return;

		String cacheKey = makeClassCacheKey();
		Server server = getCache(cacheKey, Server.class);
		if (server == null)
			return;

		int interval = Utils.getIntervalSecond(server.getLast_date(), new Date());
		if (isRun(cacheKey, server)) {
			server.setLast_date(new Date());
			putCache(cacheKey, server);
		} else if (interval >= scheduler_dual_interval) {
			server.setIp(localIp);
			server.setPort(localPort);
			server.setLast_date(new Date());
			putCache(cacheKey, server);
		}
		log.trace("server: " + server);
	}

	protected boolean isRun() {
		String cacheKey = makeClassCacheKey();
		Server server = getCache(cacheKey, Server.class);

		return isRun(cacheKey, server);
	}

	/**
	 * 이중화 실행 여부
	 * 
	 * @param cacheKey
	 * @param server
	 * @return
	 */
	protected boolean isRun(String cacheKey, Server server) {
		return (localIp.equals(server.getIp()) && localPort == server.getPort()
				&& activeProfile.equals(server.getProfile()));
	}
}