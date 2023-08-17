package com.mrmrscart.orderspaymentsservice.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.mrmrscart.orderspaymentsservice.feign.pojo.OrderedProductsPojo;
import com.mrmrscart.orderspaymentsservice.feign.pojo.OrdersRequestPojo;
import com.mrmrscart.orderspaymentsservice.feign.response.ProductsResponse;
import com.mrmrscart.orderspaymentsservice.pojo.order.IncreaseStockQuantityPojo;
import com.mrmrscart.orderspaymentsservice.response.order.SuccessResponse;

@FeignClient(name = "product-categories-service")
public interface ProductServiceClient {

	@PostMapping("/api/v1/products/product-feign")
	public ResponseEntity<SuccessResponse> getProductIds(@RequestBody OrdersRequestPojo ordersRequestPojo);

	@PostMapping("/api/v1/products/order/order-products")
	public ResponseEntity<ProductsResponse> validateOrderedProducts(
			@RequestBody OrderedProductsPojo orderedProductsPojo);

	@PutMapping("/api/v1/products/increase-qty")
	public ResponseEntity<SuccessResponse> increaseStockQuantity(
			@RequestBody IncreaseStockQuantityPojo increaseStockQuantityPojo);
}
