package com.mrmrscart.userservice.feign.service;

import static com.mrmrscart.userservice.common.supplier.SupplierConstant.SOMETHING_WENT_WRONG;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mrmrscart.userservice.entity.customer.CustomerRegistration;
import com.mrmrscart.userservice.entity.supplier.EUserType;
import com.mrmrscart.userservice.entity.supplier.SupplierRegistration;
import com.mrmrscart.userservice.exception.supplier.SupplierException;
import com.mrmrscart.userservice.feign.pojo.UserInfoWrapper;
import com.mrmrscart.userservice.feign.wrapper.UserDetailsResponseWrapper;
import com.mrmrscart.userservice.repository.customer.CustomerRegistrationRepository;
import com.mrmrscart.userservice.repository.supplier.SupplierRegistrationRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private SupplierRegistrationRepository supplierRegistrationRepository;
	@Autowired
	private CustomerRegistrationRepository customerRegistrationRepository;

	@Override
	public List<UserDetailsResponseWrapper> getUserDetails(UserInfoWrapper userInfoWrapper) {

		try {
			if (userInfoWrapper.getUserType().equals(EUserType.SUPPLIER)) {
				List<SupplierRegistration> supplierRegistrations = supplierRegistrationRepository
						.findAllById(userInfoWrapper.getUserId());
				return supplierRegistrations.stream().map(e -> new UserDetailsResponseWrapper(e.getSupplierId(),
						e.getFirstName() + " " + e.getLastName())).collect(Collectors.toList());
			} else if (userInfoWrapper.getUserType().equals(EUserType.CUSTOMER)
					|| userInfoWrapper.getUserType() == EUserType.CUSTOMER_STORE) {
				List<CustomerRegistration> list = customerRegistrationRepository
						.findAllById(userInfoWrapper.getUserId());
				return list.stream().map(e -> new UserDetailsResponseWrapper(e.getCustomerId(), e.getCustomerName()))
						.collect(Collectors.toList());
			} else {
				return new ArrayList<>();
			}
		} catch (Exception e) {
			throw new SupplierException(SOMETHING_WENT_WRONG);
		}
	}

}
