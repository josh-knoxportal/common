package com.nemustech.platform.lbs.lpms.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;

@XmlRootElement(name = "OUTPUT")
public class LpmsWorksVo {

	private String returnCode;
	private String returnDesc;
	private String returnTotalCount;
	private List<LpmsWorkVo> lpmsWorksVo;

	@XmlElement(name = "ReturnCode")
	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	@XmlElement(name = "ReturnDesc")
	public String getReturnDesc() {
		return returnDesc;
	}

	public void setReturnDesc(String returnDesc) {
		this.returnDesc = returnDesc;
	}

	@XmlElement(name = "ReturnTotalCount")
	public String getReturnTotalCount() {
		return returnTotalCount;
	}

	public void setReturnTotalCount(String returnTotalCount) {
		this.returnTotalCount = returnTotalCount;
	}

	@XmlElementWrapper(name = "ReturnData")
	@XmlElement(name = "Data")
	public List<LpmsWorkVo> getLpmsWorksVo() {
		return lpmsWorksVo;
	}

	public void setLpmsWorksVo(List<LpmsWorkVo> lpmsWorksVo) {
		this.lpmsWorksVo = lpmsWorksVo;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
