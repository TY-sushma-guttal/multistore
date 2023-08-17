package com.mrmrscart.userservice.service.supplier;

import java.util.List;

import com.mrmrscart.userservice.entity.supplier.EStaffStatus;
import com.mrmrscart.userservice.entity.supplier.StaffManagementInfo;
import com.mrmrscart.userservice.entity.supplier.SupplierRegistration;
import com.mrmrscart.userservice.pojo.supplier.StaffManagementPojo;

public interface StaffManagementService {
	public String addStaffInfo(StaffManagementPojo data);

	public StaffManagementPojo getStaffInfo(String id);

	public StaffManagementPojo deleteStaffInfo(String id);

	public List<StaffManagementPojo> getAllStaffInfo(int pageNumber, int pageSize);

	public List<StaffManagementPojo> getAllStaffInfoBySupllierId(String supplierId, int pageNumber, int pageSize);

	public List<StaffManagementInfo> staffFilter(String supplierId, int pageNumber, int pageSize, EStaffStatus type, String keyword);
	
	public StaffManagementInfo updateStaffInfo(StaffManagementPojo data);
	
}
