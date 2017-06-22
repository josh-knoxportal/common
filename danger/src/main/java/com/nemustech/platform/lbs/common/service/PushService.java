package com.nemustech.platform.lbs.common.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nemustech.platform.lbs.common.mapper.PushApiMapper;
import com.nemustech.platform.lbs.common.vo.PushHistoryVo;

//db insert, 상태 update ...

@Service(value = "pushService")
public class PushService {
	@Autowired
	private PushApiMapper pushApiMapper;
	@Autowired
	private GcmService gcmService;
	
	public boolean pushSendByDevice(String systemType,List<String> deviceList) throws Exception{
		return true;
	}
	
	public String pushSend(PushHistoryVo pushHistory) throws Exception {
		insertPush(pushHistory) ;
		List<String> pushIdList = getPushIdList(pushHistory);		
		Map<String,String> data = new HashMap<String,String>();
		
		data.put("action",pushHistory.getAction());
		data.put("message",pushHistory.getMessage());
		
		System.out.println("pushIdList : " + pushIdList.toArray());
		
		gcmService.pushToGcm(pushIdList, data);
		
		
		//현재는 gcm만 있다고 판단하고
		//주석처리
		//gcmService.pushToGcm(pushIdList, null);
		//난중에 결과값을 받아 push 상태를 업데이트해야 한다.
		
		return "SUCCESS";
	}
	
	
	
	public String pushSend(String systemType, String action, String phone, String message, String user_id) throws Exception {
		List<String> phoneList = new ArrayList<String>();
		phoneList.add(phone);
		return pushSend(systemType,action, phoneList, message,user_id);
	}
	
	
	public String pushSend(String systemType, String action, List<String> phoneList, String message, String user_id) throws Exception{	
		
		String uid = pushApiMapper.selectUid();
		//WebUtils.getSessionAttribute(request, name)
		PushHistoryVo pushHistory = new PushHistoryVo();
		pushHistory.setPush_uid(uid);
		pushHistory.setSystem_type(systemType);
		pushHistory.setUser_id(user_id);
		pushHistory.setMessage(message);
		pushHistory.setTargetList(phoneList);
		pushHistory.setAction(action);
		return pushSend(pushHistory);
	}
	
	private boolean insertPush(PushHistoryVo pushHistory) throws Exception{
		pushApiMapper.insertPushHistory(pushHistory);
		pushApiMapper.insertPushTarget(pushHistory);
		return true;
	}
	
	
	private List<String> getPushIdList(PushHistoryVo pushHistoryVo) throws Exception{
		if("1".equals(pushHistoryVo.getSystem_type())){
			return pushApiMapper.selectVehiclePushIdList(pushHistoryVo);
		}
		
		if("2".equals(pushHistoryVo.getSystem_type())){
			return pushApiMapper.selectDangerPushIdList(pushHistoryVo);
		}
		
		
		return null;
	}

}
