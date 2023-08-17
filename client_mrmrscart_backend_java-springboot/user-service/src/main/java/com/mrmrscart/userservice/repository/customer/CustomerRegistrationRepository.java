package com.mrmrscart.userservice.repository.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mrmrscart.userservice.entity.customer.CustomerRegistration;

@Repository
public interface CustomerRegistrationRepository extends JpaRepository<CustomerRegistration, String> {

	CustomerRegistration findByEmailIdAndIsDisabled(String emailId, boolean isDisabled);

	CustomerRegistration findByMobileNumberAndIsDisabled(String mobileNumber, boolean isDisabled);

	CustomerRegistration findByMobileNumber(String mobileNumber);

	CustomerRegistration findByCustomerIdAndIsDisabled(String customerId, boolean b);
}
