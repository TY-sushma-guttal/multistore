package com.mrmrscart.userservice.repository.supplier;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.mrmrscart.userservice.entity.supplier.StaffManagementInfo;

public interface StaffManagementRepository extends JpaSpecificationExecutor<StaffManagementInfo>, JpaRepository<StaffManagementInfo, String> {
	
	
	StaffManagementInfo findByEmailIdAndIsDelete(String emailId,boolean isDelete);
	
	StaffManagementInfo findByMobileNumberAndIsDeleteAndIsMobileNumberVerified(String mobileNumber,boolean isDelete,boolean isMobileNumberVerified);
	
	public StaffManagementInfo findByStaffIdAndIsDelete(String staffId,boolean isDelete);
	
	public StaffManagementInfo findByMobileNumber(String mobileNumber);
}
