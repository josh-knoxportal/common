package com.nemustech.platform.lbs.ngms.service;




import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.nemustech.platform.lbs.common.vo.SearchVo;
import com.nemustech.platform.lbs.ngms.mapper.VehicleApiMapper;
import com.nemustech.platform.lbs.ngms.vo.VehicleRegVo;;

@Service(value = "vehicleApiService")
public class VehicleApiService {

	private static final Logger logger = LoggerFactory.getLogger(VehicleApiService.class);
	
	@Autowired
	private VehicleApiMapper vehicleApiMapper;
	

	public List<VehicleRegVo>  vehicleRegList(SearchVo searchVo) throws Exception {
		logger.info("차량등록정보 검색");
		return vehicleApiMapper.selectVehicleRegList(searchVo);
	}
	
	public int vehicleRegListTotalCnt(SearchVo searchVo) throws Exception {
		logger.info("차량등록정보 검색 총 건수");		
		return 	vehicleApiMapper.selectVehicleRegListTotalCnt(searchVo);
	}
	
	public VehicleRegVo  selectVehicleReg(VehicleRegVo vehicleRegVo) throws Exception {
		logger.info("차량등록정보 검색 :" + vehicleRegVo);
		return vehicleApiMapper.selectVehicleReg(vehicleRegVo);
	}
		
	@Transactional(isolation=Isolation.DEFAULT, propagation=Propagation.REQUIRES_NEW, rollbackFor=Exception.class)
	public int updateVehicleReg(VehicleRegVo vehicleRegVo) throws Exception {
		logger.info("차량등록/수정");		
		//opcode : update 이면 차량수정임 
		//         운행중이지 않을 때만 수정가능함. 운행중이면 수정불가
		//		   운행중은 last_vehicle_id 의 값으로 vehicle테이블에 is_out  1 이 아닌 경우 운행중으로 봐야함.
		//		   중복확인은 device테이블에서 해당 assigned_vehicle_no 가 중복된게 있는지 확인한다. && is_assinged = 1 
		//opcode : insert 이면 차량등록임 = tbvc_device에 assigned_vehicle_no 업데이트 함.
		//		   중복확인은 device테이블에서 해당 assigned_vehicle_no 가 중복된게 있는지 확인한다. && is_assinged = 1 
	
		// -999 : 중복할된 차량번호, -998 : 차량번호 입력하지 않음
		// -888 : 운행중인 차량번호
		
		if("".equals(vehicleRegVo.getVehicle_no()) || vehicleRegVo.getVehicle_no() == null){
			return -998;
		}
		
		int cnt = vehicleApiMapper.selectVehicleRegCheck(vehicleRegVo);		
		if(cnt > 0)
			return -999;		
		
		
		cnt = vehicleApiMapper.selectVehiclDrivingCheck(vehicleRegVo);
		if(cnt > 0){
			logger.info("cnt: "+ cnt);
			return -888;
		}
		
		logger.info("vehicleRegVo:"+vehicleRegVo);
		vehicleApiMapper.updateVehicleReg(vehicleRegVo);
		
		return 1;
	}
	
	public VehicleRegVo insertVehicle(VehicleRegVo vehicleReg) throws Exception {
		logger.info("차량등록 insert");		
		vehicleApiMapper.updateVehicleReg(vehicleReg);
		return vehicleReg;
	}
	
	
}
