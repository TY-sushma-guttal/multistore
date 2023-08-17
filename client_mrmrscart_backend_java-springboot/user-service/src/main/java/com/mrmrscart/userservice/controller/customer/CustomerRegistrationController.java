package com.mrmrscart.userservice.controller.customer;

import static com.mrmrscart.userservice.common.customer.CustomerConstant.REGISTRATION_SUCCESS_MESSAGE;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mrmrscart.userservice.pojo.customer.CustomerRegistrationPojo;
import com.mrmrscart.userservice.response.admin.SuccessResponse;
import com.mrmrscart.userservice.response.customer.CustomerRegistrationResponse;
import com.mrmrscart.userservice.service.customer.CustomerRegistrationService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/users")
public class CustomerRegistrationController {

	@Autowired
	private CustomerRegistrationService customerRegistrationService;

	@PostMapping("/customers")
	public ResponseEntity<SuccessResponse> getCustomers(@RequestBody List<String> customerIds) {
		return new ResponseEntity<>(new SuccessResponse(false, "Customers Fetched Successfully",
				customerRegistrationService.getCustomers(customerIds)), HttpStatus.OK);
	}

	/*
	 * @Author : Hemadri G
	 */
	@Operation(summary = "This API is used to register customer")
	@PostMapping("/customer-registration")
	public ResponseEntity<SuccessResponse> addCustomer(@RequestBody CustomerRegistrationPojo customerRegistrationPojo) {
		return new ResponseEntity<>(new SuccessResponse(false, REGISTRATION_SUCCESS_MESSAGE,
				customerRegistrationService.addCustomer(customerRegistrationPojo)), HttpStatus.OK);
	}

	/*
	 * @Author : Sudharshan B S
	 */
	@Operation(summary = "")
	@GetMapping("/customer")
	public ResponseEntity<CustomerRegistrationResponse> findCustomerById(@RequestParam String customerId) {
		return new ResponseEntity<>(new CustomerRegistrationResponse(false, "Customer Found Successfully",
				customerRegistrationService.findCustomerById(customerId)), HttpStatus.OK);

	}

}
