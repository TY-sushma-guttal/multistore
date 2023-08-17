package com.mrmrscart.userservice.controller.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mrmrscart.userservice.response.admin.SuccessResponse;
import com.mrmrscart.userservice.service.customer.CustomerStoreService;
import com.mrmrscart.userservice.wrapper.customer.CustomerWrapper;
import static com.mrmrscart.userservice.common.customer.CustomerConstant.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/users")
public class CustomerStoreController {

	@Autowired
	private CustomerStoreService customerStoreService;

	@GetMapping("/customers/favourite-customers/{supplierId}")
	public ResponseEntity<SuccessResponse> getFavouritreCustomers(@PathVariable String supplierId) {
		return new ResponseEntity<>(new SuccessResponse(false, "Customers Fetched Successfully",
				customerStoreService.getFavouritreCustomers(supplierId)), HttpStatus.OK);
	}

	/**
	 * @author Hemadri G
	 * 
	 * @param supplierId
	 * @return
	 */
	@GetMapping("/supplier/customers/{supplierId}")
	public ResponseEntity<SuccessResponse> getCustomers(@PathVariable String supplierId) {
		List<CustomerWrapper> customers = customerStoreService.getCustomers(supplierId);
		if (!customers.isEmpty()) {
			return new ResponseEntity<>(new SuccessResponse(false, "Customers Fetched Successfully", customers),
					HttpStatus.OK);
		} else
			return new ResponseEntity<>(
					new SuccessResponse(true, "There Are No Customers For The Supplier Store", customers),
					HttpStatus.OK);
	}

	/*
	 * @Author : Hemadri G
	 * 
	 */
	@GetMapping("/customers/validate-store")
	public ResponseEntity<SuccessResponse> validateStoreCode(@RequestParam String storeCode) {
		return new ResponseEntity<>(
				new SuccessResponse(false, VALID_STORE_CODE, customerStoreService.validateStoreCode(storeCode)),
				HttpStatus.OK);
	}

}
