package com.mrmrscart.userservice.service.admin;

import java.util.List;

import javax.mail.SendFailedException;

import org.springframework.data.jpa.domain.Specification;

import com.mrmrscart.userservice.entity.admin.EAdminStatus;
import com.mrmrscart.userservice.entity.supplier.ESupplierStatus;
import com.mrmrscart.userservice.entity.supplier.SupplierRegistration;
import com.mrmrscart.userservice.pojo.admin.FromAndToDatePojo;
import com.mrmrscart.userservice.pojo.admin.SupplierApprovalPojo;
import com.mrmrscart.userservice.pojo.admin.SupplierCountPojo;
import com.mrmrscart.userservice.wrapper.admin.AdminSupplierCountWrapper;

public interface AdminSupplierService {
	public SupplierRegistration supplierApproval(SupplierApprovalPojo data) throws SendFailedException;

	public AdminSupplierCountWrapper getAllSupplier(ESupplierStatus status, int pageNumber, int pageSize);

	public List<SupplierRegistration> getAllSupplierInfo(Specification<SupplierRegistration> spec, int pageNumber,
			int pageSize);

	public SupplierCountPojo getSupplierCount(FromAndToDatePojo fromAndToDatePojo);

	public List<SupplierRegistration> getSupplierInfoBasedOnFilter(EAdminStatus status, String category,
			String fromDate, String toDate, String keyWord);

	public void inviteSupplier(String userName) throws SendFailedException;

	public boolean enableDisableSupplier(String supplierId, boolean isDisabled);

}
