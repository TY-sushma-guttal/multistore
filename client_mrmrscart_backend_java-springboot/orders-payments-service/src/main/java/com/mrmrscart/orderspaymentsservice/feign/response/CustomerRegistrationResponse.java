package com.mrmrscart.orderspaymentsservice.feign.response;

import com.mrmrscart.orderspaymentsservice.feign.pojo.CustomerRegistration;

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
