package com.nemustech.sample.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.nemustech.common.file.Files;
import com.nemustech.common.service.impl.FilesServiceImpl;
import com.nemustech.common.util.Utils;
import com.nemustech.sample.model.Files2;
import com.nemustech.sample.service.Files2Service;

/**
 * 파일2 서비스
 * 
 * @author skoh
 */
@Service
public class Files2ServiceImpl extends FilesServiceImpl<Files2> implements Files2Service {
	@Lazy
	@Autowired
	protected Files2Service files2Service;

	public List<Files2> insertFile(List<Files2> files) throws Exception {
		List<Files2> filesList = new ArrayList<Files2>();
		for (Files file : files) {
			file.setPath(fileStorage.save(file));

//			filesList.add(new Files2(file, getId(model).toString()));
		}

		return null;
	}

	public List<Files2> updateFile(List<Files2> files) throws Exception {
		if (Utils.isValidate(files)) {
			deleteFile(files);
		}

		return super.updateFile(files);
	}

	public List<Files2> deleteFile(List<Files2> files) throws Exception {
//		int result = 0;

//		List<Sample> list = list(model);
//		for (Sample sample : list) {
		Files2 files2 = new Files2();
//			files2.addCondition("doc_id = '" + sample.getId() + "'");
		files2.addCondition("doc_id = '" + files.get(0).getDoc_id() + "'");

//		result += files2Service.delete(files2);
//		}

		return files;
	}

}