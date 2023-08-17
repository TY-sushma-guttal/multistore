package com.mrmrscart.userservice.service.customer;

import java.util.List;

import com.mrmrscart.userservice.entity.customer.CustomerRegistration;
import com.mrmrscart.userservice.pojo.customer.CustomerRegistrationPojo;
import com.mrmrscart.userservice.pojo.customer.DropDownPojo;

public interface CustomerRegistrationService {

	public List<DropDownPojo> getCustomers(List<String> customerId);

	public CustomerRegistration addCustomer(CustomerRegistrationPojo customerRegistrationPojo);

	public CustomerRegistration findCustomerById(String customerId);

}
