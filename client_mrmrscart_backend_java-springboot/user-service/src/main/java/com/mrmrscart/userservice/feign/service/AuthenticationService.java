package com.mrmrscart.userservice.feign.service;

import com.mrmrscart.userservice.entity.admin.AdminRegistration;
import com.mrmrscart.userservice.entity.supplier.StaffManagementInfo;
import com.mrmrscart.userservice.feign.pojo.AuthenticationPojo;
import com.mrmrscart.userservice.feign.pojo.JwtInfoPojo;

public interface AuthenticationService {

	JwtInfoPojo verifyUser(AuthenticationPojo authenticationPojo);

	StaffManagementInfo getStaffManagementInfo(String emailId);

	public AdminRegistration getAdminInfo(String emailId);
}
