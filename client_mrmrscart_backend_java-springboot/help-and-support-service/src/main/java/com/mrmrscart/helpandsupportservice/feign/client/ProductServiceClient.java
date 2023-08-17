package com.mrmrscart.helpandsupportservice.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;

import com.mrmrscart.helpandsupportservice.feign.response.ProductResponse;

@FeignClient(name = "product-categories-service")
public interface ProductServiceClient {

	@PutMapping("/api/v1/admin/products/{productVariationId}")
	public ResponseEntity<ProductResponse> setProductStatus(String productVariationId);
}
