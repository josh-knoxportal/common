package com.nemustech.platform.lbs.ngms.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nemustech.platform.lbs.common.vo.SearchVo;
import com.nemustech.platform.lbs.ngms.vo.VehicleRegVo;

@Repository(value = "VehicleApiMapper")
public interface VehicleApiMapper {
	public List<VehicleRegVo> selectVehicleRegList(SearchVo searchVo) throws Exception;
	public int selectVehicleRegListTotalCnt(SearchVo searchVo) throws Exception;
	public void	updateVehicleReg(VehicleRegVo vehicleRegVo) throws Exception;

	public VehicleRegVo selectVehicleReg(VehicleRegVo vehicleReVo) throws Exception;
	public int selectVehicleRegCheck(VehicleRegVo vehicleReg) throws Exception;
	public int selectVehiclDrivingCheck(VehicleRegVo vehicleReg) throws Exception;
	
	public VehicleRegVo selectVehicle(VehicleRegVo vehicleReg) throws Exception;
	
	//public int	insertVehicle(VehicleRegVo vehicleRegVo) throws Exception;
	//public void	updateVehicle(VehicleRegVo vehicleRegVo) throws Exception;
	public String selectUid() throws Exception;

	
	
}
