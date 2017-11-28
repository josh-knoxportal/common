package com.nemustech.sample.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nemustech.common.annotation.TransactionalException;
import com.nemustech.common.service.impl.CommonServiceImpl;
import com.nemustech.sample.annotation.CacheEvictSample;
import com.nemustech.sample.mapper.SampleMapper;
import com.nemustech.sample.model.Sample;
import com.nemustech.sample.model.Sample.Sample2;
import com.nemustech.sample.service.SampleService;

/**
 * 샘플 서비스
 * 
 * @author skoh
 */
@Service
public class SampleServiceImpl extends CommonServiceImpl<Sample> implements SampleService {
	@Autowired
	protected SampleMapper mapper;

//	@Override
//	public CommonMapper<Sample> getMapper() {
//		return mapper;
//	}

//	@Override
//	public Object insert(Sample model) throws Exception {
//		model.setName(makeVariable("CONCAT('" + model.getName() + "', '#')"));
//
//		return super.insert(model);
//	}
//
//	@Override
//	public int update(Sample model) throws Exception {
//		model.setName(makeVariable("CONCAT('" + model.getName() + "', '$')"));
//
//		return super.update(model);		
//	}
//
//	@Override
//	public List<Sample> list(Sample model) throws Exception {
//		model.setFields("reg_id reg_id,reg_dt reg_dt,mod_id mod_id,mod_dt mod_dt,id id,CONCAT(name, '#') name,test_id test_id");
//
//		return super.list(model);
//	}

//	@Override
//	public Sample get2(Sample sample) throws Exception {
//		List<Sample> list = mapper.list(sample);
//		return (list.size() > 0) ? list.get(0) : null;
//	}
//
//	@Override
//	@CacheableSample
//	public List<Sample> list2(Sample sample) throws Exception {
//		return mapper.list(sample);
//	}
//
//	@Override
//	public int count2(Sample sample) throws Exception {
//		return mapper.count(sample);
//	}
//
//	@Override
//	public List<Sample> page2(Sample sample) throws Exception {
//		return mapper.list(sample);
//	}
//
//	@Override
//	@TransactionalException
//	@CacheEvictSample
//	public int insert2(Sample sample) throws Exception {
//		return mapper.insert(sample);
//	}
//
//	@Override
//	@TransactionalException
//	@CacheEvictSample
//	public int update2(Sample sample) throws Exception {
//		return mapper.update(sample);
//	}
//
//	@Override
//	@TransactionalException
//	@CacheEvictSample
//	public int delete2(Sample sample) throws Exception {
//		return mapper.delete(sample);
//	}

	@Override
	@TransactionalException
	@CacheEvictSample
	public int merge(Sample sample) throws Exception {
		return mapper.merge(sample);
	}

	@Override
	public List<Sample> list3(Sample2 model) throws Exception {
		model.setSql_name("list3");
		model.setHint("DISTINCT");
		model.setFields("id, name");
		model.setOrder_by("id DESC");

		return list(model);
	}
}