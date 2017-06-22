package com.nemustech.platform.lbs.common.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nemustech.platform.lbs.common.mapper.SelectApiMapper;

@SuppressWarnings("rawtypes")
@Service(value = "SelectApiService")
public class SelectApiService {

	@Autowired
	private SelectApiMapper selectApiMapper;

	public List<Map<String, String>> getSelectAnything(Map query) throws Exception {

		List<Map<String, String>> list = selectApiMapper.selectAnything(query);
		return list;
	}

	public void getInsertAnything(Map query) throws Exception {

		selectApiMapper.getInsertAnything(query);
	}

	public int getUpdateAnything(Map query) throws Exception {

		return selectApiMapper.getUpdateAnything(query);
	}

	public int getDeleteAnything(Map query) throws Exception {

		return selectApiMapper.getDeleteAnything(query);
	}

	public List<Map<String, String>> getSigunguList(Map query) throws Exception {

		List<Map<String, String>> list = selectApiMapper.getSigunguList(query);
		return list;
	}
}
