package com.nemustech.platform.lbs.wwms.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.nemustech.platform.lbs.wwms.mapper.DangerApiMapper;
import com.nemustech.platform.lbs.wwms.mapper.WorkApiMapper;
import com.nemustech.platform.lbs.wwms.model.WorkStatusList;
import com.nemustech.platform.lbs.wwms.vo.WorkIssueVo;
import com.nemustech.platform.lbs.wwms.vo.WorkMntRequestVo;
import com.nemustech.platform.lbs.wwms.vo.WorkMntVo;
import com.nemustech.platform.lbs.wwms.vo.WorkStatusRequestVo;
import com.nemustech.platform.lbs.wwms.vo.WorkStatusVo;
import com.nemustech.platform.lbs.wwms.vo.WorkVo;
import com.nemustech.platform.lbs.wwms.vo.WorkerMntRequestVo;
import com.nemustech.platform.lbs.wwms.vo.WorkerMntVo;

@Service(value = "workApiService")
public class WorkApiService {

	private static final Logger logger = LoggerFactory.getLogger(WorkApiService.class);

	@Autowired
	private WorkApiMapper workApiMapper;

	@Autowired
	private DangerApiMapper dangerApiMapper;

	public List<WorkStatusVo> workStatusList(WorkStatusRequestVo requestVo) throws Exception {
		return workApiMapper.selectWorkStatusList(requestVo);
	}

	public int selectWorkStatusTotalCount(WorkStatusRequestVo requestVo) throws Exception {
		return workApiMapper.selectWorkStatusTotalCount(requestVo);
	}

	public List<WorkStatusVo> assignWorkerList(WorkStatusRequestVo requestVo) throws Exception {
		return workApiMapper.selectAssignWorkerList(requestVo);
	}

	public WorkStatusList getWorkerList(WorkStatusRequestVo requestVo) throws Exception {
		logger.info("getWorkerList");
		WorkStatusList list = new WorkStatusList();
		list.setWorkStatusList(workApiMapper.selectAssignWorkerList(requestVo));
		list.setTotalcnt(workApiMapper.selectAssignWorkerListTotalCount(requestVo));
		return list;
	}

	public List<WorkIssueVo> workIssueList(WorkStatusRequestVo requestVo) throws Exception {
		return workApiMapper.selectWorkIssueList(requestVo);
	}

	public int selectworkIssueListTotalCount(WorkStatusRequestVo requestVo) throws Exception {
		return workApiMapper.selectworkIssueListTotalCount(requestVo);
	}

	public List<WorkerMntVo> workerMntList(WorkerMntRequestVo requestVo) throws Exception {
		return workApiMapper.selectWorkerMntList(requestVo);
	}

	public int selectWorkerMntTotalCount(WorkerMntRequestVo requestVo) throws Exception {
		return workApiMapper.selectWorkerMntTotalCount(requestVo);
	}

	public List<WorkMntVo> workMntList(WorkMntRequestVo requestVo) throws Exception {
		return workApiMapper.selectWorkMntList(requestVo);
	}

	public int selectWorkMntTotalCount(WorkMntRequestVo requestVo) throws Exception {
		return workApiMapper.selectWorkMntTotalCount(requestVo);
	}

	@Transactional
	public int workUpdate(WorkVo requestVo) throws Exception {
		logger.debug("수정 팝업창 작업 수정");
		int result = 1;
		if (requestVo.getWork_uid() != null) {
			if (!StringUtils.isEmpty(requestVo.getType_list())) {
				workApiMapper.deleteWorkType(requestVo);
				if (requestVo.getType_list().size() != 0) {
					workApiMapper.insertWorkType(requestVo);
				}
			}
		}
		result = workApiMapper.updateWork(requestVo);
		return result;
	}

	@Transactional
	public int workInsert(WorkVo requestVo) throws Exception {
		logger.debug("작업 추가");
		int result = 1;

		String work_uid = dangerApiMapper.selectUid();
		logger.debug("get work_uid [{}]", work_uid);

		requestVo.setWork_uid(work_uid);
		logger.debug("get requestVo [{}]", requestVo.toString());

		// 1-N 시나리오에서 중복 작업 허용
		// WorkVo data = workApiMapper.checkWorkNo(requestVo);
		// if (data != null) {
		// logger.debug("WorkVo data not null
		// workApiMapper.checkWorkNo(requestVo) [{}]", data.toString());
		// return -801;
		// }

		workApiMapper.insertWork(requestVo);
		if (requestVo.getType_list().size() != 0) {
			workApiMapper.insertWorkType(requestVo);
		}

		return result;
	}

	public int workDelete(WorkVo requestVo) throws Exception {
		logger.debug("작업 삭제");
		int result = 0;
		result = workApiMapper.deleteWorkType(requestVo);
		result = workApiMapper.deleteWork(requestVo);
		return result;
	}

	@Transactional
	public int complatedWorkDelete(WorkVo requestVo) throws Exception {
		int result = 0;

		logger.debug("완료된 작업 삭제");

		if (StringUtils.isEmpty(requestVo.getWork_uid())) {
			logger.debug("완료된 작업 삭제 work_uid is null return");
			return result;
		}

		result = workApiMapper.updateIsDeletedWork(requestVo.getWork_uid());

		// result = workApiMapper.deleteBeaconEvent(requestVo);
		// result = workApiMapper.deleteGpsEvent(requestVo);
		// result = workApiMapper.deleteWorkType(requestVo);
		// result = workApiMapper.deleteWork(requestVo);
		return result;
	}

	public String makeWorkNo(String name) throws Exception {
		return dangerApiMapper.makeWorkNo(name);
	}

	public int updateWorkerAssigned(WorkerMntVo workerMntVo) throws Exception {
		logger.debug("작업자 수정");
		return workApiMapper.updateWorkerAssigned(workerMntVo);
	}

	public int deleteWorkerAssigned(WorkerMntVo workerMntVo) throws Exception {
		logger.debug("작업자 삭제");
		return workApiMapper.deleteWorkerAssigned(workerMntVo);
	}

	public WorkMntVo selectWorkDetail(String work_uid) throws Exception {
		return workApiMapper.selectWorkDetail(work_uid);
	}

}
