package com.nemustech.platform.lbs.wwms.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nemustech.platform.lbs.wwms.vo.WorkIssueVo;
import com.nemustech.platform.lbs.wwms.vo.WorkMntRequestVo;
import com.nemustech.platform.lbs.wwms.vo.WorkMntVo;
import com.nemustech.platform.lbs.wwms.vo.WorkStatusRequestVo;
import com.nemustech.platform.lbs.wwms.vo.WorkStatusVo;
import com.nemustech.platform.lbs.wwms.vo.WorkVo;
import com.nemustech.platform.lbs.wwms.vo.WorkerMntRequestVo;
import com.nemustech.platform.lbs.wwms.vo.WorkerMntVo;

@Repository(value = "WorkApiMapper")
public interface WorkApiMapper {
	public List<WorkStatusVo> selectWorkStatusList(WorkStatusRequestVo requestVo) throws Exception;

	public int selectWorkStatusTotalCount(WorkStatusRequestVo requestVo) throws Exception;

	public List<WorkStatusVo> selectAssignWorkerList(WorkStatusRequestVo requestVo) throws Exception;

	public List<WorkIssueVo> selectWorkIssueList(WorkStatusRequestVo requestVo) throws Exception;

	public int selectworkIssueListTotalCount(WorkStatusRequestVo requestVo) throws Exception;

	public List<WorkerMntVo> selectWorkerMntList(WorkerMntRequestVo requestVo) throws Exception;

	public int selectWorkerMntTotalCount(WorkerMntRequestVo requestVo) throws Exception;

	public List<WorkMntVo> selectWorkMntList(WorkMntRequestVo requestVo) throws Exception;

	public int selectWorkMntTotalCount(WorkMntRequestVo requestVo) throws Exception;

	public int insertWork(WorkVo requestVo) throws Exception;

	public int deleteWork(WorkVo requestVo) throws Exception;

	public int updateWork(WorkVo requestVo) throws Exception;

	public int insertWorkType(WorkVo requestVo) throws Exception;

	public WorkVo checkWorkNo(WorkVo requestVo) throws Exception;

	public int deleteWorkType(WorkVo requestVo) throws Exception;

	public int deleteBeaconEvent(WorkVo requestVo) throws Exception;

	public int deleteGpsEvent(WorkVo requestVo) throws Exception;

	public int updateWorkerAssigned(WorkerMntVo workerMntVo) throws Exception;

	public int deleteWorkerAssigned(WorkerMntVo workerMntVo) throws Exception;

	public WorkMntVo selectWorkDetail(String work_uid) throws Exception;

	public int updateIsDeletedWork(String work_uid) throws Exception;

	public int selectAssignWorkerListTotalCount(WorkStatusRequestVo requestVo) throws Exception;

}
