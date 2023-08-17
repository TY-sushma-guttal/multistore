package com.mrmrscart.orderspaymentsservice.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mrmrscart.orderspaymentsservice.feign.pojo.ESupplierStatus;
import com.mrmrscart.orderspaymentsservice.feign.response.CustomerRegistrationResponse;
import com.mrmrscart.orderspaymentsservice.response.order.SupplierRegistartionGetResponse;

@FeignClient(name = "user-service")
public interface UserServiceClient {

	@GetMapping("/api/v1/users/customer")
	public ResponseEntity<CustomerRegistrationResponse> findCustomerById(@RequestParam("customerId") String customerId);

	@GetMapping("/api/v1/users/supplier-registration")
	public ResponseEntity<SupplierRegistartionGetResponse> getSupplierById(@RequestParam String id,
			@RequestParam ESupplierStatus status);
}
