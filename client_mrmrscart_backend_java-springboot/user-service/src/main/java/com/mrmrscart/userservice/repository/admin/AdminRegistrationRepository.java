package com.mrmrscart.userservice.repository.admin;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mrmrscart.userservice.entity.admin.AdminRegistration;
import com.mrmrscart.userservice.entity.admin.EAdminStatus;

@Repository
public interface AdminRegistrationRepository extends JpaRepository<AdminRegistration, String> {

	AdminRegistration findByEmailIdAndStatus(String emailId, EAdminStatus status);

	AdminRegistration findByMobileNumberAndStatus(String mobileNumber, EAdminStatus status);

	AdminRegistration findByEmailIdAndStatusAndIsDelete(String emailId, EAdminStatus status, boolean b);

	AdminRegistration findByMobileNumberAndStatusAndIsDelete(String userName, EAdminStatus approved, boolean b);

	AdminRegistration findByAdminRegistrationIdAndIsDelete(String adminRegistrationId, boolean b);

	List<AdminRegistration> findByIsDelete(boolean b);

	List<AdminRegistration> findByIsDeleteAndCreatedByIn(boolean b, List<String> createdBy);

	AdminRegistration findByAdminRegistrationIdAndStatusAndIsDelete(String adminRegistrationId, EAdminStatus status,
			boolean isDelete);

	List<AdminRegistration> findByDesignationAndIsDeleteAndStatus(String designation, boolean isDelete, EAdminStatus status);

	AdminRegistration findByMobileNumber(String mobileNumber);
}
