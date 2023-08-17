package com.mrmrscart.userservice.repository.reseller;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mrmrscart.userservice.entity.reseller.ResellerRegistration;

@Repository
public interface ResellerRegistrationRepository extends JpaRepository<ResellerRegistration, String>{

	ResellerRegistration findByEmailIdAndIsDisabled(String emailId,boolean isDisabled);
	
	ResellerRegistration findByMobileNumberAndIsDisabled(String mobileNumber,boolean isDisabled);
}
