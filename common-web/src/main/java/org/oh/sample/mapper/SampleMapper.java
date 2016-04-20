package org.oh.sample.mapper;

import java.util.List;

import org.oh.sample.model.Sample;
import org.springframework.stereotype.Repository;

/**
 * 샘플 매퍼
 * 
 * @author skoh
 */
@Repository
public interface SampleMapper {
	public List<Sample> list(Sample sample);

	public int count(Sample sample);

	public int insert(Sample sample);

	public int update(Sample sample);

	public int delete(Sample sample);

	public int merge(Sample sample);
}