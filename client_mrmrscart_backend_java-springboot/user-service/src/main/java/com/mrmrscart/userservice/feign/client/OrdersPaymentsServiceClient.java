package com.mrmrscart.userservice.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mrmrscart.userservice.feign.response.OrderResponse;
import com.mrmrscart.userservice.response.supplier.SuccessResponse;

@FeignClient(name="orders-payments-service")
public interface OrdersPaymentsServiceClient {

	@GetMapping("/api/v1/order-payment/reports/revenue-sales")
	public ResponseEntity<SuccessResponse> getTotalOrdersCount(@RequestParam String supplierId);
	
	@GetMapping("/api/v1/order-payment/reports/orders")
	public ResponseEntity<SuccessResponse> getOrdersTotalCount(@RequestParam String supplierId);
	
	@GetMapping("/api/v1/order-payment/reports/orders/info")
	public ResponseEntity<SuccessResponse> getOrdersMonthInfo(@RequestParam int year,@RequestParam String supplierId);
	
	@GetMapping("/api/v1/order-payment/reports/orders/summary")
	public ResponseEntity<OrderResponse> getOrdersSummary(@RequestParam int year,@RequestParam String supplierId,@RequestParam int pageNumber,
			@RequestParam int pageSize, @RequestParam(required = false) String status);
	
	@GetMapping("/api/v1/order-payment/reports/revenue-sales/amount")
	public ResponseEntity<SuccessResponse> getSumOfOrderAmount(@RequestParam String supplierId);
	
	@GetMapping("/api/v1/order-payment/reports/revenue-sales/sales")
	public ResponseEntity<SuccessResponse> getSumOfOrderAmountMonthInfo(@RequestParam int year,@RequestParam String supplierId);
}
