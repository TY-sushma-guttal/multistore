package com.mrmrscart.userservice.response.customer;

import com.mrmrscart.userservice.entity.customer.CustomerRegistration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRegistrationResponse {
	private boolean error;
	private String message;
	private CustomerRegistration data;

}
