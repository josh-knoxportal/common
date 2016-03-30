package org.oh.web.mapper;

import java.util.List;
import java.util.Map;

import org.oh.web.model.Sample;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleMapper {
	public List<Sample> list(Map<String, Object> params);

	public int insert(Sample sample);

	public int update(Sample sample);

	public int delete(Sample sample);

	public int merge(Sample sample);
}