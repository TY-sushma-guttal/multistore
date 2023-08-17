package com.mrmrscart.userservice.service.admin;

import java.util.List;

import com.mrmrscart.userservice.entity.admin.AdminRegistration;
import com.mrmrscart.userservice.pojo.admin.AdminManagerFilterPojo;
import com.mrmrscart.userservice.pojo.admin.AdminManagerPojo;

public interface AdminManagerService {
	public AdminRegistration addAdminManager(AdminManagerPojo data);
	public AdminRegistration getAdminManager(String id);
	public AdminRegistration updateAdminManager(AdminManagerPojo data);
	public boolean deleteAdminManager(String id);
	public boolean disableAdminManager(String id);
	public List<AdminRegistration> getFilteredAndPaginatedData(AdminManagerFilterPojo data);
}
