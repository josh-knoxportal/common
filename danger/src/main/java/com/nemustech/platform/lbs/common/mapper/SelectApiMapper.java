package com.nemustech.platform.lbs.common.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@SuppressWarnings("rawtypes")
@Repository(value = "SelectApiMapper")
public interface SelectApiMapper {

	public List<Map<String, String>> selectAnything(Map query) throws Exception;

	public void getInsertAnything(Map query) throws Exception;

	public int getUpdateAnything(Map query) throws Exception;

	public int getDeleteAnything(Map query) throws Exception;

	public List<Map<String, String>> getSigunguList(Map query) throws Exception;
}
