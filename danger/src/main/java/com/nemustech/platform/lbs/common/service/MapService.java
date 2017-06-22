package com.nemustech.platform.lbs.common.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nemustech.platform.lbs.common.mapper.MapApiMapper;
import com.nemustech.platform.lbs.common.vo.DateVo;
import com.nemustech.platform.lbs.common.vo.MapVo;

//db insert, 상태 update ...

@Service(value = "mapService")
public class MapService {
	@Autowired
	private MapApiMapper mapApiMapper;

	public boolean getIsLastModifiedMap(String map_uid, String if_modified_since) throws Exception {
		DateVo dateVo = mapApiMapper.getLastModifiedMap(map_uid);
		DateFormat sdFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", new Locale("en", "US"));
		Date param = sdFormat.parse(if_modified_since);

		// logger.info("date1 : " + naviMapVo.getUpd_date());
		// logger.info("date2 : " + param);
		if (dateVo.getUpd_date().after(param) == true) {
			// logger.info("date2 true ");
			return true;
		}
		// logger.info("date2 false ");
		return false;
	}

	// @Cacheable(value = "mapCache", key = "#map_uid")
	public MapVo selectMap(String map_uid) throws Exception {
		return mapApiMapper.selectMap(map_uid);
	}

}
