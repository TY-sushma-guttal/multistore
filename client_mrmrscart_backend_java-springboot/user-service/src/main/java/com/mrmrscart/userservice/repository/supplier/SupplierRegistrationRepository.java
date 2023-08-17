package com.mrmrscart.userservice.repository.supplier;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mrmrscart.userservice.entity.supplier.ESupplierStatus;
import com.mrmrscart.userservice.entity.supplier.SupplierRegistration;
import com.mrmrscart.userservice.entity.supplier.SupplierStoreInfo;

@Repository
public interface SupplierRegistrationRepository
		extends JpaRepository<SupplierRegistration, String>, JpaSpecificationExecutor<SupplierRegistration> {

	SupplierRegistration findByMobileNumber(String mobileNumber);

	SupplierRegistration findByEmailIdAndStatus(String userName, ESupplierStatus status);

	SupplierRegistration findByMobileNumberAndStatus(String userName, ESupplierStatus userType);

	List<SupplierRegistration> findByGstinAndStatusNot(String gstin, ESupplierStatus status);

	@Query("Select sr from SupplierRegistration sr where sr.supplierId=?1 and sr.status=?2")
	public SupplierRegistration findBySupplierIdAndStatus(String supplierId, ESupplierStatus status);

	@Query("Select sr from SupplierRegistration sr where sr.emailId=?1")
	public SupplierRegistration findByEmailId(String emailId);

	Page<SupplierRegistration> findByStatus(ESupplierStatus status, PageRequest pageRequest);

	List<SupplierRegistration> findByStatus(ESupplierStatus status);

	long countByRegisteredAtBetween(LocalDateTime fromDate, LocalDateTime toDate);

	long countByRegisteredAtBefore(LocalDateTime fromDate);

	SupplierRegistration findBySupplierReferralCodeAndStatus(String supplierReferralCode, ESupplierStatus status);

	SupplierRegistration findTopByOrderByApprovedAtDesc();

	List<SupplierRegistration> findByReferredById(String referredById);

	SupplierRegistration findBySupplierStoreInfo(SupplierStoreInfo supplierStoreInfo);

	List<SupplierRegistration> findByEmailIdOrMobileNumber(String emailId, String mobileNumber);

	List<SupplierRegistration> findBySupplierIdIn(List<String> userId);
	
	long countByReferredByIdAndSignupFreeOrderCountGreaterThan(String supplierId,Long signupFreeOrderCount);
	
	@Query("Select sr from SupplierRegistration sr where sr.referredById=?1 and sr.signupFreeOrderCount>?2 and  EXTRACT (year FROM sr.registeredAt) =?3 and EXTRACT (month FROM sr.registeredAt) =?4 ")
	public List<SupplierRegistration> findByYearMonth(String supplierId,Long signupFreeOrderCount,int year,int month);

}
