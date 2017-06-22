package com.nemustech.platform.lbs.common.mapper;



import org.springframework.stereotype.Repository;

import com.nemustech.platform.lbs.common.vo.DateVo;
import com.nemustech.platform.lbs.common.vo.MapVo;


@Repository(value = "MapApiMapper")
public interface MapApiMapper {
	
	public MapVo selectMap(String map_uid) throws Exception;	
	public DateVo getLastModifiedMap(String map_uid)  throws Exception;
	
}
