package com.mrmrscart.userservice.feign.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.mrmrscart.userservice.feign.pojo.MasterProductIdAndVariationIdPojo;
import com.mrmrscart.userservice.feign.response.MainCategoryResponse;
import com.mrmrscart.userservice.feign.response.MasterProductAndVariationResponse;
import com.mrmrscart.userservice.feign.response.MasterProductGetResponse;
import com.mrmrscart.userservice.feign.response.ProductVariationGetResponse;
import com.mrmrscart.userservice.feign.response.ProductVariationResponse;
import com.mrmrscart.userservice.feign.response.SubCategoryGetResponse;

@FeignClient(name = "product-categories-service")
public interface ProductCategoryClient {

	@GetMapping("/api/v1/products/sub-category/{id}")
	public ResponseEntity<SubCategoryGetResponse> getSubCategory(@PathVariable String id);

	@GetMapping("/api/v1/products/master-product/{id}")
	public ResponseEntity<MasterProductGetResponse> getSimpleOrVariableProduct(@PathVariable String id);

	@GetMapping("/api/v1/products/product-variation/{id}")
	public ResponseEntity<ProductVariationGetResponse> getProductVariationWithId(@PathVariable String id);

	@PostMapping("/api/v1/products/master-product/product-variation")
	public ResponseEntity<MasterProductAndVariationResponse> getProductListByProductIdAndVariationId(
			@RequestBody List<MasterProductIdAndVariationIdPojo> data);

	@GetMapping("/api/v1/products/main-category/{mainCategoryId}")
	public ResponseEntity<MainCategoryResponse> getMainCategory(@PathVariable String mainCategoryId);

	@GetMapping("/api/v1/products/variation")
	public ResponseEntity<ProductVariationResponse> getProductVariationBySkuId(@RequestParam("skuId") String skuId);
}
