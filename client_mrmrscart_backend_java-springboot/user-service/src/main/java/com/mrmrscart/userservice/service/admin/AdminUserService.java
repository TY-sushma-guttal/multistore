package com.mrmrscart.userservice.service.admin;

import java.util.List;

import javax.mail.SendFailedException;

import com.mrmrscart.userservice.entity.admin.AdminRegistration;
import com.mrmrscart.userservice.entity.admin.EAdminStatus;
import com.mrmrscart.userservice.pojo.admin.AdminUserFilterPojo;
import com.mrmrscart.userservice.pojo.admin.AdminUserPojo;
import com.mrmrscart.userservice.pojo.customer.DropDownPojo;

public interface AdminUserService {
	
	
	public AdminRegistration createAdminUser(AdminUserPojo adminUserPojo) throws SendFailedException;
	
	public AdminRegistration updateAdminUser(AdminUserPojo adminUserPojo);
	
	public AdminRegistration disableAdminUser(String adminRegistrationId,String status);
	
	public boolean deleteAdminUser(String adminRegistrationId);
	
	public List<AdminRegistration> getAllAdminUsers(int pageNumber,int pageSize,AdminUserFilterPojo adminUserFilterPojo);
	
	public List<EAdminStatus> getAllStatus();
	
	public List<DropDownPojo> getAllCreatedBy();

}
