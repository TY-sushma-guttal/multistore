package com.mrmrscart.notificationreportlogservice.feign;


import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.mrmrscart.notificationreportlogservice.feign.entity.ESupplierStatus;
import com.mrmrscart.notificationreportlogservice.feign.pojo.SupplierChangesHistoryPojo;
import com.mrmrscart.notificationreportlogservice.response.CustomerResponse;
import com.mrmrscart.notificationreportlogservice.response.SupplierResponse;

@FeignClient(name="user-service")
public interface UserService {

	@PutMapping("/api/v1/users/supplier/profile")
	public ResponseEntity<SupplierResponse> updateSupplierProfile(@RequestBody SupplierChangesHistoryPojo supplierChangesHistoryPojo);
	
	@PutMapping("/api/v1/users/supplier-profile/approval")
	public ResponseEntity<SupplierResponse> approveSupplierProfile(@RequestBody SupplierChangesHistoryPojo supplierChangesHistoryPojo) ;
	
	@GetMapping("/api/v1/users/supplier-registration")
	public ResponseEntity<SupplierResponse> getSupplierById(@RequestParam String id,
			@RequestParam ESupplierStatus status);
	
	@GetMapping("/api/v1/users/customers/favourite-customers/{supplierId}")
	public ResponseEntity<CustomerResponse> getCustomers(@PathVariable String supplierId);
	
	@PostMapping("/api/v1/users/customers")
	public ResponseEntity<CustomerResponse> getCustomers(@RequestBody List<String> customerIds);
}
