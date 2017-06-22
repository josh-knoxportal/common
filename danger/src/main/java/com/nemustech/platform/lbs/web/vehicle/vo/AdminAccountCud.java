package com.nemustech.platform.lbs.web.vehicle.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;

/**
 * 계정목록정보 목록
 **/
@ApiModel(description = "계정정보 ")
public class AdminAccountCud extends ResponseData {

    private AdminAccountVo adminAccount;

    @ApiModelProperty(value = "계정정보")
    @JsonProperty("adminAccount")
    public AdminAccountVo getAdminAccount() {
        return adminAccount;
    }

    public void setAdminAccount(AdminAccountVo adminAccount) {
        this.adminAccount = adminAccount;
    }

}
