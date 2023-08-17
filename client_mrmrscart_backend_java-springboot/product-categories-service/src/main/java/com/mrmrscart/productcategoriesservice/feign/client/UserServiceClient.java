package com.mrmrscart.productcategoriesservice.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.mrmrscart.productcategoriesservice.feign.enums.ESupplierStatus;
import com.mrmrscart.productcategoriesservice.feign.response.MarketingToolResponse;
import com.mrmrscart.productcategoriesservice.feign.response.SupplierRegistrationGetResponse;
import com.mrmrscart.productcategoriesservice.feign.response.SupplierStoreInfoResponse;

@FeignClient(name = "user-service")
public interface UserServiceClient {

	@GetMapping("/api/v1/users/supplier-registration")
	public ResponseEntity<SupplierRegistrationGetResponse> getSupplierById(@RequestParam String id,
			@RequestParam ESupplierStatus status);

	@PutMapping("/api/v1/users/supplier/supplier-store")
	public ResponseEntity<SupplierStoreInfoResponse> updateProductCount(@RequestParam String supplierId,
			@RequestParam int productCount, @RequestHeader String userId);

	@GetMapping("/api/v1/users/marketing-tool/subscription/{purchasedById}/{purchasedByType}/{adminMarketingToolName}")
	public ResponseEntity<MarketingToolResponse> getSubscription(@PathVariable String purchasedById,
			@PathVariable String purchasedByType, @PathVariable String adminMarketingToolName) ;
	
	@GetMapping("/api/v1/users/supplier-registration/{id}")
	public ResponseEntity<SupplierRegistrationGetResponse> getSupplierById(@PathVariable String id);

}
